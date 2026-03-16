# Clean Code Architecture - Dashboard Todos

This project uses a simple clean architecture setup in one Android module with:

- **Presentation layer**: Compose UI + ViewModel
- **Domain layer**: models, repository contract, and use case
- **Data layer**: Retrofit API, DTO mapping, and repository implementation

## Stack

- Jetpack Compose
- Navigation Compose (`NavHost`)
- Koin DI
- Retrofit2 + OkHttp3 (logging interceptor)
- Gson converter
- Kotlin Coroutines

## Logging Behavior

- Debug builds: OkHttp logging interceptor uses `BODY`
- Release builds: OkHttp logging interceptor uses `NONE`

## Network Endpoint

The app requests todos from:

- `https://jsonplaceholder.typicode.com/todos`

## Feature Flow

1. `DashboardScreen` requests state from `DashboardViewModel`
2. `DashboardViewModel` calls `GetDashboardTodosUseCase`
3. Use case fetches data via `TodoRepository`
4. Data layer calls Retrofit `TodoApiService`
5. Use case adds date-time to each todo element before returning it to UI

## Folder Structure

- `app/src/main/java/com/coding/clean_code_architecture/presentation`
- `app/src/main/java/com/coding/clean_code_architecture/domain`
- `app/src/main/java/com/coding/clean_code_architecture/data`
- `app/src/main/java/com/coding/clean_code_architecture/data/mapper`
- `app/src/main/java/com/coding/clean_code_architecture/di`
- `app/src/main/java/com/coding/clean_code_architecture/navigation`

## Quick Run

```zsh
./gradlew :app:compileDebugKotlin :app:testDebugUnitTest
```

## Compose UI Instrumentation Test

```zsh
./gradlew :app:compileDebugAndroidTest
./gradlew :app:connectedDebugAndroidTest --tests "*DashboardScreenTest*"
```

## Optional Install and Launch

```zsh
./gradlew :app:installDebug
```

