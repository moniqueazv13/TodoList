package br.com.todolist.data.local.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.todolist.data.local.dao.TaskDao
import br.com.todolist.data.local.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}