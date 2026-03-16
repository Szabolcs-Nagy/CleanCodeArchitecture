package com.coding.clean_code_architecture.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.coding.clean_code_architecture.presentation.dashboard.DashboardScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object AppRoute {
    const val Dashboard = "dashboard"
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
            DashboardScreen()
        }
    }
}

