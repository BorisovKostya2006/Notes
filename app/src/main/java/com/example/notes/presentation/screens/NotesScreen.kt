package com.example.notes.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.domain.Note

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel()
){
    val state by viewModel.state.collectAsState()
    LazyRow(
        modifier= Modifier.fillMaxWidth()
            .padding(top = 42.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        items(items = state.pinnedNotes, key = {it.id}){note ->
            NoteCard(note = note,
                onNoteClick = { viewModel.processCommand(NotesCommand.PinnedNotes(it.id)) },
            )
        }
    }
    LazyColumn(
        modifier = Modifier
            .padding(top = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        items(items = state.pinnedNotes, key = {it.id}){note ->
        NoteCard(
                    note = note,
                    onNoteClick = { viewModel.processCommand(NotesCommand.PinnedNotes(it.id)) },
                )
            }
        }

    }


@Composable
fun NoteCard(
    modifier : Modifier = Modifier,
    onNoteClick: (Note) -> Unit,
    note: Note
)
{
    Text(text = "title${note.id} - content${note.id}"
        , fontSize = 32.sp,
        modifier = Modifier.clickable{
            onNoteClick(note)
        }
    )
}