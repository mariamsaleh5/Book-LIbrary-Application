package com.example.book_library_application

import org.junit.Test
import org.junit.Assert.*


class BookTest {

    @Test
    fun correct_default_values() {
        val book = Book(
            name = "The Silent Patient",
            goodreadsUrl = "https://www.goodreads.com/book/show/40097951"
        )
        assertEquals("The Silent Patient", book.name)
        assertEquals("https://www.goodreads.com/book/show/40097951", book.goodreadsUrl)
        assertEquals(0L, book.id)
        assertFalse(book.isRead)
    }

    @Test
    fun created_with_customid() {
        val book = Book(
            id = 42,
            name = "Test Book",
            goodreadsUrl = "https://example.com",
            isRead = true
        )

        assertEquals(42L, book.id)
        assertEquals("Test Book", book.name)
        assertEquals("https://example.com", book.goodreadsUrl)
        assertTrue(book.isRead)
    }

    @Test
    fun isRead_toggled() {

        val book = Book(
            name = "Test Book",
            goodreadsUrl = "https://example.com",
            isRead = false
        )
        book.isRead = true
        assertTrue(book.isRead)
    }

}