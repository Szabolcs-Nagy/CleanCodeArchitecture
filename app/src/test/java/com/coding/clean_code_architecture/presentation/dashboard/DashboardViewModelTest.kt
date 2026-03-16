package com.coding.clean_code_architecture.presentation.dashboard

import com.coding.clean_code_architecture.domain.model.DashboardTodo
import com.coding.clean_code_architecture.domain.model.Todo
import com.coding.clean_code_architecture.domain.repository.TodoRepository
import com.coding.clean_code_architecture.domain.usecase.GetDashboardTodosUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.async
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @Test
    fun `onTodoClicked emits navigate to details event`() = runTest {
        val repository = object : TodoRepository {
            override suspend fun getTodos(): List<Todo> = emptyList()
        }
        val viewModel = DashboardViewModel(GetDashboardTodosUseCase(repository))
        val todo = DashboardTodo(
            id = 1,
            title = "delectus aut autem",
            completed = false,
            dateTime = "2026-03-16 10:10:10",
        )
        val eventDeferred = async { viewModel.navigationEvents.first() }
        runCurrent()

        viewModel.onTodoClicked(todo)

        val event = eventDeferred.await()
        assertEquals(DashboardNavigationEvent.NavigateToDetails(todo), event)
    }
}

