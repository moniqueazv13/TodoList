package br.com.todolist.presentation

import br.com.todolist.domain.AddTaskUseCase
import br.com.todolist.domain.DeleteTaskUseCase
import br.com.todolist.domain.GetAllTasksUseCase
import br.com.todolist.domain.UpdateTaskUseCase
import app.cash.turbine.test
import br.com.todolist.MainDispatcherRule
import br.com.todolist.data.local.model.Task
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TaskListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    private lateinit var viewModel: TaskListViewModel

    private val fakeTasks = listOf(
        Task(id = 1, title = "Test Task 1", isCompleted = false),
        Task(id = 2, title = "Test Task 2", isCompleted = true)
    )

    @Before
    fun setUp() {
        getAllTasksUseCase = mockk()
        addTaskUseCase = mockk(relaxUnitFun = true)
        updateTaskUseCase = mockk(relaxUnitFun = true)
        deleteTaskUseCase = mockk(relaxUnitFun = true)

        every { getAllTasksUseCase() } returns flowOf(fakeTasks)


        viewModel = TaskListViewModel(
            getAllTasksUseCase,
            addTaskUseCase,
            updateTaskUseCase,
            deleteTaskUseCase
        )
    }

    @Test
    fun `when viewmodel is initialized, it should load tasks`() = runTest {
        viewModel.tasks.test {
            assertEquals(fakeTasks, awaitItem())
        }

        verify(exactly = 1) { getAllTasksUseCase() }
    }

    @Test
    fun `when addTask is called with a valid title, it should call the use case`() = runTest {
        val newTitle = "estudar"

        viewModel.addTask(newTitle)

        coVerify(exactly = 1) { addTaskUseCase(Task(title = newTitle)) }
    }

    @Test
    fun `when addTask is called with a blank title, it should not call the use case`() = runTest {
        viewModel.addTask("")

        coVerify(exactly = 0) { addTaskUseCase(any()) }
    }

    @Test
    fun `when updateTask is called, it should call the use case`() = runTest {
        val taskToUpdate = fakeTasks.first().copy(isCompleted = true)

        viewModel.updateTask(taskToUpdate)

        coVerify(exactly = 1) { updateTaskUseCase(taskToUpdate) }
    }

    @Test
    fun `when deleteTask is called, it should call the use case`() = runTest {
        val taskToDelete = fakeTasks.first()

        viewModel.deleteTask(taskToDelete)

        coVerify(exactly = 1) { deleteTaskUseCase(taskToDelete) }
    }
}