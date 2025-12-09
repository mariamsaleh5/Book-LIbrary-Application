package com.example.book_library_application
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.book_library_application.Book
import com.example.book_library_application.BookViewModel
import com.example.book_library_application.ui.theme.BookLIbraryApplicationTheme
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.book_library_application.BookDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.text.startsWith
import kotlin.text.trim

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: BookViewModel
) {
    val books by viewModel.allBooks.collectAsStateWithLifecycle(initialValue = emptyList())
    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Book Library",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(books) { book ->
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
                                            val builder = CustomTabsIntent.Builder().setShowTitle(true)
                                            val customTabsIntent = builder.build()
                                            customTabsIntent.launchUrl(context, Uri.parse(url))
                                        } catch (e: Exception) {
                                            Log.e(
                                                "MainScreen",
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
                            IconButton(onClick = { viewModel.deleteBook(book) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete book",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                            Checkbox(
                                checked = book.isRead,
                                onCheckedChange = { viewModel.toggleRead(book) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookLIbraryApplicationTheme {
        val dummyDao = object : BookDao {
            override fun getAllBooks(): Flow<List<Book>> = flowOf(emptyList())
            override fun getReadBooks(): Flow<List<Book>> = flowOf(emptyList())
            override suspend fun insertBook(book: Book) {}
            override suspend fun updateBook(book: Book) {}
            override suspend fun deleteBook(book: Book) {}
        }
        val dummyRepository = com.example.book_library_application.BookRepository(dummyDao)
        val dummyViewModel = com.example.book_library_application.BookViewModel(dummyRepository)
        MainScreen(rememberNavController(), dummyViewModel)
    }
}