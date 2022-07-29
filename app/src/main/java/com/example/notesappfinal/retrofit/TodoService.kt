package com.example.notesappfinal.retrofit

import retrofit2.Response
import com.example.notesappfinal.db.Todo
import retrofit2.http.GET

interface TodoService {

    @GET("/todos")
    suspend fun getTodos(): Response<Todo>
}
