package com.example.notes.presentation.screens

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.domain.Note
import com.example.notes.presentation.ui.theme.Green
import com.example.notes.presentation.ui.theme.OtherNotesColors
import com.example.notes.presentation.ui.theme.PinnedNotesColors
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
            .padding(top = 42.dp)
    ) {
        item {
            Title(text = "All Notes",
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            SearchBar(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp),
                onQueryChange = { viewModel.processCommand(NotesCommand.InputSearchNotes(it)) },
                query = state.query
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Subtitle(text = "Pinned", modifier = Modifier.padding(horizontal = 24.dp))
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 24.dp)

            ) {

                itemsIndexed(items = state.pinnedNotes, key = {
                        _, note -> note.id
                }){ index ,note ->
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
                        background = PinnedNotesColors[index % PinnedNotesColors.size],
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Subtitle(text = "Others", modifier = Modifier.padding(horizontal = 24.dp))
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        itemsIndexed(items = state.otherNotes,
            key = {
            _, note -> note.id
            })
        {index, note ->
            NoteCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp),
                note = note,
                onNoteClick = { viewModel.processCommand(NotesCommand.PinnedNotes(it.id)) },
                onLongNoteClick = { viewModel.processCommand(NotesCommand.EditedNote(it)) },
                onDoubleNoteClick = { viewModel.processCommand(NotesCommand.DeleteNote(noteId = it.id)) },
                background = OtherNotesColors[index % OtherNotesColors.size],
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun Subtitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
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
        modifier = modifier.fillMaxWidth()
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedTextColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search notes",
                tint = MaterialTheme.colorScheme.onSurface
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
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            note.updatedAt.toString(), fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            note.content, fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }

}