package com.example.notes.data

import android.content.Context
import com.example.notes.domain.Note
import com.example.notes.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl private constructor(context: Context) : NotesRepository{
    private val notesDatabase = NotesDatabase.getInstance(context)
    private val notesDao = notesDatabase.notesDao()

    override suspend fun addNote(
        title: String,
        content: String,
        isPinned: Boolean,
        updatedAt: Long,
        id: Int
    ) {
        val noteDbModel = NoteDbModel(
            id = 0,
            content = content,
            updatedAt = updatedAt,
            isPinned = isPinned,
            title = title
        )
        notesDao.addNote(noteDbModel)
        
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }

    override suspend fun editNote(note: Note) {
        notesDao.addNote(note.toNoteDbModel())
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map {notesDbModel ->
            notesDbModel.map {
                it.toEntity()
            }
        }
    }

    override suspend fun getNote(noteId: Int): Note {
       return notesDao.getNote(noteId).toEntity()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map {notesDbModel ->
            notesDbModel.map {
                it.toEntity()
            }
        }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }

    companion object{
        private val LOCK = Any()
        private var instance : NotesRepositoryImpl? = null
        fun getInstance(context: Context) : NotesRepositoryImpl{
            instance?.let {
                return it
            }
            synchronized(LOCK){
                instance?.let {
                    return it
                }
                return NotesRepositoryImpl(context).also {
                    instance = it
                }
            }
        }
    }

}