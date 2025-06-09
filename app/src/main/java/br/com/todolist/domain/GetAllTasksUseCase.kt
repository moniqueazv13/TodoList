package br.com.todolist.domain

import br.com.todolist.data.local.model.Task
import br.com.todolist.data.local.repository.TaskRepository


class GetAllTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke() = repository.getAllTasks()
}


class AddTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.addTask(task)
}

class UpdateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.updateTask(task)
}

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.deleteTask(task)
}