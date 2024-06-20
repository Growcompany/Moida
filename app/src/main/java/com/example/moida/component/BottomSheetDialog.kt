package com.example.moida.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.moida.R
import com.example.moida.model.BottomNavItem
import com.example.moida.model.Routes
import com.example.moida.model.schedule.FixedScheduleData
import com.example.moida.model.schedule.FixedScheduleViewModel
import com.example.moida.ui.theme.Pretendard
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomSheet(
    navController: NavHostController,
) {
    var selectedDate by remember { mutableStateOf("") }
    val modalBottomSheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ModalBottomSheet(
            onDismissRequest = { },
            sheetState = modalBottomSheetState,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            containerColor = colorResource(id = R.color.white),
            dragHandle = { BottomSheetDefaults.DragHandle()},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 30.dp)
            ) {

                BottomSheetCalendar { selectedDate = it.toString() }

                Button(
                    onClick = {
                        //onDismiss()값 넘기기
                        selectedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                        selectedDate?.let { date->
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selectedDate", date)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (true) {
                            colorResource(id = R.color.main_blue)
                        } else colorResource(id = R.color.gray_200)
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "적용하기",
                        color = Color.White,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleBottomSheet(
    navController: NavHostController,
    fixedScheduleViewModel: FixedScheduleViewModel,
    scheduleId: Int,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ModalBottomSheet(
            onDismissRequest = { navController.popBackStack() },
            sheetState = modalBottomSheetState,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            containerColor = colorResource(id = R.color.white),
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 30.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 13.dp)
                        .clickable {
                            navController.navigate("${Routes.ScheduleEdit.route}?scheduleId=$scheduleId")
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "수정",
                        modifier = Modifier
                            .size(24.dp),
                        tint = colorResource(id = R.color.text_high)
                    )
                    Text(
                        text = "수정",
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.text_high),
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 13.dp)
                        .clickable {
                            val res = FixedScheduleData()
                            fixedScheduleViewModel.GetFixedSchedule(scheduleId){
                                res.scheduleId = scheduleId
                                res.scheduleName = it.scheduleName
                                res.scheduleDate = it.scheduleDate
                                res.scheduleTime = it.scheduleTime
                                res.category = it.category
                            }
                            fixedScheduleViewModel.DeleteFixedSchedule(res)
                            navController.navigate(BottomNavItem.Home.route)
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.trashbin),
                        contentDescription = "삭제",
                        modifier = Modifier
                            .size(24.dp),
                        tint = colorResource(id = R.color.error)
                    )
                    Text(
                        text = "삭제",
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.error),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}