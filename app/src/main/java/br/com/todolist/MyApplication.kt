package br.com.todolist

import android.app.Application
import br.com.todolist.di.databaseModule
import br.com.todolist.di.repositoryModule
import br.com.todolist.di.useCaseModule
import br.com.todolist.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                databaseModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}