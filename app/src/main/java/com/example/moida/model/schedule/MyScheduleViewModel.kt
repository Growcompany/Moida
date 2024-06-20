package com.example.moida.model.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyScheduleViewModel : ViewModel() {
    private val _scheduleName = MutableStateFlow("")
    private val _scheduleDate = MutableStateFlow("")
    private val _scheduleTime = MutableStateFlow("")
    val scheduleName: StateFlow<String> get() = _scheduleName
    val scheduleDate: StateFlow<String> get() = _scheduleDate
    val scheduleTime: StateFlow<String> get() = _scheduleTime

    fun changeSName(name: String) {
        _scheduleName.value = name
    }

    fun changeSDate(date: String) {
        viewModelScope.launch {
            _scheduleDate.value = date
        }
    }

    fun changeSTime(time: String) {
        _scheduleTime.value = time
    }
}