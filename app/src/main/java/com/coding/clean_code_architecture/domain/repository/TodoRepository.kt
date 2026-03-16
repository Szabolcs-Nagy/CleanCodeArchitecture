package com.coding.clean_code_architecture.domain.repository

import com.coding.clean_code_architecture.domain.model.Todo

interface TodoRepository {
    suspend fun getTodos(): List<Todo>
}

