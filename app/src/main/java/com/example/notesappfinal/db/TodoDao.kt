package com.example.notesappfinal.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notesappfinal.TodoX

@Dao
interface TodoDao {

    @Insert
    suspend fun insertTodo(todo: TodoX)
    @Update
    suspend fun updateTodo(todo: TodoX)
    @Delete
    suspend fun deleteTodo(todo: TodoX)

    @Query("SELECT * FROM todo_data_table")
    fun getAllTodos(): LiveData<List<TodoX>>
}