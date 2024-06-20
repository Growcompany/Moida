package com.example.moida.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.moida.R
import com.example.moida.model.Meeting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinMeetingScreen(
    onDismiss: () -> Unit,
    onJoin: (String) -> Unit,
    errorMessage: String,
    showError: Boolean
) {
    var inviteCode by remember { mutableStateOf("") }
    val isJoinEnabled = inviteCode.isNotBlank()

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "전달받은 초대코드를 입력해주세요.", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                    TextField(
                        value = inviteCode,
                        onValueChange = {
                            inviteCode = it
                        },
                        label = { Text("초대코드 입력") },
                        singleLine = true,
                        isError = showError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = if (showError) Color.Red else colorResource(id = R.color.main_blue),
                            unfocusedIndicatorColor = if (showError) Color.Red else Color.Gray,
                            containerColor = Color.Transparent
                        )
                    )
                    if (showError) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id =R.drawable.ic_error),
                                contentDescription = "Warning",
                                tint = Color.Red,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Button(
                        onClick = {
                            onJoin(inviteCode)
                        },
                        enabled = isJoinEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isJoinEnabled) colorResource(id = R.color.main_blue) else Color.Gray,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("참여하기", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}