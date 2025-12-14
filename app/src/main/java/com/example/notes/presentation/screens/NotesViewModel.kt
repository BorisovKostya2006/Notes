package com.example.notes.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.TestNotesRepositoryImpl
import com.example.notes.domain.AddNoteUseCase
import com.example.notes.domain.DeleteNoteUseCase
import com.example.notes.domain.EditNoteUseCase
import com.example.notes.domain.GetAllNotesUseCase
import com.example.notes.domain.Note
import com.example.notes.domain.SearchNotesUseCase
import com.example.notes.domain.SwitchPinnedStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel(){
    private val _state = MutableStateFlow(NotesScreenState())
    val state  = _state.asStateFlow()
    private val query = MutableStateFlow("")
    val repository = TestNotesRepositoryImpl
    val searchNotesUseCase = SearchNotesUseCase(repository)
    val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)
    val getAllNotesUseCase = GetAllNotesUseCase(repository)
    val addNoteUseCase = AddNoteUseCase(repository)

    val editNoteUseCase = EditNoteUseCase(repository)
    val deleteNoteUseCase = DeleteNoteUseCase(repository)

    init {
        addSomeNotes()
        query
            .onEach { input ->
                _state.update { it.copy(query= input) }

            }
            .flatMapLatest {input ->
                if (input.isBlank()) {
                    getAllNotesUseCase()
                } else {
                    searchNotesUseCase(input)
                }
            }
                    .onEach{notes ->
                        val pinnedNotes: List<Note> = notes.filter{it.isPinned }
                        val otherNotes : List<Note> = notes.filter{!it.isPinned }
                        _state.update{it.copy(pinnedNotes = pinnedNotes, otherNotes = otherNotes) }
                    }
            .launchIn(viewModelScope)
                    }




    fun processCommand(command: NotesCommand){
        viewModelScope.launch {
            when(command) {
                is NotesCommand.DeleteNote -> deleteNoteUseCase(command.noteId)
                is NotesCommand.EditedNote -> editNoteUseCase(command.note)
                is NotesCommand.PinnedNotes -> switchPinnedStatusUseCase(noteId = command.noteId)
                is NotesCommand.InputSearchNotes -> query.update {
                    command.query.trim()
                }
            }
        }

    }
    //TODO for test
    private fun addSomeNotes(){
        viewModelScope.launch {
            repeat(10_000){
                addNoteUseCase(title = "title$it titletitletitletitletitletitletitletitle", content = "content$it contentcontentcontentcontentcontentcontent" )
            }
        }

    }


}

sealed interface NotesCommand{
    data class EditedNote(val note : Note) : NotesCommand
    data class DeleteNote(val noteId : Int) : NotesCommand
    data class InputSearchNotes(val query: String) : NotesCommand
    data class PinnedNotes( val noteId : Int) : NotesCommand
}

data class  NotesScreenState(
    val query : String = "",
    val pinnedNotes : List<Note> = listOf(),
    val otherNotes : List<Note> = listOf(),
)