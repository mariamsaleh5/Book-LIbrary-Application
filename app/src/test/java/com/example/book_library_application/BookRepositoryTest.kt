package com.example.book_library_application

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class BookRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var bookDao: BookDao

    private lateinit var repository: BookRepository

    private val testBook = Book(
        id = 1,
        name = "The Silent Patient",
        goodreadsUrl = "https://www.goodreads.com/book/show/40097951",
        isRead = false
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = BookRepository(bookDao)
    }

    @Test
    fun `getAllBooks returns flow of books from dao`() = runTest {

        val booksList = listOf(testBook)
        whenever(bookDao.getAllBooks()).thenReturn(flowOf(booksList))

        val result = repository.getAllBooks()

        verify(bookDao).getAllBooks()

    }

    @Test
    fun `getReadBooks returns flow of read books from dao`() = runTest {

        val readBook = testBook.copy(isRead = true)
        val readBooksList = listOf(readBook)
        whenever(bookDao.getReadBooks()).thenReturn(flowOf(readBooksList))

        val result = repository.getReadBooks()

        verify(bookDao).getReadBooks()
    }

    @Test
    fun `addBook calls dao insertBook`() = runTest {

        repository.addBook(testBook)

        verify(bookDao).insertBook(testBook)
    }

    @Test
    fun `deleteBook calls dao deleteBook`() = runTest {
        repository.deleteBook(testBook)


        verify(bookDao).deleteBook(testBook)
    }

    @Test
    fun `toggleRead toggles book read status and updates in dao`() = runTest {

        val book = testBook.copy(isRead = false)

        repository.toggleRead(book)


        assert(book.isRead) // Should be toggled to true
        verify(bookDao).updateBook(book)
    }

    @Test
    fun `toggleRead from true to false works correctly`() = runTest {

        val book = testBook.copy(isRead = true)

        repository.toggleRead(book)

        assert(!book.isRead) // Should be toggled to false
        verify(bookDao).updateBook(book)
    }
}
