package com.coding.clean_code_architecture.domain.model

data class DashboardTodo(
    val id: Int,
    val title: String,
    val completed: Boolean,
    val dateTime: String,
)

