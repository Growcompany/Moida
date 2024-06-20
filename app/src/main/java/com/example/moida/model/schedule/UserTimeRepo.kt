package com.example.moida.model.schedule

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class UserTimeRepo(private val table: DatabaseReference) {
    fun addPendingUserTime(userTime: UserTime) {
        val userTime2 = UserTime2(
            userName = userTime.userName,
            time1 = userTime.time1,
            time2 = userTime.time2,
            time3 = userTime.time3,
            time4 = userTime.time4,
            time5 = userTime.time5,
            time6 = userTime.time6,
            time7 = userTime.time7,
        )
        table.child(userTime.scheduleId)
            .child(userTime.userName).setValue(userTime2)
            .addOnCompleteListener { }
    }

    fun updateUserTime(userTime: UserTime) {
        table.child(userTime.scheduleId)
            .child(userTime.userName)
            .child("time1").setValue(userTime.time1)
        table.child(userTime.scheduleId)
            .child(userTime.userName)
            .child("time2").setValue(userTime.time2)
        table.child(userTime.scheduleId)
            .child(userTime.userName)
            .child("time3").setValue(userTime.time3)
        table.child(userTime.scheduleId)
            .child(userTime.userName)
            .child("time4").setValue(userTime.time4)
        table.child(userTime.scheduleId)
            .child(userTime.userName)
            .child("time5").setValue(userTime.time5)
        table.child(userTime.scheduleId)
            .child(userTime.userName)
            .child("time6").setValue(userTime.time6)
        table.child(userTime.scheduleId)
            .child(userTime.userName)
            .child("time7").setValue(userTime.time7)
    }

    fun getUserTime(scheduleId: String, userName: String, callback: (UserTime?) -> Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    if (itemSnapshot.key == scheduleId) {
                        for (itemSnapshot2 in itemSnapshot.children) {
                            val item = itemSnapshot2.getValue(UserTime2::class.java)
                            item?.let {
                                if (it.userName == userName) {
                                    val res = UserTime(
                                        scheduleId = scheduleId,
                                        userName = it.userName,
                                        time1 = it.time1,
                                        time2 = it.time2,
                                        time3 = it.time3,
                                        time4 = it.time4,
                                        time5 = it.time5,
                                        time6 = it.time6,
                                        time7 = it.time7,
                                    )
                                    Log.i("chrin", "[userTimeRepo] onDataChage\nres = $res")
                                    callback(res)
                                    return
                                }
                            }
                        }
                    }
                }
                callback(null)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("chrin", "[UserTimeRepo] getSchedule onCancelled(1): DatabaseError")
            }
        }

        table.addValueEventListener(listener)
    }

    fun getAllUserTime(
        scheduleId: String,
        callback: (List<UserTime2>) -> Unit
    ) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                val itemList = mutableListOf<UserTime2>()
//                for (itemSnapshot in snapshot.children) {
//                    val item = itemSnapshot.getValue(UserTime2::class.java)
//                    item?.let {
//                        if (it.scheduleId == scheduleId)
//                            itemList.add(it)
//                    }
//                }
//                trySend(itemList)
                val itemList = mutableListOf<UserTime2>()
                for (itemSnapshot in snapshot.children) {
                    //Log.i("chrin", "onDataChange: \nfor문 안\nitemsnophot.key=${itemSnapshot.key}\nvalue=${itemSnapshot.value}")
                    if (itemSnapshot.key == scheduleId) {
                        for (itemSnapshot2 in itemSnapshot.children) {
                            val item = itemSnapshot2.getValue(UserTime2::class.java)
                            item?.let {
                                val res = UserTime2(
                                    userName = it.userName,
                                    time1 = it.time1,
                                    time2 = it.time2,
                                    time3 = it.time3,
                                    time4 = it.time4,
                                    time5 = it.time5,
                                    time6 = it.time6,
                                    time7 = it.time7,
                                )
                                itemList.add(res)
                            }
                        }
                        Log.i("chrin", "[UserTImeRepo]\nonDataChange:\nitemList = $itemList")
                        callback(itemList)
                        //trySend(itemList)
                    }
                }
                //trySend(itemList)
                return
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("chrin", "[UserTimeRepo] onCancelled(2): DatabaseError")
            }
        }

        table.addValueEventListener(listener) //변경사항을 background에서 감지 중 변경 발생하면 onDataChange 불러짐
//        awaitClose {
//            table.removeEventListener(listener)
//        }
    }
}