package com.example.book_library_application

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.book_library_application.Book
import com.example.book_library_application.BookViewModel
import kotlin.text.isNotBlank

@Composable
fun AddBookScreen(
    navController: NavHostController,
    viewModel: BookViewModel
) {
    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Book Name") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Goodreads URL") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (name.isNotBlank() && url.isNotBlank()) {
                        viewModel.addBook(Book(name = name, goodreadsUrl = url))
                        navController.popBackStack()
                    }
                },
                enabled = name.isNotBlank() && url.isNotBlank()
            ) {
                Text("Save Book")
            }
        }
    }
}