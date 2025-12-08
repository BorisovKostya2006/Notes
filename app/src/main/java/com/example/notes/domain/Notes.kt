package com.example.notes.domain

data class Notes(
    val id : Int,
    val content : String,
    val updatedAt : Long,
    val isPinned : Boolean
)
