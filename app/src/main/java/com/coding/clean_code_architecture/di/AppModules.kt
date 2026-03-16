package com.coding.clean_code_architecture.di

import com.coding.clean_code_architecture.BuildConfig
import com.coding.clean_code_architecture.data.remote.TodoApiService
import com.coding.clean_code_architecture.data.repository.TodoRepositoryImpl
import com.coding.clean_code_architecture.domain.repository.TodoRepository
import com.coding.clean_code_architecture.domain.usecase.GetDashboardTodosUseCase
import com.coding.clean_code_architecture.presentation.dashboard.DashboardViewModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

val networkModule = module {
    single { Gson() }
    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
    single<TodoApiService> {
        get<Retrofit>().create(TodoApiService::class.java)
    }
}

val dataModule = module {
    single<TodoRepository> { TodoRepositoryImpl(get()) }
}

val domainModule = module {
    factory { GetDashboardTodosUseCase(get()) }
}

val presentationModule = module {
    viewModelOf(::DashboardViewModel)
}

val appModules = listOf(
    networkModule,
    dataModule,
    domainModule,
    presentationModule,
)
