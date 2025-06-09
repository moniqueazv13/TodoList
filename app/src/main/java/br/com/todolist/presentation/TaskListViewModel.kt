package br.com.todolist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.todolist.data.local.model.Task
import br.com.todolist.domain.AddTaskUseCase
import br.com.todolist.domain.DeleteTaskUseCase
import br.com.todolist.domain.GetAllTasksUseCase
import br.com.todolist.domain.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        getAllTasksUseCase()
            .onEach { taskList ->
                _tasks.value = taskList
            }
            .launchIn(viewModelScope)
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            if (title.isNotBlank()) {
                addTaskUseCase(Task(title = title))
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }
}