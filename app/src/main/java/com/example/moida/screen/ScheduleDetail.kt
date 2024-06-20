package com.example.moida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.moida.R
import com.example.moida.component.ParticipantsField
import com.example.moida.component.TextField
import com.example.moida.component.Title
import com.example.moida.model.BottomNavItem
import com.example.moida.model.Routes
import com.example.moida.model.schedule.FixedScheduleData
import com.example.moida.model.schedule.FixedScheduleViewModel
import com.example.moida.model.schedule.ScheduleData
import com.example.moida.ui.theme.Pretendard

@Composable
fun ScheduleDetail(
    navController: NavHostController,
    fixedScheduleViewModel: FixedScheduleViewModel,
    scheduleId: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var name by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var time by remember { mutableStateOf("") }

        var selectedItem by remember {
            mutableStateOf<FixedScheduleData?>(null)
        }

        LaunchedEffect(selectedItem) {
            initScheduleInfo(scheduleId, fixedScheduleViewModel) {
                name = it.scheduleName
                date = it.scheduleDate
                time = it.scheduleTime
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_left_shevron),
                contentDescription = "뒤로가기",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(BottomNavItem.Home.route) { inclusive = true }
                        }
                    }
            )
            Text(
                text = "상세 페이지",
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Image(
                painter = painterResource(id = R.drawable.ic_dots_vertical),
                contentDescription = "햄버거버튼",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.navigate("${Routes.ScheduleBottomSheet.route}?scheduleId=$scheduleId")
                    }
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 24.dp, top = 40.dp, end = 24.dp)
        ) {
            TextField(title = "일정 이름", value = name)
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            TextField(title = "일정 날짜", value = date)
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            TextField(title = "일정 시간", value = time)
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
//            ParticipantsField()
        }
    }
}

fun initScheduleInfo(
    scheduleId: Int,
    fixedScheduleViewModel: FixedScheduleViewModel,
    function: (FixedScheduleData) -> Unit
) {
    fixedScheduleViewModel.GetFixedSchedule(scheduleId) {
        function(it)
    }
}
