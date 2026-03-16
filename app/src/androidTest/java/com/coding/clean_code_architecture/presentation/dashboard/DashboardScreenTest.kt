package com.coding.clean_code_architecture.presentation.dashboard

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.coding.clean_code_architecture.domain.model.DashboardTodo
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test

class DashboardScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun dashboardContent_showsLoading_thenRendersTodos() {
        val loadedState = DashboardUiState(
            isLoading = false,
            todos = listOf(
                DashboardTodo(id = 1, title = "delectus aut autem", completed = false, dateTime = "2026-03-16 10:10:10"),
                DashboardTodo(id = 2, title = "quis ut nam facilis et officia qui", completed = true, dateTime = "2026-03-16 10:10:11"),
            ),
        )

        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            var state by remember { mutableStateOf(DashboardUiState(isLoading = true)) }

            LaunchedEffect(Unit) {
                delay(150)
                state = loadedState
            }

            DashboardContent(
                state = state,
                onRetry = {},
            )
        }

        composeRule.onNodeWithTag(DashboardTestTags.Loading).assertExists()

        composeRule.mainClock.advanceTimeBy(200)
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(DashboardTestTags.List).assertExists()
        composeRule.onNodeWithText("Dashboard").assertExists()
        composeRule.onNodeWithText("#1 delectus aut autem").assertExists()
        composeRule.onNodeWithText("DateTime: 2026-03-16 10:10:10").assertExists()
    }
}

