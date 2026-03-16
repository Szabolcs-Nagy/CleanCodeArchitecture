package com.coding.clean_code_architecture.domain.usecase

import com.coding.clean_code_architecture.domain.model.DashboardTodo
import com.coding.clean_code_architecture.domain.repository.TodoRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GetDashboardTodosUseCase(
    private val todoRepository: TodoRepository,
) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    suspend operator fun invoke(): List<DashboardTodo> {
        val now = LocalDateTime.now()

        return todoRepository.getTodos().mapIndexed { index, todo ->
            DashboardTodo(
                id = todo.id,
                title = todo.title,
                completed = todo.completed,
                dateTime = now.plusSeconds(index.toLong()).format(formatter),
            )
        }
    }
}

