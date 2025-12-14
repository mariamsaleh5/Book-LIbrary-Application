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
    fun addBook_calls_repository_addBook() = runTest {

        viewModel.addBook(testBook)
        advanceUntilIdle()

        verify(repository).addBook(testBook)
    }

    @Test
    fun deleteBook_calls_repository_deleteBook() = runTest {

        viewModel.deleteBook(testBook)
        advanceUntilIdle()


        verify(repository).deleteBook(testBook)
    }

    @Test
    fun toggleRead_calls_repository_toggleRead() = runTest {

        viewModel.toggleRead(testBook)
        advanceUntilIdle()


        verify(repository).toggleRead(testBook)
    }

    @Test
    fun multiple_addBook_calls_work_correctly() = runTest {

        val book1 = testBook
        val book2 = testBook.copy(id = 2, name = "Another Book")


        viewModel.addBook(book1)
        viewModel.addBook(book2)
        advanceUntilIdle()


        verify(repository).addBook(book1)
        verify(repository).addBook(book2)
    }

    @Test
    fun toggleRead_multiple_times_works_correctly() = runTest {

        viewModel.toggleRead(testBook)
        viewModel.toggleRead(testBook)
        advanceUntilIdle()


        verify(repository, times(2)).toggleRead(testBook)
    }

    @Test
    fun deleteBook_after_addBook_works_correctly() = runTest {

        viewModel.addBook(testBook)
        advanceUntilIdle()
        viewModel.deleteBook(testBook)
        advanceUntilIdle()


        verify(repository).addBook(testBook)
        verify(repository).deleteBook(testBook)
    }
}