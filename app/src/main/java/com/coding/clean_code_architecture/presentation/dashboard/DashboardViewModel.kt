package com.coding.clean_code_architecture.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.clean_code_architecture.domain.model.DashboardTodo
import com.coding.clean_code_architecture.domain.usecase.GetDashboardTodosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DashboardNavigationEvent {
    data class NavigateToDetails(val todo: DashboardTodo) : DashboardNavigationEvent
}

class DashboardViewModel(
    private val getDashboardTodosUseCase: GetDashboardTodosUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<DashboardNavigationEvent>(extraBufferCapacity = 1)
    val navigationEvents: SharedFlow<DashboardNavigationEvent> = _navigationEvents.asSharedFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                getDashboardTodosUseCase()
            }.onSuccess { todos ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        todos = todos,
                        errorMessage = null,
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.localizedMessage ?: "Failed to load dashboard data",
                    )
                }
            }
        }
    }

    fun onTodoClicked(todo: DashboardTodo) {
        _navigationEvents.tryEmit(DashboardNavigationEvent.NavigateToDetails(todo))
    }
}

