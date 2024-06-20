package com.example.moida.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moida.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class GroupDetailViewModel(meeting: Meeting) : ViewModel() {
    private val _groupInfo = MutableStateFlow<GroupInfo?>(null)
    val groupInfo: StateFlow<GroupInfo?> = _groupInfo

    private val _itemList = MutableStateFlow<List<GroupItemData>>(emptyList())
    val itemList: StateFlow<List<GroupItemData>> = _itemList

    init {
        viewModelScope.launch {
            _itemList.value = listOf(
                GroupItemData("1차 스터디", "2024.05.31", "12:00"),
                GroupItemData("2차 스터디", "2024.06.07", "13:00"),
                GroupItemData("첫 회식", "2024.06.08", "18:00")
            )
            _groupInfo.value = GroupInfo(meeting.id, meeting.name, meeting.imageRes, meeting.members.size)
        }
    }
}

class GroupDetailViewModelFactory(private val meeting: Meeting) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupDetailViewModel::class.java)) {
            return GroupDetailViewModel(meeting) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class GroupUpcomingViewModel() : ViewModel() {
    private val _itemList = MutableStateFlow<List<UpcomingItemData>>(emptyList())
    val itemList: StateFlow<List<UpcomingItemData>> = _itemList
    private val database = FirebaseFirestore.getInstance()
    init {
        viewModelScope.launch {
            database.collection("upcomingItems")
                .get()
                .addOnSuccessListener { docs ->
                    if(!docs.isEmpty) {
                        val items = docs.map {doc ->
                            val item = doc.toObject(UpcomingItemData::class.java)
                            Log.d("UpcomingViewModel", "Fetched item: $item")
                            item
                        }
                        _itemList.value = items
                        Log.d("UpcomingViewModel", "Fetched ${items.size} items")
                    } else {
                        Log.d("UpcomingViewModel", "No items found in upcomingItems collection")
                    }
                }
                .addOnFailureListener{
                    Log.e("UpcomingViewModel", "Error getting upcomingItems data", it)
                }
        }
    }
    fun getItemCount(): Int {
        return _itemList.value.size
    }
}