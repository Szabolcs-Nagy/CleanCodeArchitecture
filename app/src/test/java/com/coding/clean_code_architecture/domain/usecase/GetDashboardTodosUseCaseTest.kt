package com.coding.clean_code_architecture.domain.usecase

import com.coding.clean_code_architecture.domain.model.Todo
import com.coding.clean_code_architecture.domain.repository.TodoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetDashboardTodosUseCaseTest {

    @Test
    fun `invoke adds date time to every todo`() = runTest {
        val repository = object : TodoRepository {
            override suspend fun getTodos(): List<Todo> = listOf(
                Todo(userId = 1, id = 1, title = "A", completed = false),
                Todo(userId = 2, id = 2, title = "B", completed = true),
            )
        }

        val useCase = GetDashboardTodosUseCase(repository)

        val result = useCase()

        assertEquals(2, result.size)
        assertTrue(result.all { it.dateTime.isNotBlank() })
        assertEquals("A", result[0].title)
        assertEquals("B", result[1].title)
    }
}

