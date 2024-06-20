package com.example.moida.screen

import android.annotation.SuppressLint
import android.text.TextUtils.split
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.moida.component.HeadOfTime
import com.example.moida.component.ShowTimeLine
import com.example.moida.component.TimeBlockInput
import com.example.moida.component.TitleWithXBtn
import com.example.moida.model.Routes
import com.example.moida.model.schedule.ScheduleData
import com.example.moida.model.schedule.ScheduleViewModel
import com.example.moida.model.schedule.UserTime
import com.example.moida.model.schedule.UserTimeViewModel
import com.example.moida.ui.theme.Pretendard
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TimeInput(
    navController: NavHostController,
    scheduleViewModel: ScheduleViewModel,
    userTimeViewModel: UserTimeViewModel,
    scheduleId: Int
) {
    var startDate by remember { mutableStateOf(scheduleViewModel.selectedItem.scheduleStartDate) }
    var startDay by remember { mutableStateOf(DayOfWeek.MONDAY) }
    var page by remember { mutableIntStateOf(1) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val context = LocalContext.current
    val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory(context))
    val userName = signInViewModel.userName.value

    var time1 by remember { mutableStateOf(List(24) { 0 }) }
    var time2 by remember { mutableStateOf(List(24) { 0 }) }
    var time3 by remember { mutableStateOf(List(24) { 0 }) }
    var time4 by remember { mutableStateOf(List(24) { 0 }) }
    var time5 by remember { mutableStateOf(List(24) { 0 }) }
    var time6 by remember { mutableStateOf(List(24) { 0 }) }
    var time7 by remember { mutableStateOf(List(24) { 0 }) }

    var selectedItem by remember {
        mutableStateOf<ScheduleData?>(null)
    }

    LaunchedEffect(selectedItem) {
        initInfo(scheduleId, scheduleViewModel) {
            startDate = it.scheduleStartDate
            startDay = getDayofWeek(startDate)
        }
        if (userName != null) {
            initUserTimeInfo(scheduleId, userName, userTimeViewModel) { userTime ->
                time1 = userTime.time1.split(",").map { it.toInt() }
                time2 = userTime.time2.split(",").map { it.toInt() }
                time3 = userTime.time3.split(",").map { it.toInt() }
                time4 = userTime.time4.split(",").map { it.toInt() }
                time5 = userTime.time5.split(",").map { it.toInt() }
                time6 = userTime.time6.split(",").map { it.toInt() }
                time7 = userTime.time7.split(",").map { it.toInt() }
            }
        }
    }

    var isBtnClicked by remember { mutableStateOf(false) }

    Column {
        //제목부분
        TitleWithXBtn(
            navController = navController,
            route = Routes.TimeSheet.route,
            title = "내 시간 입력하기",
            rightBtn = false
        )
        //날짜부분
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 44.dp)
        ) {
            if (page == 1) {
                Box(modifier = Modifier.size(16.dp))
            } else {
                Image(
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = "왼쪽버튼",
                    modifier = Modifier
                        .size(16.dp)
                        .padding(end = 4.dp)
                        .clickable {
                            page -= 1
                            val date = LocalDate.parse(startDate, formatter)
                            val newDate = date.minusDays(3)
                            startDate = newDate.format(formatter)
                            startDay = getDayofWeek(startDate)
                        }
                )
            }
            HeadOfTime(startDate = startDate, startDay = startDay, page = page)
            if (page == 3) {
                Box(modifier = Modifier.size(16.dp))
            } else {
                Image(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = "오른쪽버튼",
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 4.dp)
                        .clickable {
                            page += 1
                            val date = LocalDate.parse(startDate, formatter)
                            val newDate = date.plusDays(3)
                            startDate = newDate.format(formatter)
                            startDay = getDayofWeek(startDate)
                        }
                )
            }
        }

        //시간테이블 부분
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            //시간대
            ShowTimeLine()
            //시간화면보여줌
            Box(
                modifier = Modifier
                    .width(250.dp)
                    .padding(top = 13.dp, start = 10.dp)
            ) {
                if (page == 1) {
                    Row {
                        TimeBlockInput(isBtnClicked, time1.toIntArray()) {
                            for (i in 0..23) {
                                if (it[i] && !isBtnClicked) {
                                    time1 = time1.toMutableList().apply {
                                        this[i] = 1
                                    }
                                } else {
                                    time1 = time1.toMutableList().apply {
                                        this[i] = 0
                                    }
                                }
                            }
                        }
                        TimeBlockInput(isBtnClicked, time2.toIntArray()) {
                            for (i in 0..23) {
                                if (it[i] && !isBtnClicked) {
                                    time2 = time2.toMutableList().apply {
                                        this[i] = 1
                                    }
                                } else {
                                    time2 = time2.toMutableList().apply {
                                        this[i] = 0
                                    }
                                }
                            }
                        }
                        TimeBlockInput(isBtnClicked, time3.toIntArray()) {
                            for (i in 0..23) {
                                if (it[i] && !isBtnClicked) {
                                    time3 = time3.toMutableList().apply {
                                        this[i] = 1
                                    }
                                } else {
                                    time3 = time3.toMutableList().apply {
                                        this[i] = 0
                                    }
                                }
                            }
                        }
                    }
                } else if (page == 2) {
                    Row {
                        TimeBlockInput(isBtnClicked, time4.toIntArray()) {
                            for (i in 0..23) {
                                if (it[i] && !isBtnClicked) {
                                    time4 = time4.toMutableList().apply {
                                        this[i] = 1
                                    }
                                } else {
                                    time4 = time4.toMutableList().apply {
                                        this[i] = 0
                                    }
                                }
                            }
                        }
                        TimeBlockInput(isBtnClicked, time5.toIntArray()) {
                            for (i in 0..23) {
                                if (it[i] && !isBtnClicked) {
                                    time5 = time5.toMutableList().apply {
                                        this[i] = 1
                                    }
                                } else {
                                    time5 = time5.toMutableList().apply {
                                        this[i] = 0
                                    }
                                }
                            }
                        }
                        TimeBlockInput(isBtnClicked, time6.toIntArray()) {
                            for (i in 0..23) {
                                if (it[i] && !isBtnClicked) {
                                    time6 = time6.toMutableList().apply {
                                        this[i] = 1
                                    }
                                } else {
                                    time6 = time6.toMutableList().apply {
                                        this[i] = 0
                                    }
                                }
                            }
                        }
                    }
                } else {
                    TimeBlockInput(isBtnClicked, time7.toIntArray()) {
                        for (i in 0..23) {
                            if (it[i] && !isBtnClicked) {
                                time7 = time7.toMutableList().apply {
                                    this[i] = 1
                                }
                            } else {
                                time7 = time7.toMutableList().apply {
                                    this[i] = 0
                                }
                            }
                        }
                    }
                }
            }

            //가능한시간 없음
            Button(
                onClick = {
                    isBtnClicked = !isBtnClicked
                    for (i in 0..23) {
                        time1 = time1.toMutableList().apply { this[i] = 0 }
                        time2 = time2.toMutableList().apply { this[i] = 0 }
                        time3 = time3.toMutableList().apply { this[i] = 0 }
                        time4 = time4.toMutableList().apply { this[i] = 0 }
                        time5 = time5.toMutableList().apply { this[i] = 0 }
                        time6 = time6.toMutableList().apply { this[i] = 0 }
                        time7 = time7.toMutableList().apply { this[i] = 0 }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black // 텍스트 색상 또는 아이콘 색상 설정
                ),
                elevation = null, // 그림자 제거
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    var painter =
                        if (!isBtnClicked) R.drawable.ic_timeinput else R.drawable.ic_timeinput_clicked
                    var color = if (!isBtnClicked) R.color.disabled else R.color.main_blue

                    Image(
                        painter = painterResource(id = painter),
                        contentDescription = "가능한시간없음",
                    )
                    Text(
                        text = "가능한 시간 없음",
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight(400),
                        color = colorResource(id = color),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        var activate = true
        //버튼 입력
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, start = 24.dp, end = 24.dp),
            onClick = {
                var userTime = UserTime(
                    scheduleId = scheduleId.toString(),
                    userName = userName.toString(),
                    time1 = time1.joinToString(separator = ","),
                    time2 = time2.joinToString(separator = ","),
                    time3 = time3.joinToString(separator = ","),
                    time4 = time4.joinToString(separator = ","),
                    time5 = time5.joinToString(separator = ","),
                    time6 = time6.joinToString(separator = ","),
                    time7 = time7.joinToString(separator = ",")
                )
                userTimeViewModel.UpdateUserTime(userTime)
                navController.navigate("${Routes.TimeSheet.route}?scheduleId=$scheduleId")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (activate) colorResource(id = R.color.main_blue) else colorResource(
                    id = R.color.disabled
                ),
            )
        ) {
            Text(
                text = "저장",
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

fun initUserTimeInfo(
    scheduleId: Int,
    userName: String,
    userTimeViewModel: UserTimeViewModel,
    onInit: (UserTime) -> Unit
) {
    userTimeViewModel.GetUserTime(scheduleId, userName) {
        onInit(it)
    }
}

