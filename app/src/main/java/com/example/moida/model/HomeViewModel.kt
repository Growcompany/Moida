package com.example.moida.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

class UpcomingViewModel() : ViewModel() {
    private val _itemList = MutableStateFlow<List<UpcomingItemData>>(emptyList())
    val itemList: StateFlow<List<UpcomingItemData>> = _itemList
    private val database = FirebaseFirestore.getInstance()
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

    fun getItemCount(): Int {
        return _itemList.value.size
    }
}