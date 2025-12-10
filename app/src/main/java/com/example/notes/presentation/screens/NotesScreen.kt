package com.example.notes.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel()
){
    val state by viewModel.state.collectAsState()
    Row(
        modifier= Modifier.fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(top = 42.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        state.pinnedNotes.forEach {
            Text(text = "title${it.id} content${it.id}",
                fontSize = 32.sp,
                )
        }
    }
    Column(
        modifier = Modifier
            .padding(top = 80.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        state.otherNotes.forEach {
            Text(text = "title${it.id} - content${it.id}"
                , fontSize = 32.sp,
                modifier = Modifier.clickable{
                    viewModel.processCommand(NotesCommand.PinnedNotes(it.id))
                }
            )
        }
    }
}