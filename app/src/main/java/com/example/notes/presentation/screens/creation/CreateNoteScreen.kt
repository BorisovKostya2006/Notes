package com.example.notes.presentation.screens.creation

import android.R
import android.content.IntentSender
import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.presentation.utils.DateFormatter.formateCurrentDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateNoteViewModel = viewModel(),
    onFinished: () -> Unit
){
    val state = viewModel.state.collectAsState()
    val currentState = state.value
    when(currentState) {
        is CreateNoteState.Creation -> {
            Scaffold(
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Create note",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                                    .clickable{
                                        viewModel.processCommand(CreateNoteCommand.Back)
                                    },
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    )
                }

            ) {innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding),

                    ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        value =currentState.title,
                        onValueChange = {
                            viewModel.processCommand(CreateNoteCommand.InputTitle(it))
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        placeholder = {
                            Text(
                                text = "Title",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        text = formateCurrentDate(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        )
                    TextField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        value =currentState.content,
                        onValueChange = {
                            viewModel.processCommand(CreateNoteCommand.InputContent(it))
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                        ),
                        placeholder = {
                            Text(
                                text = "Note something down",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                fontSize = 16.sp,

                            )
                        }
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        onClick = {
                          viewModel.processCommand(CreateNoteCommand.Save)
                      },
                        shape = RoundedCornerShape(10.dp),
                        enabled = currentState.isSaveEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                        )

                    ){
                        Text(
                            text = "Save note"
                        )
                    }
                }
            }


        }
        CreateNoteState.Finished -> {
            LaunchedEffect(key1= Unit) {
            onFinished()
        }
        }
    }
}