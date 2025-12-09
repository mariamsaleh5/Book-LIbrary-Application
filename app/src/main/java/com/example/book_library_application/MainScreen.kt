package com.example.book_library_application

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.book_library_application.Book
import com.example.book_library_application.BookViewModel
import com.example.book_library_application.ui.theme.BookLIbraryApplicationTheme
import com.example.book_library_application.ui.theme.CardColors
import android.net.Uri
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: BookViewModel
) {
    val books by viewModel.allBooks.collectAsStateWithLifecycle(initialValue = emptyList())
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var showScrollToTop by remember { mutableStateOf(false) }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemIndex > 0 }
            .collect { showScrollToTop = it }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Book Library",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(
                state = scrollState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(books) { book ->
                    val cardColor = CardColors[kotlin.math.abs(book.name.hashCode()) % CardColors.size]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = cardColor
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Book,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                                Text(
                                    text = book.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
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
                                    color = Color.Black.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Checkbox(
                                    checked = book.isRead,
                                    onCheckedChange = { viewModel.toggleRead(book) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.secondary,
                                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                IconButton(onClick = { viewModel.deleteBook(book) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete book",
                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = showScrollToTop,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "Scroll to top"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookLIbraryApplicationTheme {
        val dummyDao = object : com.example.book_library_application.BookDao {
            override fun getAllBooks(): kotlinx.coroutines.flow.Flow<List<Book>> = kotlinx.coroutines.flow.flowOf(emptyList())
            override fun getReadBooks(): kotlinx.coroutines.flow.Flow<List<Book>> = kotlinx.coroutines.flow.flowOf(emptyList())
            override suspend fun insertBook(book: Book) {}
            override suspend fun updateBook(book: Book) {}
            override suspend fun deleteBook(book: Book) {}
        }
        val dummyRepository = com.example.book_library_application.BookRepository(dummyDao)
        val dummyViewModel = com.example.book_library_application.BookViewModel(dummyRepository)
        MainScreen(rememberNavController(), dummyViewModel)
    }
}