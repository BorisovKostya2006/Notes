package com.example.notes.presentation.screens

import androidx.lifecycle.ViewModel
import com.example.notes.data.TestNotesRepositoryImpl
import com.example.notes.domain.AddNoteUseCase
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

class NotesViewModel : ViewModel(){
    private val _state = MutableStateFlow(NotesScreenState())
    val state  = _state.asStateFlow()
    private val scope: CoroutineScope = CoroutineScope (Dispatchers.IO)
    private val query = MutableStateFlow("")
    val repository = TestNotesRepositoryImpl
    val searchNotesUseCase = SearchNotesUseCase(repository)
    val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)
    val getAllNotesUseCase = GetAllNotesUseCase(repository)
    val addNoteUseCase = AddNoteUseCase(repository)


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
            .launchIn(scope)
                    }




    fun processCommand(command: NotesCommand){
        when(command) {
            is NotesCommand.PinnedNotes -> switchPinnedStatusUseCase(noteId = command.noteId)
            is NotesCommand.SearchNotes -> query.update {
                command.query.trim()
            }
        }
    }
    //TODO for test
    private fun addSomeNotes(){
        repeat(50){
        addNoteUseCase(title = "title$it", content = "content$it" )
        }
    }


}

sealed interface NotesCommand{

    data class SearchNotes(val query: String) : NotesCommand
    data class PinnedNotes( val noteId : Int) : NotesCommand
}

data class  NotesScreenState(
    val query : String = "",
    val pinnedNotes : List<Note> = listOf(),
    val otherNotes : List<Note> = listOf(),
)