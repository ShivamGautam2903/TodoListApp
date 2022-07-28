package com.example.notesappfinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappfinal.db.TodoDao
import kotlinx.coroutines.launch

class TodoViewModel(private val dao: TodoDao): ViewModel() {

    val todos = dao.getAllTodos()

    fun insertTodo(todo : TodoX) = viewModelScope.launch{
        dao.insertTodo(todo)
    }

    fun updateTodo(todo: TodoX) = viewModelScope.launch{
        dao.updateTodo(todo)
    }

    fun deleteTodo(todo:TodoX) = viewModelScope.launch {
        dao.deleteTodo(todo)
    }
    
}