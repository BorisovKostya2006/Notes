package com.example.notes.presentation.screens

import android.R.attr.query
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import com.example.notes.data.TestNotesRepositoryImpl
import com.example.notes.domain.GetAllNotesUseCase
import com.example.notes.domain.Note
import com.example.notes.domain.SearchNotesUseCase
import com.example.notes.domain.SwitchPinnedStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
    init {
        query
            .flatMapLatest {
                if (it.isBlank()) {
                    getAllNotesUseCase()
                } else {
                    searchNotesUseCase(it)
                }
            }
                    .onEach{
                        val pinnedNotes: List<Note> = it.filter{it.isPinned }
                        val otherNotes : List<Note> = it.filter{!it.isPinned }
                        _state.update{it.copy(pinnedNotes = pinnedNotes, otherNotes = otherNotes) }
                    }
            .launchIn(scope)
                    }




    fun proccesCommand(command: NotesCommand){
        when(command) {
            is NotesCommand.PinnedNotes -> switchPinnedStatusUseCase(noteId = command.noteId)
            is NotesCommand.SearchNotes -> TODO()
        }
    }
}

sealed interface NotesCommand{
    data class SearchNotes(val searchNotes : String) : NotesCommand
    data class PinnedNotes( val noteId : Int) : NotesCommand
}

data class  NotesScreenState(
    val searchNotes : String = "",
    val notes : List<Note> = listOf(),
    val pinnedNotes : List<Note> = listOf(),
    val otherNotes : List<Note> = listOf(),
)