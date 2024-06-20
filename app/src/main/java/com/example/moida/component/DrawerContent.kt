package com.example.moida.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moida.R
import com.example.moida.model.GroupInfo
import com.example.moida.model.Meeting
import com.example.moida.ui.theme.Pretendard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    group : GroupInfo,
    drawerState: DrawerState,
    scope: CoroutineScope,
    onDeleteMeeting: (String) -> Unit,
    onLeaveMeeting: (String) -> Unit
) {
    // `group` 객체를 로그로 확인합니다
            Log.d("DrawerContent", "DrawerContent called with group: ${group}")
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(270.dp)
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 20.dp)
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_x_close),
                contentDescription = "닫기",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                tint = colorResource(id = R.color.gray_600),

                )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 27.dp)
        ){
            Text(
                text = group.groupName,
                fontFamily = Pretendard,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 18.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "편집하기",
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .padding(start = 8.dp),
                tint = colorResource(id = R.color.text_medium)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 18.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.person_24),
                contentDescription = "사람 아이콘",
                modifier = Modifier
                    .width(16.dp)
                    .height(16.dp),
                tint = colorResource(id = R.color.text_medium)
            )
            Text(
                text = "${group.memberCount}명",
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = colorResource(id = R.color.text_medium),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.blue3)),
            ) {
                Box(
                    modifier = Modifier.padding(start = 18.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ticket),
                        contentDescription = "티켓 아이콘",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),
                        tint = colorResource(id = R.color.text_high)
                    )
                }

                Text(
                    text = "초대 코드 발급",
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.text_high),
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .padding(vertical = 13.dp)
                )
            }
        }

        Text(
            text = "약속",
            fontFamily = Pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = colorResource(id = R.color.main_blue),
            modifier = Modifier
                .padding(start = 17.dp, top = 15.dp)
                .padding(vertical = 15.dp)
        )
        Divider(
            color = colorResource(id = R.color.gray_100),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )
        Text(
            text = "대기 중인 일정",
            fontFamily = Pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_high),
            modifier = Modifier
                .padding(start = 20.dp)
                .padding(vertical = 13.dp)
        )
        Divider(
            color = colorResource(id = R.color.gray_100),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )
        Text(
            text = "일정",
            fontFamily = Pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_high),
            modifier = Modifier
                .padding(start = 20.dp)
                .padding(vertical = 13.dp)
        )
        Divider(
            color = colorResource(id = R.color.gray_100),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp)
        ) {
            Divider(
                color = colorResource(id = R.color.gray_100),
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 19.dp)
                    .align(Alignment.CenterHorizontally)

            ) {
                Box(
                    modifier = Modifier.clickable {
                        onDeleteMeeting(group.groupId)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 30.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.trashbin),
                            contentDescription = "삭제",
                            modifier = Modifier
                                .size(18.dp),
                            tint = colorResource(id = R.color.error)
                        )
                        Text(
                            text = "모임 삭제",
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.error),
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }
                Divider(
                    color = colorResource(id = R.color.gray_100),
                    modifier = Modifier
                        .width(1.dp)
                        .height(18.dp)
                )
                Box(
                    modifier = Modifier.clickable {
                        Log.d("DrawerContent", "onLeaveMeeting clicked for groupId: ${group.groupId}")
                        onLeaveMeeting(group.groupId)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.exit),
                            contentDescription = "나가기",
                            modifier = Modifier
                                .size(18.dp)
                        )
                        Text(
                            text = "모임 나가기",
                            fontFamily = Pretendard,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.text_high),
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }
            }
        }
    }
}