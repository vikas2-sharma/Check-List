package com.app.open.checklist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.open.checklist.data.TaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
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
        addTask("This is a task")
        addTask("This is a task 2")
        addTask("This is a task 3")
    }


    // Add a new task
    fun addTask(taskName: String) {
        _uiStateList.update { currentList ->
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