package com.example.notesappfinal.db

import com.example.notesappfinal.db.TodoX

data class Todo(
    val limit: Int,
    val skip: Int,
    val todos: List<TodoX>,
    val total: Int
)