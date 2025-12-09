package com.example.notes.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Contextual

interface NotesRepository {
    fun addNote(title : String, content : String)
    fun deleteNote(noteId : Int)
    fun editNote(note : Note)
    fun getAllNotes() : Flow<List<Note>>
    fun getNote(noteId : Int) : Note
    fun searchNotes(query : String) : Flow<List<Note>>
    fun switchPinnedStatus(noteId : Int)
}