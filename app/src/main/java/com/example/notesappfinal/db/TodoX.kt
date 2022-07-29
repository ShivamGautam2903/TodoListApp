package com.example.notesappfinal.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_data_table")
data class TodoX(
    val completed: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val todo: String,
    val userId: Int
)