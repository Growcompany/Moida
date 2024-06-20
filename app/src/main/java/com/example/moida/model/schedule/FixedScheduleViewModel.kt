package com.example.moida.model.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Random

class FixedScheduleViewModel(private val repository: FixedScheduleRepo) : ViewModel() {
    private val _scheduleName = MutableStateFlow("")
    private val _scheduleDate = MutableStateFlow("")
    private val _scheduleTime = MutableStateFlow("")
    val scheduleName: StateFlow<String> get() = _scheduleName
    val scheduleDate: StateFlow<String> get() = _scheduleDate
    val scheduleTime: StateFlow<String> get() = _scheduleTime

    private val _itemList = MutableStateFlow<List<FixedScheduleData>>(emptyList())
    var selectedItem = FixedScheduleData(-1, "", "", "", "")
    private val database = Firebase.firestore

    init {
        viewModelScope.launch {
            database.collection("fixedSchedule")
                .get()
                .addOnSuccessListener { docs ->
                    if (!docs.isEmpty) {
                        val items = docs.map { doc ->
                            val item = doc.toObject(FixedScheduleData::class.java)
                            item
                        }
                        _itemList.value = items
                    } else {
                        Log.d("FixedScheduleViewModel", "No items found in FixedScheduleData collection")
                    }
                }
                .addOnFailureListener {
                    Log.e("FixedScheduleViewModel", "Error getting FixedScheduleData", it)
                }
        }
    }

    fun changeFSName(name: String) {
        _scheduleName.value = name
        selectedItem.scheduleName = name
    }
    fun changeFSDate(date: String) {
        viewModelScope.launch {
            _scheduleDate.value = date
            selectedItem.scheduleDate = date
        }
    }
    fun changeFSTime(time: String) {
        _scheduleTime.value = time
        selectedItem.scheduleTime = time
    }

    fun AddFixedSchedule(fixedScheduleData: FixedScheduleData) {
        viewModelScope.launch {
            repository.addFixedSchedule(fixedScheduleData)
            GetAllFixedSchedule()
        }
    }

    fun UpdateFixedSchedule(fixedScheduleData: FixedScheduleData) {
        viewModelScope.launch {
            repository.updateFixedSchedule(fixedScheduleData)
        }
    }

    fun DeleteFixedSchedule(fixedScheduleData: FixedScheduleData) {
        viewModelScope.launch {
            repository.deleteFixedSchedule(fixedScheduleData)
        }
    }

    fun GetAllFixedSchedule() {
        viewModelScope.launch {
            repository.getAllFixedSchedule().collect {
                _itemList.value = it
            }
        }
    }

    fun GetFixedSchedule(scheduleId: Int, callback: (FixedScheduleData) -> Unit) {
        viewModelScope.launch {
            repository.getFixedSchedule(scheduleId.toString()) {
                selectedItem = it
                _scheduleName.value = it.scheduleName
                _scheduleDate.value = it.scheduleDate
                _scheduleTime.value = it.scheduleTime
                callback(it)
            }
        }
    }
}

class FixedScheduleViewModelFactory(private val repository: FixedScheduleRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FixedScheduleViewModel::class.java)) {
            return FixedScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}