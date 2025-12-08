package com.example.book_library_application

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for BookViewModel using JUnit and Mockito
 */
@ExperimentalCoroutinesApi
class BookViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: BookRepository

    private lateinit var viewModel: BookViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val testBook = Book(
        id = 1,
        name = "The Silent Patient",
        goodreadsUrl = "https://www.goodreads.com/book/show/40097951",
        isRead = false
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = BookViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `addBook calls repository addBook`() = runTest {
        // When
        viewModel.addBook(testBook)
        advanceUntilIdle() // Wait for coroutine to complete

        // Then
        verify(repository).addBook(testBook)
    }

    @Test
    fun `deleteBook calls repository deleteBook`() = runTest {
        // When
        viewModel.deleteBook(testBook)
        advanceUntilIdle() // Wait for coroutine to complete

        // Then
        verify(repository).deleteBook(testBook)
    }

    @Test
    fun `toggleRead calls repository toggleRead`() = runTest {
        // When
        viewModel.toggleRead(testBook)
        advanceUntilIdle() // Wait for coroutine to complete

        // Then
        verify(repository).toggleRead(testBook)
    }

    @Test
    fun `multiple addBook calls work correctly`() = runTest {
        // Given
        val book1 = testBook
        val book2 = testBook.copy(id = 2, name = "Another Book")

        // When
        viewModel.addBook(book1)
        viewModel.addBook(book2)
        advanceUntilIdle()

        // Then
        verify(repository).addBook(book1)
        verify(repository).addBook(book2)
    }

    @Test
    fun `toggleRead multiple times works correctly`() = runTest {
        // When
        viewModel.toggleRead(testBook)
        viewModel.toggleRead(testBook)
        advanceUntilIdle()

        // Then
        verify(repository, times(2)).toggleRead(testBook)
    }

    @Test
    fun `deleteBook after addBook works correctly`() = runTest {
        // When
        viewModel.addBook(testBook)
        advanceUntilIdle()
        viewModel.deleteBook(testBook)
        advanceUntilIdle()

        // Then
        verify(repository).addBook(testBook)
        verify(repository).deleteBook(testBook)
    }
}
