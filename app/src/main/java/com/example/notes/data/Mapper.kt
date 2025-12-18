package com.example.notes.data

import com.example.notes.domain.Note

fun Note.toNoteDbModel() : NoteDbModel{
    return NoteDbModel(id, content, updatedAt, isPinned, title)
}

fun NoteDbModel.toEntity() : Note{
    return Note(id, content, updatedAt, isPinned, title)
}