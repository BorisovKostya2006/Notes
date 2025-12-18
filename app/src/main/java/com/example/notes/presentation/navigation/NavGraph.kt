package com.example.notes.presentation.navigation

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notes.presentation.screens.creation.CreateNoteScreen
import com.example.notes.presentation.screens.edit.EditNoteScreen
import com.example.notes.presentation.screens.notes.NotesScreen

@Composable
fun NavGraph (){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route
    ){
        composable(Screen.Notes.route) {
            NotesScreen(
                onNoteClick = {
                    Log.d("MainActivity", "onNoteClick")
                    navController.navigate(Screen.EditNote.createRoute(it.id))
                },
                onAddNoteClick = {
                    Log.d("MainActivity", "onAddNoteClick")
                    navController.navigate(Screen.CreateNote.route)
                }
            )
        }
        composable(Screen.EditNote.route) {
            val noteId = Screen.EditNote.getNoteId(it.arguments)
            EditNoteScreen(
                noteId = noteId,
                onFinished = {
                    Log.d("MainActivity","onFinished")
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.CreateNote.route) {

            CreateNoteScreen(
                onFinished = {
                    Log.d("MainActivity","onFinished")
                    navController.popBackStack()
                }
            )
        }
    }



}

sealed class Screen(val route : String){
    data object Notes : Screen(route = "notes")
    data object EditNote : Screen(route = "edit_note/{note_id}") {
        fun createRoute(noteId : Int) : String{
            return "edit_note/${noteId}"
        }

        fun getNoteId(arguments : Bundle?) : Int{
            val noteId = arguments?.getString("note_id")?.toInt() ?: 0
            return noteId
        }
    }
    data object CreateNote : Screen(route = "CreateNote")


}