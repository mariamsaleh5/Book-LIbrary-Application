package com.example.book_library_application

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.book_library_application.MainScreen
import com.example.book_library_application.AddBookScreen
import com.example.book_library_application.ReadBooksScreen
import com.example.book_library_application.ui.theme.BookLIbraryApplicationTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.book_library_application.Book
import com.example.book_library_application.BookDao
import com.example.book_library_application.BookDatabase
import kotlinx.coroutines.launch


class BookRepository(private val bookDao: BookDao) {
    fun getAllBooks(): kotlinx.coroutines.flow.Flow<List<Book>> = bookDao.getAllBooks()
    fun getReadBooks(): kotlinx.coroutines.flow.Flow<List<Book>> = bookDao.getReadBooks()
    suspend fun addBook(book: Book) = bookDao.insertBook(book)
    suspend fun toggleRead(book: Book) {
        book.isRead = !book.isRead
        bookDao.updateBook(book)
    }
    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)
}

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    val allBooks = repository.getAllBooks()
    val readBooks = repository.getReadBooks()

    fun addBook(book: Book) {
        viewModelScope.launch {
            repository.addBook(book)
        }
    }

    fun toggleRead(book: Book) {
        viewModelScope.launch {
            repository.toggleRead(book)
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }
}


class BookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {
    private lateinit var database: BookDatabase
    private lateinit var repository: BookRepository
    private lateinit var viewModelFactory: BookViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = BookDatabase.getDatabase(this)
        repository = BookRepository(database.bookDao())
        viewModelFactory = BookViewModelFactory(repository)

        enableEdgeToEdge()
        setContent {
            BookLIbraryApplicationTheme {
                val navController = rememberNavController()

                val sharedViewModel: BookViewModel by viewModels { viewModelFactory }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar(navController = navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("main") {
                            MainScreen(
                                navController = navController,
                                viewModel = sharedViewModel
                            )
                        }
                        composable("add") {
                            AddBookScreen(
                                navController = navController,
                                viewModel = sharedViewModel
                            )
                        }
                        composable("read") {
                            ReadBooksScreen(
                                navController = navController,
                                viewModel = sharedViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}