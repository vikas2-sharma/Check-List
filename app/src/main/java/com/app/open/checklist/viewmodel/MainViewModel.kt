package com.app.open.checklist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.open.checklist.data.TaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TaskItem())
    val uiState : StateFlow<TaskItem> = _uiState.asStateFlow()

    fun updateCheckStatusuiState(){
        _uiState.update { currentState->
            currentState.copy(isTaskCompleted = !currentState.isTaskCompleted)
        }
    }

    private val _uiState2 = MutableStateFlow(TaskItem())
    val uiState2 : StateFlow<TaskItem> = _uiState2.asStateFlow()

    fun updateCheckStatusuiState2(){
        _uiState2.update { currentState->
            currentState.copy(isTaskCompleted = !currentState.isTaskCompleted)
        }
    }

    private val _uiState3 = MutableStateFlow(TaskItem())
    val uiState3 : StateFlow<TaskItem> = _uiState3.asStateFlow()

    fun updateCheckStatusuiState3(){
        _uiState3.update { currentState->
            currentState.copy(isTaskCompleted = !currentState.isTaskCompleted)
        }
    }

}