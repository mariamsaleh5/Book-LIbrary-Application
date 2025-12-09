package com.example.book_library_application

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.book_library_application.Book
import com.example.book_library_application.BookViewModel
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.RowScope.weight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlin.text.startsWith
import kotlin.text.trim

@Composable
fun ReadBooksScreen(
    navController: NavHostController,
    viewModel: BookViewModel
) {
    val readBooks by viewModel.readBooks.collectAsStateWithLifecycle(initialValue = emptyList())
    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Read Books",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.testTag("ReadBooksTitle")
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (readBooks.isEmpty()) {
                Text("No books read yet. Mark some in the main list!")
            } else {
                LazyColumn {
                    items(readBooks) { book ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = book.name,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            try {
                                                var url = book.goodreadsUrl.trim()
                                                // Add https:// if missing
                                                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                                    url = "https://$url"
                                                }
                                                val intent =
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Log.e(
                                                    "ReadBooksScreen",
                                                    "Error opening URL: ${book.goodreadsUrl}",
                                                    e
                                                )
                                                Toast.makeText(
                                                    context,
                                                    "Error opening URL: ${e.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        },
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text("✓ Read", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}