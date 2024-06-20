package com.example.moida.model

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moida.screen.SignInViewModel
import com.example.moida.screen.SignInViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.moida.model.schedule.ScheduleData
import com.example.moida.model.schedule.ScheduleViewModel
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodayViewModel() : ViewModel() {
    private val _itemList = MutableStateFlow<List<TodayItemData>>(emptyList())
    val itemList: StateFlow<List<TodayItemData>> = _itemList
    private val database = Firebase.firestore

    init {
        fetchTodayItems()
    }

    private fun fetchTodayItems() {
        database.collection("todayItems")
            .get()
            .addOnSuccessListener {docs ->
                if(!docs.isEmpty) {
                    val items = docs.map {doc ->
                        val item = doc.toObject(TodayItemData::class.java)
                        Log.d("TodayViewModel", "Fetched item: $item")
                        item
                    }
                    _itemList.value = items
                    Log.d("TodayViewModel", "Fetched ${items.size} items")
                } else {
                    Log.d("TodayViewModel", "No items found in todayItems collection")
                }
            }
            .addOnFailureListener {
                Log.e("TodayViewModel", "Error getting todayItems data", it)
            }
    }
}

class UpcomingViewModel(
    private val scheduleViewModel: ScheduleViewModel
) : ViewModel() {
    private val _itemList = MutableStateFlow<List<UpcomingItemData>>(emptyList())
    val itemList: StateFlow<List<UpcomingItemData>> = _itemList
    private val database = FirebaseFirestore.getInstance()
    private val Realtimedatabase = FirebaseDatabase.getInstance().reference

    init {
        fetchUpcomingItems()
    }

    private fun fetchUpcomingItems() {
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

    fun fetchAndAddUserData(userName: String) {
        // userTime 컬렉션에서 사용자 이름을 검색하여 ID를 찾습니다.
        Realtimedatabase.child("userTime")
            .get()
            .addOnSuccessListener { dataSnapshot ->
                val userIds = mutableListOf<String>()
                for (snapshot in dataSnapshot.children) {
                    val userMap = snapshot.value as? Map<String, Any>
                    userMap?.values?.forEach { userInfo ->
                        val userInfoMap = userInfo as? Map<String, String>
                        if (userInfoMap?.get("userName") == userName) {
                            userIds.add(snapshot.key!!)
                        }
                    }
                }
                if (userIds.isNotEmpty()) {
                    Log.d("UpcomingViewModel", "User IDs: $userIds")
                    // ID를 사용하여 pendingSchedule 컬렉션에서 관련된 정보를 가져옵니다.
                    userIds.forEach { userId ->
                        fetchPendingSchedule(userId)
                    }
                } else {
                    Log.d("UpcomingViewModel", "No user found with name $userName")
                }
            }
            .addOnFailureListener {
                Log.e("UpcomingViewModel", "Error getting userTime data", it)
            }
    }

    private fun fetchPendingSchedule(userId: String) {
        Realtimedatabase.child("pendingSchedule").orderByKey().equalTo(userId)
            .get()
            .addOnSuccessListener { dataSnapshot ->
                val pendingItems = dataSnapshot.children.mapNotNull { snapshot ->
                    snapshot.getValue(ScheduleData::class.java)
                }

                pendingItems.forEach { scheduleData ->
                    scheduleData?.let {
                        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val startDate = LocalDate.parse(it.scheduleStartDate, dateFormatter)
                        val endDate = startDate.plusDays(7).format(dateFormatter)

                        // groups 컬렉션에서 그룹 이름 가져오기
                        database.collection("groups").document(it.category).get()
                            .addOnSuccessListener { documentSnapshot ->
                                val groupName = documentSnapshot.getString("name") ?: "Unknown Group"
                                val upcomingItem = UpcomingItemData(
                                    startDate = it.scheduleStartDate,
                                    endDate = endDate,
                                    name = it.scheduleName,
                                    category = groupName
                                )
                                _itemList.value = _itemList.value + upcomingItem
                                Log.d("UpcomingViewModel", "Added item: $upcomingItem")

                                // ScheduleViewModel에 데이터 추가
                                scheduleViewModel.AddScheduleData(it)
                            }
                            .addOnFailureListener { exception ->
                                Log.e("UpcomingViewModel", "Error getting group name", exception)
                            }
                    }
                }
            }
            .addOnFailureListener {
                Log.e("UpcomingViewModel", "Error getting pendingSchedule data", it)
            }
    }


    fun getItemCount(): Int {
        return _itemList.value.size
    }
}

class UpcomingViewModelFactory(
    private val scheduleViewModel: ScheduleViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(scheduleViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
