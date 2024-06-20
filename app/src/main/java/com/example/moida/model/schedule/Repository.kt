package com.example.moida.model.schedule

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class Repository(private val table: DatabaseReference) {
    fun addPendingSchedule(scheduleData: ScheduleData) {
        table.child(scheduleData.scheduleId.toString()).setValue(scheduleData)
            .addOnCompleteListener { }
    }

//    fun updateScheduleName(scheduleData: ScheduleData) {
//        table.child(scheduleData.scheduleId.toString())
//            .child(scheduleData.scheduleName).setValue(scheduleData.scheduleName)
//    }
//
//    fun updateScheduleDate(scheduleData: ScheduleData) {
//        table.child(scheduleData.scheduleId.toString())
//            .child(scheduleData.scheduleStartDate).setValue(scheduleData.scheduleStartDate)
//    }
//
//    fun updateScheduleTime(scheduleData: ScheduleData) {
//        table.child(scheduleData.scheduleId.toString())
//            .child(scheduleData.scheduleTime).setValue(scheduleData.scheduleTime)
//    }
//
////    fun updateMemberTime(scheduleData: ScheduleData) {
////        val id = scheduleData.memberTimes?.keys?.first()
////        table.child(scheduleData.scheduleId.toString())
////            .child("memberTimes")
////            .child(id.toString())
////            .setValue(scheduleData.memberTimes)
////    }
//
//    fun deleteSchedule(scheduleData: ScheduleData) {
//        table.child(scheduleData.scheduleId.toString()).removeValue()
//    }

    fun getAllSchedules(): Flow<List<ScheduleData>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList = mutableListOf<ScheduleData>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(ScheduleData::class.java)
                    item?.let {
                        itemList.add(it)
                    }
                }
                trySend(itemList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("chrin", "[Repository] onCancelled(1): DatabaseError")
            }
        }

        table.addValueEventListener(listener) //변경사항을 background에서 감지 중 변경 발생하면 onDataChange 불러짐
        awaitClose {
            table.removeEventListener(listener)
        }
    }

    fun getSchedule(scheduleId: String, callback: (ScheduleData?) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(ScheduleData::class.java)
                    item?.let {
                        if (it.scheduleId.toString() == scheduleId) {
                            Log.i("chrin2", "onDataChange: it $it")
                            callback(it)
                        }
                    }
                }
//                val gson = Gson()
//                val data = gson.toJson(snapshot.value) //gson->json 변환
//                var res = ""
//                for (i in 6..<data.length - 1) {
//                    res += data[i]
//                }
//                Log.i("chrin", "onDataChange: res = $res")
//                val dataMap: Map<String, String> = jacksonObjectMapper().readValue(res) //json->gson
//
//
//                var scheduleId = dataMap.getValue("scheduleId").toInt()
//                var scheduleStartDate = dataMap.getValue("scheduleStartDate")
//                var scheduleTime = dataMap.getValue("scheduleTime")
//                var scheduleName = dataMap.getValue("scheduleStartDate")
//                var category = dataMap.getValue("category")
//                Log.i("chrin", "onDataChange: dataMap $dataMap")
//                Log.i("chrin", "onDataChange: scheduleid $scheduleId")

//                val memTimeMap: Map<String, String> = mapper.readValue(dataMap.getValue("memberTimes"))
//                Log.i("chrin", "====== timemap : $memTimeMap")
//                val timeMap : Map<String, List<Int>> = mapper.readValue(memTimeMap.getValue("${memTimeMap.keys}"))
//                Log.i("chrin", "====== timemap : $timeMap")
////                var memberTimes = mapOf(
//                    "${memTimeMap.keys}" to mapOf(
//                        "time1" to timeMap.getValue("time1"),
//                        "time2" to List(24) { 0 },
//                        "time3" to List(24) { 0 },
//                        "time4" to List(24) { 0 },
//                        "time5" to List(24) { 0 },
//                        "time6" to List(24) { 0 },
//                        "time7" to List(24) { 0 }
//                    )
//                )
//
//                if (dataMap != null) {
//                    if (dataMap.getValue("scheduleId") == scheduleId) {
////                        val scheduleData = ScheduleData(
////                            scheduleId = dataMap.getValue("scheduleId"),
////                            scheduleStartDate = dataMap.getValue("scheduleStartDate"),
////                            scheduleTime = dataMap.getValue("scheduleTime"),
////                            scheduleName = dataMap.getValue("scheduleStartDate"),
////                            category = dataMap.getValue("category"),
////                            memberTimes = dataMap.getValue("memberTimes")
////                        )
//                        Log.i("chrin", "[getSchedule] : ${dataMap.getValue("memberTimes")}")
//                        //callback(scheduleData)
//                    } else {
//                        callback(null)
//                    }
//                } else {
//                    callback(null)
//                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("chrin", "[Repository] getSchedule onCancelled(2): DatabaseError")
            }
        }
        table.addValueEventListener(listener)
    }

//    fun getUserSchedule(
//        scheduleId: String,
//        userName: String,
//        callback: (Map<String, List<Int>>?) -> Unit
//    ) {
//        val listener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val data = snapshot.value as? Map<String, Any>
//                if (data != null) {
//                    val memberTimes = data["memberTimes"] as? Map<String, Map<String, List<Int>>>
//                    if (data["scheduleId"] == scheduleId) {
//                        if (memberTimes != null) {
//                            for (time in memberTimes) {
//                                if (userName == time.key) {
//                                    Log.i("chrin", "[getUserSchedule] : $time")
//                                    callback(time.value)
//                                    return
//                                }
//                            }
//
//                        }
//                    }
//                } else {
//                    callback(null)
//                }
////                for (itemSnapshot in snapshot.children) {
////                    val item = itemSnapshot.getValue(ScheduleData::class.java)
////                    item?.let {
////                        if (it.scheduleId.toString() == scheduleId) {
////                            for (time in it.memberTimes!!)
////                                if (userName == time.key) {
////                                    callback(time.value)
////                                    return
////                                }
////                        }
////                    }
////                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("chrin", "[Repository] userSchedule onCancelled(2): DatabaseError")
//            }
//        }
//
//        table.addValueEventListener(listener)
//    }
}