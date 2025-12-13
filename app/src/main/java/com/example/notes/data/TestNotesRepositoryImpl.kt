package com.example.notes.data

import com.example.notes.domain.Note
import com.example.notes.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object TestNotesRepositoryImpl : NotesRepository {
    private val notesListFlow = MutableStateFlow<List<Note>>(emptyList())

    override suspend fun addNote(title: String, content: String) {
        notesListFlow.update {oldList->
            val note = Note(id = oldList.size,
                content = content,
                title = title,
                updatedAt = System.currentTimeMillis(),
                isPinned = false)
            oldList + note
        }
    }

    override suspend fun deleteNote(noteId: Int) {
        notesListFlow.update { currentList ->
            currentList.filterNot {
                it.id == noteId }
        }
    }

    override suspend fun editNote(updatedNote: Note) {
        notesListFlow.update { currentList ->
            currentList.map { note ->
                if (note.id == updatedNote.id) updatedNote else note
            }
        }
    }

    override fun getAllNotes(): Flow<List<Note>> = notesListFlow

    override suspend fun getNote(noteId: Int): Note {
        return notesListFlow.value.first { it.id == noteId }
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesListFlow.map { currentList ->
            currentList.filter { note ->
                note.title.contains(query, ignoreCase = true) ||
                        note.content.contains(query, ignoreCase = true)
            }
        }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesListFlow.update { currentList ->
            currentList.map { note ->
                if (note.id == noteId) {
                    note.copy(isPinned = !note.isPinned)
                } else {
                    note
                }
            }
        }
    }
}


