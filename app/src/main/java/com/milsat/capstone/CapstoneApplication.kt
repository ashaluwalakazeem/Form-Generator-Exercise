package com.milsat.capstone

import android.app.Application
import com.milsat.capstone.di.appModule
import com.milsat.core.di.coreModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CapstoneApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CapstoneApplication)
            modules(listOf(coreModules, appModule))
        }
    }
}