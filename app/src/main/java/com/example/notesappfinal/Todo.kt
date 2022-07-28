package com.example.notesappfinal

data class Todo(
    val limit: Int,
    val skip: Int,
    val todos: List<TodoX>,
    val total: Int
)