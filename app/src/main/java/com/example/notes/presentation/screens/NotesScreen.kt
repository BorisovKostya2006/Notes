package com.example.notes.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.domain.Note
import com.example.notes.presentation.ui.theme.Green
import com.example.notes.presentation.ui.theme.Yellow100
import com.example.notes.presentation.ui.theme.Yellow200

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Title(text = "All Notes")
        }
        item {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                onQueryChange = { viewModel.processCommand(NotesCommand.InputSearchNotes(it)) },
                query = state.query
            )
        }
        item {
            Subtitle(text = "Pinned")
        }
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 42.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)

            ) {

                items(items = state.pinnedNotes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        onNoteClick = { viewModel.processCommand(NotesCommand.PinnedNotes(it.id)) },
                        onLongNoteClick = { viewModel.processCommand(NotesCommand.EditedNote(it)) },
                        onDoubleNoteClick = {
                            viewModel.processCommand(
                                NotesCommand.DeleteNote(
                                    noteId = it.id
                                )
                            )
                        },
                        background = Yellow200,
                    )
                }
            }
        }
        item {
            Subtitle(text = "Others")
        }
        items(items = state.otherNotes, key = { it.id }) { note ->
            NoteCard(
                modifier = Modifier.fillMaxWidth(),
                note = note,
                onNoteClick = { viewModel.processCommand(NotesCommand.PinnedNotes(it.id)) },
                onLongNoteClick = { viewModel.processCommand(NotesCommand.EditedNote(it)) },
                onDoubleNoteClick = { viewModel.processCommand(NotesCommand.DeleteNote(noteId = it.id)) },
                background = Green,
            )
        }
    }
}


@Composable
fun Subtitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Bold
    )
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
    query: String
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search notes"
            )
        },
        shape = RoundedCornerShape(10.dp)

    )
}


@Composable
fun Title(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}


@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    onNoteClick: (Note) -> Unit,
    onLongNoteClick: (Note) -> Unit,
    onDoubleNoteClick: (Note) -> Unit,
    note: Note,
    background: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .combinedClickable(
                onClick = { onNoteClick(note) },
                onLongClick = { onLongNoteClick(note) },
                onDoubleClick = { onDoubleNoteClick(note) }
            )
    ) {
        Text(
            text = note.title, fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            note.updatedAt.toString(), fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            note.content, fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }

}