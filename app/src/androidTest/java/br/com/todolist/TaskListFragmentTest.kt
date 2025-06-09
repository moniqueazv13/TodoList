package br.com.todolist

import br.com.todolist.data.local.local.AppDatabase
import br.com.todolist.presentation.MainActivity
import br.com.todolist.presentation.TaskAdapter
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.todolist.di.createTestModules
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class TaskListFragmentTest : KoinTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val database: AppDatabase by inject()

    @Before
    fun setUp() {
        stopKoin()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(createTestModules(ApplicationProvider.getApplicationContext()))
        }
        database.clearAllTables()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun addTask_displaysNewTaskInList() {
        val taskTitle = "estudar"

        onView(withId(R.id.fab_add_task)).perform(click())

        onView(withId(R.id.edit_text_task_title)).perform(typeText(taskTitle), closeSoftKeyboard())

        onView(withText("ADICIONAR")).perform(click())

        Thread.sleep(500)

        onView(withText(taskTitle)).check(matches(isDisplayed()))
    }

    @Test
    fun deleteTask_removesTaskFromList() {
        val taskTitle = "Levar o lixo para fora"

        onView(withId(R.id.fab_add_task)).perform(click())
        onView(withId(R.id.edit_text_task_title)).perform(typeText(taskTitle), closeSoftKeyboard())
        onView(withText("ADICIONAR")).perform(click())

        onView(withText(taskTitle)).check(matches(isDisplayed()))

        onView(withId(R.id.recycler_view_tasks))
            .perform(
                actionOnItemAtPosition<TaskAdapter.TaskViewHolder>(
                    0,
                    clickChildViewWithId(R.id.button_delete)
                )
            )

        Thread.sleep(500)

        onView(withText(taskTitle)).check(doesNotExist())

    }

    @Test
    fun completeTask_updatesTaskAppearance() {
        val taskTitle = "Estudar testes de UI"

        onView(withId(R.id.fab_add_task)).perform(click())
        onView(withId(R.id.edit_text_task_title)).perform(typeText(taskTitle), closeSoftKeyboard())
        onView(withText("ADICIONAR")).perform(click())

        onView(withId(R.id.recycler_view_tasks))
            .perform(
                actionOnItemAtPosition<TaskAdapter.TaskViewHolder>(
                    0,
                    clickChildViewWithId(R.id.checkbox_completed)
                )
            )

        Thread.sleep(500)

        onView(withId(R.id.checkbox_completed)).check(matches(isChecked()))
    }

    @Test
    fun changeNameTask_updatesTaskAppearance() {
        val taskTitle = "Estudar testes de UI"

        onView(withId(R.id.fab_add_task)).perform(click())
        onView(withId(R.id.edit_text_task_title)).perform(typeText(taskTitle), closeSoftKeyboard())
        onView(withText("ADICIONAR")).perform(click())
        onView(withId(R.id.recycler_view_tasks))
            .perform(
                actionOnItemAtPosition<TaskAdapter.TaskViewHolder>(
                    0,
                    clickChildViewWithId(R.id.item_task)
                )
            )
        Thread.sleep(500)

        onView(withId(R.id.edit_text_task_title)).perform(clearText(), typeText("Estudar testes de UI1"), closeSoftKeyboard())

        onView(withText("ALTERAR")).perform(click())

        onView(withText("Estudar testes de UI1")).check(matches(isDisplayed()))

    }
}

// Função auxiliar para clicar em uma view filha com um id específico
fun clickChildViewWithId(id: Int) = object : androidx.test.espresso.ViewAction {
    override fun getConstraints() = null
    override fun getDescription() = "Clique em uma view filha com o id especificado."
    override fun perform(
        uiController: androidx.test.espresso.UiController,
        view: android.view.View
    ) {
        val childView = view.findViewById<android.view.View>(id)
        childView.performClick()
    }
}