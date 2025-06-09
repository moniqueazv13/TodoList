package br.com.todolist.di

import br.com.todolist.data.local.local.AppDatabase
import br.com.todolist.data.local.repository.TaskRepository
import br.com.todolist.data.local.repository.TaskRepositoryImpl
import br.com.todolist.domain.AddTaskUseCase
import br.com.todolist.domain.DeleteTaskUseCase
import br.com.todolist.domain.GetAllTasksUseCase
import br.com.todolist.domain.UpdateTaskUseCase
import android.content.Context
import androidx.room.Room
import br.com.todolist.presentation.TaskListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Módulos Koin para o ambiente de teste.
 */
fun createTestModules(context: Context) = listOf(
    module {
        single {
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        }
        single { get<AppDatabase>().taskDao() }
    },
    module {
        single<TaskRepository> { TaskRepositoryImpl(get()) }
    },
    module {
        factory { GetAllTasksUseCase(get()) }
        factory { AddTaskUseCase(get()) }
        factory { UpdateTaskUseCase(get()) }
        factory { DeleteTaskUseCase(get()) }
    },
    module {
        viewModel {
            TaskListViewModel(
                getAllTasksUseCase = get(),
                addTaskUseCase = get(),
                updateTaskUseCase = get(),
                deleteTaskUseCase = get()
            )
        }
    }
)