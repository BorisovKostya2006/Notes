package com.example.notes.domain

import android.R

class AddNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(title : String, content : String, isPinned : Boolean, updatedAt : Long, id : Int){
        repository.addNote(title, content, isPinned, updatedAt, id)
    }
}