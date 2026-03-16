package com.coding.clean_code_architecture.data.repository

import com.coding.clean_code_architecture.data.mapper.toDomain
import com.coding.clean_code_architecture.data.remote.TodoApiService
import com.coding.clean_code_architecture.domain.model.Todo
import com.coding.clean_code_architecture.domain.repository.TodoRepository

class TodoRepositoryImpl(
    private val todoApiService: TodoApiService,
) : TodoRepository {
    override suspend fun getTodos(): List<Todo> {
        return todoApiService.getTodos().map { it.toDomain() }
    }
}

