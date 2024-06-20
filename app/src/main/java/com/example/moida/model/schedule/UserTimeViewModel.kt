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

class UserTimeViewModel(private val repository: UserTimeRepo) : ViewModel() {
    private val _itemList = MutableStateFlow<List<UserTime>>(emptyList())
    //val itemList: StateFlow<List<UserTime>> = _itemList
    private var _itemList2 = MutableStateFlow<List<UserTime2>>(emptyList())
//    val itemList2: StateFlow<List<UserTime2>> = _itemList2
    var selectedItem = UserTime("-1", "", "", "", "", "", "", "", "")
    private val database = Firebase.firestore

    init {
        viewModelScope.launch {
            database.collection("UserTime")
                .get()
                .addOnSuccessListener { docs ->
                    if (!docs.isEmpty) {
                        val items = docs.map { doc ->
                            val item = doc.toObject(UserTime::class.java)
                            item
                        }
                        _itemList.value = items
                    } else {
                        Log.d("UserTimeViewModel", "No items found in UserTime collection")
                    }
                }
                .addOnFailureListener {
                    Log.e("UserTimeViewModel", "Error getting UserTime", it)
                }
        }
    }

    fun AddUserTime(scheduleId: Int, userName: String) {
        val userTime = UserTime(
            scheduleId = scheduleId.toString(),
            userName = userName,
            time1 = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
            time2 = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
            time3 = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
            time4 = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
            time5 = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
            time6 = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
            time7 = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0"
        )
        viewModelScope.launch {
            repository.addPendingUserTime(userTime)
            GetAllUserTime(scheduleId)
        }
    }

    fun UpdateUserTime(userTime: UserTime) {
        viewModelScope.launch {
            repository.updateUserTime(userTime)
        }
    }

    fun GetAllUserTime(scheduleId: Int): StateFlow<List<UserTime2>> {
        viewModelScope.launch {
            repository.getAllUserTime(scheduleId.toString(), callback = {_itemList2.value = it})
        }
//        Log.i("chrin", "[UserTimeViewModel] GetUserTime itmeList2: ${_itemList2.value}")
        return _itemList2
    }

    fun GetUserTime(scheduleId: Int, userName: String, callback: (UserTime) -> Unit) {
        viewModelScope.launch {
            repository.getUserTime(scheduleId.toString(), userName) {
                if (it != null) {
                    selectedItem = it
                    callback(it)
                    return@getUserTime
                }
            }
        }
    }
}

class UserTimeViewModelFactory(private val repository: UserTimeRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserTimeViewModel::class.java)) {
            return UserTimeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}