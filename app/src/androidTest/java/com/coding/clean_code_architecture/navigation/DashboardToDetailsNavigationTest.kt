package com.coding.clean_code_architecture.navigation

import android.net.Uri
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.coding.clean_code_architecture.R
import com.coding.clean_code_architecture.domain.model.DashboardTodo
import com.coding.clean_code_architecture.domain.model.Todo
import com.coding.clean_code_architecture.domain.repository.TodoRepository
import com.coding.clean_code_architecture.domain.usecase.GetDashboardTodosUseCase
import com.coding.clean_code_architecture.presentation.dashboard.DashboardScreen
import com.coding.clean_code_architecture.presentation.dashboard.DashboardViewModel
import com.coding.clean_code_architecture.presentation.details.DetailsScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardToDetailsNavigationTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun clickingDashboardTodo_navigatesToDetailsScreen_andBackNavigatesToDashboard() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val detailsTitle = context.getString(R.string.details_title)
        val backContentDescription = context.getString(R.string.details_navigate_back)

        val viewModel = DashboardViewModel(GetDashboardTodosUseCase(FakeTodoRepository()))

        composeRule.setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = AppRoute.Dashboard,
            ) {
                composable(AppRoute.Dashboard) {
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToDetails = { todo ->
                            navController.navigate(AppRoute.buildDetailsRoute(todo))
                        },
                    )
                }

                composable(
                    route = AppRoute.DetailsRoute,
                    arguments = listOf(
                        navArgument("todoId") { type = NavType.IntType },
                        navArgument("todoTitle") { type = NavType.StringType },
                        navArgument("todoCompleted") { type = NavType.BoolType },
                        navArgument("todoDateTime") { type = NavType.StringType },
                    ),
                ) { backStackEntry ->
                    val args = requireNotNull(backStackEntry.arguments)
                    val todo = DashboardTodo(
                        id = args.getInt("todoId"),
                        title = Uri.decode(args.getString("todoTitle").orEmpty()),
                        completed = args.getBoolean("todoCompleted"),
                        dateTime = Uri.decode(args.getString("todoDateTime").orEmpty()),
                    )

                    DetailsScreen(
                        todo = todo,
                        onNavigateBack = { navController.popBackStack() },
                    )
                }
            }
        }

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule
                .onAllNodesWithText("#1 delectus aut autem")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("#1 delectus aut autem").performClick()

        composeRule.onNodeWithText(detailsTitle).assertExists()
        composeRule.onNodeWithText("Id: 1").assertExists()
        composeRule.onNodeWithText("Title: delectus aut autem").assertExists()
        composeRule.onNodeWithContentDescription(backContentDescription).performClick()
        composeRule.onNodeWithText("Dashboard").assertExists()
    }

    private class FakeTodoRepository : TodoRepository {
        override suspend fun getTodos(): List<Todo> {
            return listOf(
                Todo(
                    userId = 1,
                    id = 1,
                    title = "delectus aut autem",
                    completed = false,
                ),
            )
        }
    }
}


