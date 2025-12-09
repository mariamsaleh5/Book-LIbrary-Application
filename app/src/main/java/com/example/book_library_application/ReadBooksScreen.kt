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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.book_library_application.Book
import com.example.book_library_application.BookViewModel
import com.example.book_library_application.ui.theme.CardColors
import android.net.Uri
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.launch

@Composable
fun ReadBooksScreen(
    navController: NavHostController,
    viewModel: BookViewModel
) {
    val readBooks by viewModel.readBooks.collectAsStateWithLifecycle(initialValue = emptyList())
    val context = LocalContext.current

    if (readBooks.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Read Books",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag("ReadBooksTitle")
            )
            Text(
                text = "No books read yet. Mark some in the main list!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
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
                    text = "Read Books",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .testTag("ReadBooksTitle")
                )
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(readBooks) { book ->
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
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Book,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
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
                                        color = Color.Black.copy(alpha = 0.8f),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(
                                    text = "✓ Read",
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelLarge
                                )
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
}