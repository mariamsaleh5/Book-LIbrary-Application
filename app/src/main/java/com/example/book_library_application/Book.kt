package com.example.booklibrary

import androidx.room.*


@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val goodreadsUrl: String,
    var isRead: Boolean = false  // Mutable for checkbox toggle
)