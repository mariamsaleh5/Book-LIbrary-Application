package com.example.book_library_application

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for Book data class using JUnit
 */
class BookTest {

    @Test
    fun `book is created with correct default values`() {
        // Given & When
        val book = Book(
            name = "The Silent Patient",
            goodreadsUrl = "https://www.goodreads.com/book/show/40097951"
        )

        // Then
        assertEquals("The Silent Patient", book.name)
        assertEquals("https://www.goodreads.com/book/show/40097951", book.goodreadsUrl)
        assertEquals(0L, book.id)
        assertFalse(book.isRead)
    }

    @Test
    fun `book can be created with custom id`() {
        // Given & When
        val book = Book(
            id = 42,
            name = "Test Book",
            goodreadsUrl = "https://example.com",
            isRead = true
        )

        // Then
        assertEquals(42L, book.id)
        assertEquals("Test Book", book.name)
        assertEquals("https://example.com", book.goodreadsUrl)
        assertTrue(book.isRead)
    }

    @Test
    fun `book isRead can be toggled`() {
        // Given
        val book = Book(
            name = "Test Book",
            goodreadsUrl = "https://example.com",
            isRead = false
        )

        // When
        book.isRead = true

        // Then
        assertTrue(book.isRead)
    }

    @Test
    fun `book copy works correctly`() {
        // Given
        val originalBook = Book(
            id = 1,
            name = "Original",
            goodreadsUrl = "https://original.com",
            isRead = false
        )

        // When
        val copiedBook = originalBook.copy(name = "Modified", isRead = true)

        // Then
        assertEquals(1L, copiedBook.id)
        assertEquals("Modified", copiedBook.name)
        assertEquals("https://original.com", copiedBook.goodreadsUrl)
        assertTrue(copiedBook.isRead)
        
        // Original should be unchanged
        assertEquals("Original", originalBook.name)
        assertFalse(originalBook.isRead)
    }

    @Test
    fun `two books with same data are equal`() {
        // Given
        val book1 = Book(
            id = 1,
            name = "Test",
            goodreadsUrl = "https://test.com",
            isRead = false
        )
        val book2 = Book(
            id = 1,
            name = "Test",
            goodreadsUrl = "https://test.com",
            isRead = false
        )

        // Then
        assertEquals(book1, book2)
        assertEquals(book1.hashCode(), book2.hashCode())
    }

    @Test
    fun `two books with different data are not equal`() {
        // Given
        val book1 = Book(
            id = 1,
            name = "Test 1",
            goodreadsUrl = "https://test1.com",
            isRead = false
        )
        val book2 = Book(
            id = 2,
            name = "Test 2",
            goodreadsUrl = "https://test2.com",
            isRead = true
        )

        // Then
        assertNotEquals(book1, book2)
    }

    @Test
    fun `book toString contains all properties`() {
        // Given
        val book = Book(
            id = 1,
            name = "Test Book",
            goodreadsUrl = "https://test.com",
            isRead = true
        )

        // When
        val bookString = book.toString()

        // Then
        assertTrue(bookString.contains("Test Book"))
        assertTrue(bookString.contains("https://test.com"))
        assertTrue(bookString.contains("true"))
    }
}
