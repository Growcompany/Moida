package com.example.moida.model.schedule

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FixedScheduleRepo(private val table: DatabaseReference) { //확정된 약속
    fun addFixedSchedule(fixedScheduleData: FixedScheduleData) {
        table.child(fixedScheduleData.scheduleId.toString()).setValue(fixedScheduleData)
            .addOnCompleteListener { }
    }

    fun updateFixedSchedule(fixedScheduleData: FixedScheduleData) {
        table.child(fixedScheduleData.scheduleId.toString())
            .child("scheduleName").setValue(fixedScheduleData.scheduleName)
        table.child(fixedScheduleData.scheduleId.toString())
            .child("scheduleDate").setValue(fixedScheduleData.scheduleDate)
        table.child(fixedScheduleData.scheduleId.toString())
            .child("scheduleDate").setValue(fixedScheduleData.scheduleDate)
    }

    fun deleteFixedSchedule(fixedScheduleData: FixedScheduleData) {
        table.child(fixedScheduleData.scheduleId.toString()).removeValue()
    }

    fun getAllFixedSchedule(): Flow<List<FixedScheduleData>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList = mutableListOf<FixedScheduleData>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(FixedScheduleData::class.java)
                    item?.let {
                        itemList.add(it)
                    }
                }
                trySend(itemList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("chrin", "[FixedScheduleRepo] onCancelled(1): DatabaseError")
            }
        }

        table.addValueEventListener(listener) //변경사항을 background에서 감지 중 변경 발생하면 onDataChange 불러짐
        awaitClose {
            table.removeEventListener(listener)
        }
    }

    fun getFixedSchedule(scheduleId: String, callback: (FixedScheduleData) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(FixedScheduleData::class.java)
                    item?.let {
                        if (it.scheduleId.toString() == scheduleId) {
                            callback(it)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        table.addValueEventListener(listener)
    }
}