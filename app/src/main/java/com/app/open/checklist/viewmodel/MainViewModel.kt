package com.app.open.checklist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val isCheckedVM = MutableLiveData(false)
}