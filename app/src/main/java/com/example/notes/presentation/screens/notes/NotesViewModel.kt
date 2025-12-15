package com.example.notes.presentation.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.TestNotesRepositoryImpl
import com.example.notes.domain.GetAllNotesUseCase
import com.example.notes.domain.Note
import com.example.notes.domain.SearchNotesUseCase
import com.example.notes.domain.SwitchPinnedStatusUseCase
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


    init {
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
                is NotesCommand.PinnedNotes -> switchPinnedStatusUseCase(noteId = command.noteId)
                is NotesCommand.InputSearchNotes -> query.update {
                    command.query.trim()
                }
            }
        }

    }



}

sealed interface NotesCommand{
    data class InputSearchNotes(val query: String) : NotesCommand
    data class PinnedNotes( val noteId : Int) : NotesCommand
}

data class  NotesScreenState(
    val query : String = "",
    val pinnedNotes : List<Note> = listOf(),
    val otherNotes : List<Note> = listOf(),
)