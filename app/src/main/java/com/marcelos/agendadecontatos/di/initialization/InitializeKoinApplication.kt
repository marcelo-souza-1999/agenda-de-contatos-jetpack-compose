package com.marcelos.agendadecontatos.di.initialization

import android.app.Application
import com.marcelos.agendadecontatos.di.modules.DatabaseModule
import com.marcelos.agendadecontatos.di.modules.RepositoryModule
import com.marcelos.agendadecontatos.di.modules.UseCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module

class InitializeKoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@InitializeKoinApplication)
            modules(
                defaultModule,
                DatabaseModule().module,
                UseCaseModule().module,
                RepositoryModule().module
            )
        }
    }
}