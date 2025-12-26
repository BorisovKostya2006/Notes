package com.example.notes.domain

import javax.inject.Inject


class AddNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(title : String, content : List<ContentItem>, isPinned : Boolean, updatedAt : Long, id : Int){
        repository.addNote(title, content, isPinned, updatedAt, id)
    }
}