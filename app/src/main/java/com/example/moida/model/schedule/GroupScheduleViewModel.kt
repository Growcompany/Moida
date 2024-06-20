package com.example.moida.model.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GroupScheduleViewModel : ViewModel() {
    private val _scheduleName = MutableStateFlow("")
    private val _scheduleDate = MutableStateFlow("")
    val scheduleName: StateFlow<String> get() = _scheduleName
    val scheduleDate: StateFlow<String> get() = _scheduleDate


    fun changeGSName(name: String) {
        _scheduleName.value = name
    }

    fun changeGSDate(date: String) {
        viewModelScope.launch {
            _scheduleDate.value = date
        }
    }
}