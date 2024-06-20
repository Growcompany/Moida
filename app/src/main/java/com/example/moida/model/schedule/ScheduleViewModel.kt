package com.example.moida.model.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Random

var index: Int = 0

class ScheduleViewModel(private val repository: Repository) : ViewModel() {
    private val _itemList = MutableStateFlow<List<ScheduleData>>(emptyList())
    val itemList: StateFlow<List<ScheduleData>> = _itemList
    var selectedItem = ScheduleData(0,"2020-01-01","","initName","")
    private val database = Firebase.firestore

    init {
        viewModelScope.launch {
            database.collection("scheduleData")
                .get()
                .addOnSuccessListener { docs ->
                    if (!docs.isEmpty) {
                        val items = docs.map { doc ->
                            val item = doc.toObject(ScheduleData::class.java)
                            item
                        }
                        _itemList.value = items
                    } else {
                        Log.d("ScheduleViewModel", "No items found in scheduleDate collection")
                    }
                }
                .addOnFailureListener {
                    Log.e("ScheduleViewModel", "Error getting scheduleDate", it)
                }
        }
        index = itemList.value.size
    }

    fun AddSchedule(name: String, date: String, groupId: String): Int {
        index = Random().nextInt(9000) + 1000
        var scheduleData = ScheduleData(
            scheduleId = index,
            scheduleName = name,
            scheduleStartDate = date,
            scheduleTime = "",
            category = groupId,
        )
        viewModelScope.launch {
            repository.addPendingSchedule(scheduleData)
            GetAllSchedules()
        }
        return index
    }

//    fun UpdateScheduleName(scheduleData: ScheduleData) {
//        viewModelScope.launch {
//            repository.updateScheduleName(scheduleData)
//        }
//    }
//
//    fun UpdateScheduleDate(scheduleData: ScheduleData) {
//        viewModelScope.launch {
//            repository.updateScheduleDate(scheduleData)
//        }
//    }
//
//    fun UpdateScheduleTime(scheduleData: ScheduleData) { //확정된 시간
//        viewModelScope.launch {
//            repository.updateScheduleTime(scheduleData)
//        }
//    }
//
//    fun DeleteSchedule(scheduleData: ScheduleData) {
//        viewModelScope.launch {
//            repository.deleteSchedule(scheduleData)
//        }
//    }

    fun GetAllSchedules() {
        viewModelScope.launch {
            repository.getAllSchedules().collect {
                _itemList.value = it
            }
        }
    }

    fun GetSchedule(scheduleId: Int, callback: (ScheduleData) -> Unit) {
        viewModelScope.launch {
            repository.getSchedule(scheduleId.toString()) {
                if (it != null) {
                    selectedItem = it
                    callback(it)
                }
            }
        }
    }
}

class ScheduleViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}