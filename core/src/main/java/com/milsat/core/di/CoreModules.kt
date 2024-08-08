package com.milsat.core.di

import androidx.room.Room.databaseBuilder
import com.milsat.core.data.db.CapstoneDatabase
import com.milsat.core.data.repository.FormRepositoryImpl
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.domain.usecase.CreateNewFormUseCase
import com.milsat.core.domain.usecase.FetchFormEntityUseCase
import com.milsat.core.domain.usecase.FetchFormFieldsUseCase
import com.milsat.core.domain.usecase.GetAllFormUseCase
import com.milsat.core.domain.usecase.SubmitFormUseCase
import com.milsat.core.utils.JsonFileSelector
import com.milsat.core.utils.ConfigurationPasser
import com.milsat.core.utils.CsvUtils
import com.milsat.core.utils.FormPageGenerator
import com.milsat.core.utils.GoogleSheetsUtils
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
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
    factoryOf(::FormPageGenerator)
    singleOf(::CsvUtils)
    singleOf(::GoogleSheetsUtils)

    // Repositories
    single<FormRepository> { FormRepositoryImpl(get(), get()) }

    // UseCases
    factoryOf(::CreateNewFormUseCase)
    factoryOf(::GetAllFormUseCase)
    factoryOf(::FetchFormFieldsUseCase)
    factoryOf(::SubmitFormUseCase)
    factoryOf(::FetchFormEntityUseCase)
}