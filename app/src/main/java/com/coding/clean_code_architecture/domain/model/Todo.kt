package com.coding.clean_code_architecture.domain.model

data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean,
)

