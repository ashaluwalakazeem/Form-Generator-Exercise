package com.milsat.capstone.di

import com.milsat.capstone.ui.screens.form.FormScreenViewModel
import com.milsat.capstone.ui.screens.home.HomeScreenViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {

    // ViewModels
    factoryOf(::HomeScreenViewModel)
    factoryOf(::FormScreenViewModel)
}