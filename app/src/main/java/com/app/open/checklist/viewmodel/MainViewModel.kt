package com.app.open.checklist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.open.checklist.data.TaskDataStore
import com.app.open.checklist.model.TaskItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStore: TaskDataStore) : ViewModel() {
    private val _uiStateList = MutableStateFlow<List<TaskItem>>(emptyList())
    val uiStateList: StateFlow<List<TaskItem>> = _uiStateList.asStateFlow()


    private val _newItemVisible = MutableStateFlow(false)
    val newItemVisible: StateFlow<Boolean> = _newItemVisible.asStateFlow()

    fun toggleNewItemVisibility() {
        _newItemVisible.update { currentValue ->
            !currentValue
        }
    }

    init {
        viewModelScope.launch {
            dataStore.tasksFlow.collect { savedTasks ->
                _uiStateList.value = savedTasks
            }
        }
    }


    // Add a new task
    fun addTask(taskName: String) {
        _uiStateList.update { currentList ->
            CoroutineScope(Dispatchers.IO).launch {
                dataStore.saveTasks(currentList + TaskItem(taskName = taskName))
            }
            currentList + TaskItem(taskName = taskName)

        }
    }

    // Update the task name at a specific index
    fun updateTaskName(index: Int, newTaskName: String) {
        _uiStateList.update { currentList ->
            currentList.mapIndexed { i, task ->
                if (i == index) task.copy(taskName = newTaskName) else task
            }
        }
    }

    // Toggle the completion status of a task at a specific index
    fun toggleTaskCompletion(index: Int) {
        _uiStateList.update { currentList ->
            currentList.mapIndexed { i, task ->
                if (i == index) task.copy(isTaskCompleted = !task.isTaskCompleted) else task
            }
        }
    }

    private val _uiState = MutableStateFlow(TaskItem())
    val uiState: StateFlow<TaskItem> = _uiState.asStateFlow()

    fun updateCheckStatusuiState() {
        _uiState.update { currentState ->
            currentState.copy(isTaskCompleted = !currentState.isTaskCompleted)
        }
    }

    private val _uiState2 = MutableStateFlow(TaskItem())
    val uiState2: StateFlow<TaskItem> = _uiState2.asStateFlow()

    fun updateCheckStatusuiState2() {
        _uiState2.update { currentState ->
            currentState.copy(isTaskCompleted = !currentState.isTaskCompleted)
        }
    }

    private val _uiState3 = MutableStateFlow(TaskItem())
    val uiState3: StateFlow<TaskItem> = _uiState3.asStateFlow()

    fun updateCheckStatusuiState3() {
        _uiState3.update { currentState ->
            currentState.copy(isTaskCompleted = !currentState.isTaskCompleted)
        }
    }

}