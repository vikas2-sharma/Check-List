package com.app.open.checklist.data

import android.content.Context
import android.provider.ContactsContract.Data
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.app.open.checklist.model.TaskItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class TaskDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val TASKS_KEY = stringPreferencesKey("tasks_list")
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tasks")
    }

    suspend fun saveTasks(tasks: List<TaskItem>) {
        dataStore.edit { preferences ->
            val jsonTasks = Gson().toJson(tasks)
            preferences[TASKS_KEY] = jsonTasks
        }
    }

    val tasksFlow: Flow<List<TaskItem>> = dataStore.data
        .map { preferences ->
            preferences[TASKS_KEY]?.let {
                val listType = object : TypeToken<List<TaskItem>>() {}.type
                 Gson().fromJson(it, listType)
            } ?: emptyList()
        }
}
