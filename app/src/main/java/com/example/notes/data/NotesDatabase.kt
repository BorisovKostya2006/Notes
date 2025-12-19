package com.example.notes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [NoteDbModel::class]
)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao() : NotesDao

    companion object{
        private val LOCK = Any()
        private var instance : NotesDatabase? = null
        fun getInstance(context: Context) : NotesDatabase{
            instance?.let {
                return it
            }
            synchronized(LOCK){
                instance?.let {
                    return it
                }
                return Room.databaseBuilder(
                    context = context,
                    klass = NotesDatabase::class.java,
                    name = "notes.db"
                ).build().also {
                    instance = it
                }
            }
        }

    }

}