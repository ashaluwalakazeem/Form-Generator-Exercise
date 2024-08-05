package com.milsat.core.di

import com.milsat.core.presentation.JsonFileSelector
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreModules = module {

    factoryOf(::JsonFileSelector)
}