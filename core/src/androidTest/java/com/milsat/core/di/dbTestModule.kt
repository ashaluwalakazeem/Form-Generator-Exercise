package com.milsat.core.di

import androidx.room.Room.inMemoryDatabaseBuilder
import com.milsat.core.data.db.CapstoneDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbTestModule = module {

    single<CapstoneDatabase> {
        inMemoryDatabaseBuilder(androidContext(), CapstoneDatabase::class.java)
            .allowMainThreadQueries()
            .setQueryExecutor { command -> command.run() }
            .build()
    }
    single { get<CapstoneDatabase>().capstoneDao() }
}
