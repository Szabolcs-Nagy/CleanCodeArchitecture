package com.coding.clean_code_architecture.data.mapper

import com.coding.clean_code_architecture.data.remote.dto.TodoDto
import com.coding.clean_code_architecture.domain.model.Todo

fun TodoDto.toDomain(): Todo = Todo(
    userId = userId,
    id = id,
    title = title,
    completed = completed,
)

