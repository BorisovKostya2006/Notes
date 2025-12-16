package com.example.notes.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notes.presentation.screens.creation.CreateNoteScreen
import com.example.notes.presentation.screens.edit.EditNoteScreen
import com.example.notes.presentation.screens.notes.NotesScreen
import com.example.notes.presentation.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                Log.d("empty screen", "MainActivity")
                EditNoteScreen(
                    noteId = 5,
                    onFinished = {
                      Log.d("MainActivity","onFinished")
                   }
                )
//                CreateNoteScreen(
//                    onFinished = {
//                        Log.d("MainActivity","onFinished")
//                    }
//                )
//                NotesScreen(
//                    onNoteClick = {
//                        Log.d("MainActivity","onNoteClick")
//                    },
//                    onAddNoteClick = {
//                        Log.d("MainActivity","onAddNoteClick")
//                    }
//                )
            }

        }
    }
}

