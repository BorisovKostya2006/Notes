package com.example.notes.domain

data class Note(
    val id : Int,
    val content : List<ContentItem>,
    val updatedAt : Long,
    val isPinned : Boolean,
    val title : String
)
