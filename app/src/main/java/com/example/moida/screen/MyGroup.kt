import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.moida.R
import com.example.moida.model.Meeting
import com.example.moida.screen.JoinMeetingScreen
import com.example.moida.screen.MyMeetingsScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MyGroup(
    navController: NavHostController
) {
    val database = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var showJoinDialog by remember { mutableStateOf(false) }
    var showJoinError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var meetings by remember { mutableStateOf(listOf<Meeting>()) }
    var isFabMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (currentUser != null) {
            val userEmail = currentUser.email
            val meetingsListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val meetingList = mutableListOf<Meeting>()
                    for (meetingSnapshot in dataSnapshot.children) {
                        val meeting = meetingSnapshot.getValue(Meeting::class.java)
                        if (meeting != null && meeting.members.any { it["memberEmail"] == userEmail }) {
                            meetingList.add(meeting)
                        }
                    }
                    meetings = meetingList
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("MyGroup", "Database error: ${databaseError.message}")
                }
            }
            database.child("groups").addValueEventListener(meetingsListener)
        }
    }

    MyMeetingsScreen(
        navController = navController,
        meetings = meetings,
        isFabMenuExpanded = isFabMenuExpanded,
        onFabMenuToggle = { isFabMenuExpanded = !isFabMenuExpanded },
        onJoinMeeting = {
            showJoinDialog = true
            showJoinError = false
        }
    )

    if (showJoinDialog || showJoinError) {
        JoinMeetingScreen(
            onDismiss = {
                showJoinDialog = false
                showJoinError = false
            },
            onJoin = { inviteCode ->
                joinMeeting(inviteCode,
                    onSuccess = { updatedMeeting ->
                        meetings = meetings + updatedMeeting
                        showJoinDialog = false
                        showJoinError = false
                    },
                    onFailure = { exception ->
                        Log.e("MyGroup", "Error joining meeting", exception)
                        errorMessage = "유효하지 않은 초대코드입니다."
                        showJoinError = true
                    }
                )
            },
            errorMessage = errorMessage,
            showError = showJoinError
        )
    }
}

fun joinMeeting(inviteCode: String, onSuccess: (Meeting) -> Unit, onFailure: (Exception) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val realtimeDb = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    if (user != null) {
        db.collection("users").document(user.uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val memberName = document.getString("name") ?: "Unknown"
                    val memberEmail = user.email ?: "Unknown"
                    val memberData = mapOf(
                        "memberName" to memberName,
                        "memberEmail" to memberEmail
                    )

                    db.collection("groups").whereEqualTo("code", inviteCode).limit(1)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot != null && !snapshot.isEmpty) {
                                val meetingDocument = snapshot.documents.first()
                                val meetingId = meetingDocument.id
                                val meeting = meetingDocument.toObject(Meeting::class.java)

                                if (meeting != null) {
                                    // Check if the user is already a member in Firestore
                                    val isMemberInFirestore = meeting.members.any { it["memberEmail"] == memberEmail }

                                    if (!isMemberInFirestore) {
                                        val updatedMembers = meeting.members + memberData

                                        // Update Firestore
                                        db.collection("groups").document(meetingId).update("members", updatedMembers)
                                            .addOnSuccessListener {
                                                // Check if the user is already a member in Realtime Database
                                                realtimeDb.child("groups").child(meetingId).child("members").get()
                                                    .addOnSuccessListener { dataSnapshot ->
                                                        val members = dataSnapshot.children.mapNotNull { it.getValue(Map::class.java) as? Map<String, String> }
                                                        val isMemberInRealtimeDb = members.any { it["memberEmail"] == memberEmail }

                                                        if (!isMemberInRealtimeDb) {
                                                            val updatedMembersInRealtimeDb = members + memberData

                                                            // Update Realtime Database
                                                            realtimeDb.child("groups").child(meetingId).child("members").setValue(updatedMembersInRealtimeDb)
                                                                .addOnSuccessListener {
                                                                    val updatedMeeting = meeting.copy(members = updatedMembers)
                                                                    onSuccess(updatedMeeting)
                                                                }
                                                                .addOnFailureListener { e ->
                                                                    onFailure(e)
                                                                }
                                                        } else {
                                                            val updatedMeeting = meeting.copy(members = updatedMembers)
                                                            onSuccess(updatedMeeting)
                                                        }
                                                    }
                                                    .addOnFailureListener { e ->
                                                        onFailure(e)
                                                    }
                                            }
                                            .addOnFailureListener { e ->
                                                onFailure(e)
                                            }
                                    } else {
                                        onFailure(Exception("User is already a member of this meeting"))
                                    }
                                } else {
                                    onFailure(Exception("Meeting not found"))
                                }
                            } else {
                                onFailure(Exception("Meeting not found"))
                            }
                        }
                        .addOnFailureListener { e ->
                            onFailure(e)
                        }
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    } else {
        onFailure(Exception("User not authenticated"))
    }
}
fun generateUniqueCode(): String {
    return (100000..999999).random().toString()
}

fun getRandomImageRes(): Int {
    val images = listOf(
        R.drawable.sample_image1,
        R.drawable.sample_image2,
        R.drawable.sample_image3,
        R.drawable.sample_image4,
        R.drawable.sample_image5,
        R.drawable.sample_image6,
        R.drawable.sample_image7,
        R.drawable.sample_image8,
        R.drawable.sample_image9,
        R.drawable.sample_image10
    )
    return images.random()
}

