package com.example.moida.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.moida.R
import com.example.moida.component.DateField
import com.example.moida.component.NameTextField
import com.example.moida.component.Title
import com.example.moida.model.BottomNavItem
import com.example.moida.model.Routes
import com.example.moida.model.schedule.GroupScheduleViewModel
import com.example.moida.model.schedule.ScheduleViewModel
import com.example.moida.model.schedule.UserTimeViewModel
import com.example.moida.ui.theme.Pretendard

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CreateGroupSchedule(
    navController: NavHostController,
    groupScheduleViewModel: GroupScheduleViewModel,
    scheduleViewModel: ScheduleViewModel,
    userTimeViewModel: UserTimeViewModel,
    groupId: String,
) {
    val scheduleName by groupScheduleViewModel.scheduleName.collectAsState()
    val scheduleDate by groupScheduleViewModel.scheduleDate.collectAsState()

    val context = LocalContext.current
    val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory(context))
    val userName = signInViewModel.userName.value

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var activate by remember { mutableStateOf(false) }

        Title(
            navController = navController,
            route = BottomNavItem.Home.route,
            title = "일정 추가",
            rightBtn = "추가",
            rightColor = R.color.white
        )

        Column(
            modifier = Modifier
                .padding(start = 24.dp, top = 40.dp, end = 24.dp)
        ) {
            NameTextField(
                title = "일정 이름", name = scheduleName, onValueChange = {
                    groupScheduleViewModel.changeGSName(it)
                },
                "일정 이름 입력"
            )

            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            DateField(navController, title = "일정 기간 - 시작일", date = scheduleDate,
                onValueChange = { groupScheduleViewModel.changeGSDate(it) })

            Row(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = "오류",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(top = 3.dp, end = 5.dp)
                )
                Text(
                    text = "일정 기간은 시작일로부터 7일 자동 설정됩니다.",
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(400),
                    color = colorResource(id = R.color.gray_800),
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            activate = scheduleName.isNotEmpty() && scheduleDate.isNotEmpty()

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                onClick = {
                    if (activate) {
                        val scheduleId = scheduleViewModel.AddSchedule(scheduleName, scheduleDate, groupId)
                        userTimeViewModel.AddUserTime(scheduleId, userName.toString())
                        navController.navigate("${Routes.TimeSheet.route}?scheduleId=$scheduleId")
                    } else {
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activate) colorResource(id = R.color.main_blue) else colorResource(
                        id = R.color.disabled
                    ),
                )
            ) {
                Text(
                    text = "만들기",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.white),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
