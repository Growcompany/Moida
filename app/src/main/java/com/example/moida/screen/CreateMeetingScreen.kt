package com.example.moida.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moida.R
import com.example.moida.model.GroupInfo
import com.example.moida.model.Meeting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import generateUniqueCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMeetingScreen(
    onDismiss: () -> Unit,
    onCreate: (GroupInfo) -> Unit
) {
    var groupName by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    val realtimeDb = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDismiss) {
                Icon(painter = painterResource(id = R.drawable.baseline_close_24), contentDescription = "Close")
            }
            Text(text = "모임 추가하기", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            TextButton(onClick = {
                if (user != null) {
                    db.collection("users").document(user.uid).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                val memberName = document.getString("name") ?: "Unknown"
                                val memberEmail = user.email ?: "Unknown"
                                val meeting = Meeting(
                                    name = groupName,
                                    imageRes = (0..9).random(),
                                    code = generateUniqueCode(),
                                    members = listOf(
                                        mapOf(
                                            "memberName" to memberName,
                                            "memberEmail" to memberEmail
                                        )
                                    )
                                )

                                // Firestore에 데이터 추가
                                db.collection("groups")
                                    .add(meeting)
                                    .addOnSuccessListener { documentReference ->
                                        val documentId = documentReference.id
                                        val meetingWithId = meeting.copy(id = documentId)
                                        db.collection("groups").document(documentId)
                                            .set(meetingWithId)
                                            .addOnSuccessListener {
                                                Log.d("CreateMeeting", "Meeting ID updated: $documentId")
                                                val groupInfo = GroupInfo(
                                                    groupId = documentId,
                                                    groupName = groupName,
                                                    groupImg = meeting.imageRes,
                                                    memberCount = meeting.members.size
                                                )
                                                onCreate(groupInfo)

                                                // Realtime Database에 데이터 추가
                                                realtimeDb.child("groups").child(documentId).setValue(meetingWithId)
                                                    .addOnSuccessListener {
                                                        Log.d("CreateMeeting", "Meeting saved to Realtime Database")
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.e("CreateMeeting", "Error saving meeting to Realtime Database", e)
                                                    }
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w("CreateMeeting", "Error updating meeting ID", e)
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("CreateMeeting", "Error adding meeting", e)
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("CreateMeetingScreen", "Error fetching user data", e)
                        }
                }
            }) {
                Text(text = "완료", color = Color.Blue)
            }
        }
        TextField(
            value = groupName,
            onValueChange = {
                if (it.length <= 20) groupName = it
            },
            label = { Text("모임 이름") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            )
        )
        Text(
            text = "${groupName.length}/20",
            modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}