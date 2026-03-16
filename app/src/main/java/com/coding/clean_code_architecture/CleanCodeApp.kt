package com.coding.clean_code_architecture

import android.app.Application
import com.coding.clean_code_architecture.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CleanCodeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CleanCodeApp)
            modules(appModules)
        }
    }
}

