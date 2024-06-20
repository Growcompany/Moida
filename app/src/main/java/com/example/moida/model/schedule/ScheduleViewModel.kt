package com.example.moida.model.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moida.model.UpcomingItemData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Random

var index: Int = 0

class ScheduleViewModel(private val repository: Repository) : ViewModel() {
    private val _itemList = MutableStateFlow<List<ScheduleData>>(emptyList())
    val itemList: StateFlow<List<ScheduleData>> = _itemList
    var selectedItem = ScheduleData(0,"2020-01-01","","initName","")
    private val database = FirebaseDatabase.getInstance().reference

    init {
        viewModelScope.launch {
            fetchScheduleData()
        }
        index = itemList.value.size
    }

    private fun fetchScheduleData() {
        database.child("pendingSchedule").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val items = dataSnapshot.children.mapNotNull { snapshot ->
                        snapshot.getValue(ScheduleData::class.java)?.let {
                            convertToScheduleData(it)
                        }
                    }
                    _itemList.value = items
                    Log.d("ScheduleViewModel", "Fetched ${items.size} items")
                } else {
                    Log.d("ScheduleViewModel", "No items found in pendingSchedule collection")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ScheduleViewModel", "Error getting pendingSchedule data", error.toException())
            }
        })
    }

    private fun convertToScheduleData(scheduleData: ScheduleData): ScheduleData? {
        return if (scheduleData.scheduleStartDate.isNotEmpty()) {
            try {
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val startDate = LocalDate.parse(scheduleData.scheduleStartDate, dateFormatter)
                val endDate = startDate.plusDays(7).format(dateFormatter)
                scheduleData.copy(scheduleStartDate = endDate)
            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "Error parsing date: ${scheduleData.scheduleStartDate}", e)
                null
            }
        } else {
            Log.e("ScheduleViewModel", "Empty start date in scheduleData")
            null
        }
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

    fun AddScheduleData(scheduleData: ScheduleData) {
        viewModelScope.launch {
            repository.addPendingSchedule(scheduleData)
            GetAllSchedules()
        }
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