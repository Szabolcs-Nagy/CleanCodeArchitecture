package com.coding.clean_code_architecture.presentation.dashboard

import com.coding.clean_code_architecture.domain.model.DashboardTodo

data class DashboardUiState(
    val isLoading: Boolean = false,
    val todos: List<DashboardTodo> = emptyList(),
    val errorMessage: String? = null,
)

