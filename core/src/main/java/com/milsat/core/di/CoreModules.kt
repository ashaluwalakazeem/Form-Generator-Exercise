package com.milsat.core.di

import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.milsat.core.data.db.CapstoneDatabase
import com.milsat.core.data.repository.FormRepositoryImpl
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.presentation.JsonFileSelector
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreModules = module {
    single<CapstoneDatabase> {
        databaseBuilder(
            androidApplication(),
            CapstoneDatabase::class.java,
            "capstone_database"
        ).build()
    }

    single { get<CapstoneDatabase>().capstoneDao() }
    factoryOf(::JsonFileSelector)


    // Repositories
    single<FormRepository> { FormRepositoryImpl(get()) }
}