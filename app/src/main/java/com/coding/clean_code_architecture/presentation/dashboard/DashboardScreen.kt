package com.coding.clean_code_architecture.presentation.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.coding.clean_code_architecture.domain.model.DashboardTodo
import org.koin.androidx.compose.koinViewModel

object DashboardTestTags {
    const val Loading = "dashboard_loading"
    const val List = "dashboard_list"
    const val Error = "dashboard_error"
}

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
    onNavigateToDetails: (DashboardTodo) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is DashboardNavigationEvent.NavigateToDetails -> {
                    onNavigateToDetails(event.todo)
                }
            }
        }
    }

    DashboardContent(
        state = uiState,
        onRetry = viewModel::loadDashboard,
        onTodoClick = viewModel::onTodoClicked,
        modifier = modifier,
    )
}

@Composable
fun DashboardContent(
    state: DashboardUiState,
    onRetry: () -> Unit,
    onTodoClick: (DashboardTodo) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    when {
        state.isLoading -> {
            LoadingState(modifier = modifier)
        }

        state.errorMessage != null -> {
            ErrorState(
                message = state.errorMessage,
                onRetry = onRetry,
                modifier = modifier,
            )
        }

        else -> {
            TodoList(
                state = state,
                onTodoClick = onTodoClick,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(DashboardTestTags.Loading),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Text(text = "Loading dashboard...", modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
private fun ErrorState(
    message: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(DashboardTestTags.Error),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = message ?: "Unexpected error")
        Button(onClick = onRetry, modifier = Modifier.padding(top = 12.dp)) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun TodoList(
    state: DashboardUiState,
    onTodoClick: (DashboardTodo) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag(DashboardTestTags.List),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        }
        items(state.todos, key = { it.id }) { todo ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTodoClick(todo) },
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "#${todo.id} ${todo.title}")
                    Text(text = "Completed: ${todo.completed}")
                    Text(text = "DateTime: ${todo.dateTime}")
                }
            }
        }
    }
}

