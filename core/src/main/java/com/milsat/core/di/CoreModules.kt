package com.milsat.core.di

import androidx.room.Room.databaseBuilder
import com.milsat.core.data.db.CapstoneDatabase
import com.milsat.core.data.repository.FormRepositoryImpl
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.utils.JsonFileSelector
import com.milsat.core.utils.ConfigurationPasser
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
    factoryOf(::ConfigurationPasser)


    // Repositories
    single<FormRepository> { FormRepositoryImpl(get(), get()) }
}