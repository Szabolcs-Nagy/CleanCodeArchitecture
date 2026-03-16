package com.coding.clean_code_architecture.di

import org.koin.dsl.module

class GreetingMessageProvider {
    fun greetingFor(name: String): String = "Hello $name!"
}

val appModule = module {
    single { GreetingMessageProvider() }
}

