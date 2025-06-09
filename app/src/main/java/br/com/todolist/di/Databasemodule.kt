package br.com.todolist.di

import br.com.todolist.data.local.local.AppDatabase
import br.com.todolist.data.local.repository.TaskRepository
import br.com.todolist.data.local.repository.TaskRepositoryImpl
import br.com.todolist.domain.AddTaskUseCase
import br.com.todolist.domain.DeleteTaskUseCase
import br.com.todolist.domain.GetAllTasksUseCase
import br.com.todolist.domain.UpdateTaskUseCase
import br.com.todolist.presentation.TaskListViewModel
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "task_db"
        ).build()
    }
    single { get<AppDatabase>().taskDao() }
}

val repositoryModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { GetAllTasksUseCase(get()) }
    factory { AddTaskUseCase(get()) }
    factory { UpdateTaskUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }
}

val viewModelModule = module {
    viewModel {
        TaskListViewModel(
            getAllTasksUseCase = get(),
            addTaskUseCase = get(),
            updateTaskUseCase = get(),
            deleteTaskUseCase = get()
        )
    }
}