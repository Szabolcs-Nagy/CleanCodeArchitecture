package com.coding.clean_code_architecture.data.remote

import com.coding.clean_code_architecture.data.remote.dto.TodoDto
import retrofit2.http.GET

interface TodoApiService {
    @GET("todos")
    suspend fun getTodos(): List<TodoDto>
}

