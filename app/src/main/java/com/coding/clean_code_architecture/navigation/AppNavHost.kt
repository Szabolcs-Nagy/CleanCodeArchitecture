package com.coding.clean_code_architecture.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.coding.clean_code_architecture.presentation.dashboard.DashboardScreen
import com.coding.clean_code_architecture.domain.model.DashboardTodo
import com.coding.clean_code_architecture.presentation.details.DetailsScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object AppRoute {
    const val Dashboard = "dashboard"
    const val Details = "details"

    private const val TodoIdArg = "todoId"
    private const val TodoTitleArg = "todoTitle"
    private const val TodoCompletedArg = "todoCompleted"
    private const val TodoDateTimeArg = "todoDateTime"

    const val DetailsRoute = "$Details/{$TodoIdArg}/{$TodoTitleArg}/{$TodoCompletedArg}/{$TodoDateTimeArg}"

    fun buildDetailsRoute(todo: DashboardTodo): String {
        val encodedTitle = Uri.encode(todo.title)
        val encodedDateTime = Uri.encode(todo.dateTime)
        return "$Details/${todo.id}/$encodedTitle/${todo.completed}/$encodedDateTime"
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Dashboard,
        modifier = modifier,
    ) {
        composable(AppRoute.Dashboard) {
            DashboardScreen(
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

