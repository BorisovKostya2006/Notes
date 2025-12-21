package com.example.notes.presentation.screens.edit

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.NotesRepositoryImpl
import com.example.notes.domain.DeleteNoteUseCase
import com.example.notes.domain.EditNoteUseCase
import com.example.notes.domain.GetNoteUseCase
import com.example.notes.domain.Note
import com.example.notes.presentation.screens.notes.NotesViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = EditNoteViewModel.Factory::class)
class EditNoteViewModel @AssistedInject constructor(
    @Assisted("noteId") private val noteId: Int,
    val editNoteUseCase: EditNoteUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val getNoteUseCase: GetNoteUseCase
) : ViewModel(){

    val _state = MutableStateFlow<EditNoteState>(EditNoteState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val note = getNoteUseCase(noteId)
            Log.d("empty screen", note.toString())
            _state.update {
                EditNoteState.Edit(note)
            }

        }

    }
    fun processCommand(command: EditNoteCommand){
        when(command) {
            EditNoteCommand.Back -> {
                _state.update{
                    EditNoteState.Finished
                }
            }
            is EditNoteCommand.InputContent -> {
            _state.update { previousState ->
                if (previousState is EditNoteState.Edit){
                    val newNote = previousState.note.copy(content = command.content)
                    previousState.copy(note= newNote)
                }
                else{
                    previousState
                }

            }
            }

            is EditNoteCommand.InputTitle -> {
                _state.update {previousState ->
                    if (previousState is EditNoteState.Edit) {
                        val newNote = previousState.note.copy(title = command.title)
                        previousState.copy(note = newNote)
                    } else {
                        previousState
                    }
                }
            }
            EditNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update {previousState ->
                        if(previousState is EditNoteState.Edit){
                            val newNote = previousState.note
                            editNoteUseCase(newNote)
                            EditNoteState.Finished
                        }else{
                            previousState
                        }
                    }
                }
            }

            EditNoteCommand.Delete ->{
                viewModelScope.launch{
                    _state.update{previousState->
                        if(previousState is EditNoteState.Edit){
                            val deleteNote = previousState.note
                            deleteNoteUseCase(deleteNote.id)
                        }
                        EditNoteState.Finished
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory{
        fun create(
            @Assisted("noteId") noteId : Int
        ) : EditNoteViewModel
    }
}



sealed interface EditNoteCommand{
    data class InputTitle(val title: String) : EditNoteCommand
    data class InputContent(val content: String) : EditNoteCommand
    data object Save : EditNoteCommand
    data object Back : EditNoteCommand
    data object Delete : EditNoteCommand
}

sealed interface EditNoteState{
    data object Initial : EditNoteState
    data class Edit(
        val note : Note
    ) : EditNoteState{
        val isSaveEnabled: Boolean
            get() = note.title.isNotBlank() && note.content.isNotBlank()
    }
    data object Finished : EditNoteState
}