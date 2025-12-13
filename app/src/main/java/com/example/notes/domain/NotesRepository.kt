package com.example.notes.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Contextual

interface NotesRepository {
    suspend fun addNote(title : String, content : String)
    suspend fun deleteNote(noteId : Int)
    suspend fun editNote(note : Note)
    fun getAllNotes() : Flow<List<Note>>
    suspend fun getNote(noteId : Int) : Note
    fun searchNotes(query : String) : Flow<List<Note>>
    suspend fun switchPinnedStatus(noteId : Int)
}