package com.example.moida.screen

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.firestore.FirebaseFirestore

class MeetingManager {
    private val db = FirebaseFirestore.getInstance()
    private val realtimeDb = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    // 모임 삭제
    fun deleteMeeting(groupId: String) {
        if (groupId.isNotEmpty()) {
            // Firestore에서 삭제
            db.collection("groups").document(groupId)
                .delete()
                .addOnSuccessListener {
                    // 삭제 성공 시 Realtime Database에서 삭제
                    realtimeDb.child("groups").child(groupId).removeValue()
                        .addOnSuccessListener {
                            Log.d("MeetingManager", "Meeting successfully deleted from Realtime Database!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("MeetingManager", "Error deleting meeting from Realtime Database", e)
                        }
                    Log.d("MeetingManager", "Meeting successfully deleted from Firestore!")
                }
                .addOnFailureListener { e ->
                    Log.w("MeetingManager", "Error deleting meeting from Firestore", e)
                }
        } else {
            Log.e("MeetingManager", "Invalid meeting ID")
        }
    }

    // 모임 탈퇴
    fun leaveMeeting(groupId: String) {
        val userEmail = auth.currentUser?.email

        if (userEmail != null && groupId.isNotEmpty()) {
            Log.d("MeetingManager", "Leaving meeting with groupId: $groupId and userEmail: $userEmail")
            // Firestore에서 업데이트
            db.collection("groups").document(groupId)
                .update("members", com.google.firebase.firestore.FieldValue.arrayRemove(mapOf("memberEmail" to userEmail)))
                .addOnSuccessListener {
                    Log.d("MeetingManager", "User successfully left the meeting in Firestore!")
                    // Realtime Database에서 업데이트
                    realtimeDb.child("groups").child(groupId).child("members").get()
                        .addOnSuccessListener { dataSnapshot ->
                            val typeIndicator = object : GenericTypeIndicator<List<Map<String, String>>>() {}
                            val members = dataSnapshot.getValue(typeIndicator) ?: emptyList()
                            val updatedMembers = members.filterNot { it["memberEmail"] == userEmail }
                            realtimeDb.child("groups").child(groupId).child("members").setValue(updatedMembers)
                                .addOnSuccessListener {
                                    Log.d("MeetingManager", "User successfully left the meeting in Realtime Database!")
                                }
                                .addOnFailureListener { e ->
                                    Log.w("MeetingManager", "Error updating members in Realtime Database", e)
                                }
                        }
                        .addOnFailureListener { e ->
                            Log.w("MeetingManager", "Error getting members in Realtime Database", e)
                        }
                }
                .addOnFailureListener { e ->
                    Log.w("MeetingManager", "Error leaving meeting in Firestore", e)
                }
        } else {
            Log.e("MeetingManager", "User not authenticated or invalid meeting ID")
        }
    }
}