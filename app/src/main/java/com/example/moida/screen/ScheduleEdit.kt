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
import androidx.compose.runtime.collectAsState
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
import com.example.moida.component.DateField
import com.example.moida.component.NameTextField
import com.example.moida.component.TimeField
import com.example.moida.component.Title
import com.example.moida.model.BottomNavItem
import com.example.moida.model.Routes
import com.example.moida.model.schedule.FixedScheduleData
import com.example.moida.model.schedule.FixedScheduleViewModel
import com.example.moida.ui.theme.Pretendard

@Composable
fun ScheduleEdit(
    navController: NavHostController,
    fixedScheduleViewModel: FixedScheduleViewModel,
    scheduleId: Int,
) {
    val name by fixedScheduleViewModel.scheduleName.collectAsState()
    val date by fixedScheduleViewModel.scheduleDate.collectAsState()
    val time by fixedScheduleViewModel.scheduleTime.collectAsState()
//    var name by remember { mutableStateOf("") }
//    var date by remember { mutableStateOf("") }
//    var time by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var selectedItem by remember {
            mutableStateOf<FixedScheduleData?>(null)
        }

        LaunchedEffect(selectedItem) {
            initScheduleInfo(scheduleId, fixedScheduleViewModel) {
                fixedScheduleViewModel.changeFSName(name)
                fixedScheduleViewModel.changeFSDate(date)
                fixedScheduleViewModel.changeFSTime(time)
                category = it.category
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
                        navController.navigate("${Routes.ScheduleDetail.route}?scheduleId=$scheduleId"){
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
            Text(
                text = "완료",
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                color =  colorResource(id = R.color.main_blue),
                fontSize = 18.sp,
                modifier = Modifier.clickable {
                    val res = FixedScheduleData(
                        scheduleId,
                        scheduleName = name,
                        scheduleDate = date,
                        scheduleTime = time,
                        category = category
                    )
                    fixedScheduleViewModel.UpdateFixedSchedule(res)
                    navController.navigate("${Routes.ScheduleDetail.route}?scheduleId=$scheduleId"){
                        popUpTo(BottomNavItem.Home.route) { inclusive = true }
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 24.dp, top = 40.dp, end = 24.dp)
        ) {
            NameTextField(title = "일정 이름", name = name, onValueChange = {
             }, "일정 이름 입력")
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            DateField(navController, title = "일정 날짜", date = date, onValueChange = {

            })
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            TimeField(navController, title = "일정 시간", time = time, onValueChange = {

            })
        }
    }
}