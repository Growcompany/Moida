package com.example.moida.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.moida.R
import com.example.moida.component.BottomBtn
import com.example.moida.component.HeadOfTime
import com.example.moida.component.MemTimeBlock
import com.example.moida.component.NumberSection
import com.example.moida.component.ShowTimeLine
import com.example.moida.component.TitleWithXBtn
import com.example.moida.model.Routes
import com.example.moida.model.schedule.FixedScheduleViewModel
import com.example.moida.model.schedule.ScheduleData
import com.example.moida.model.schedule.ScheduleViewModel
import com.example.moida.model.schedule.UserTime
import com.example.moida.model.schedule.UserTimeViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TimeSheet(
    navController: NavHostController,
    scheduleViewModel: ScheduleViewModel,
    userTimeViewModel: UserTimeViewModel,
    fixedScheduleViewModel: FixedScheduleViewModel,
    scheduleId: Int
) {
    var scheduleName by remember { mutableStateOf(scheduleViewModel.selectedItem.scheduleName) }
    var startDate by remember { mutableStateOf(scheduleViewModel.selectedItem.scheduleStartDate) }
    var startDay by remember { mutableStateOf(DayOfWeek.MONDAY) }
    var page by remember { mutableIntStateOf(1) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val memberCount = 5 //

    var selectedItem by remember {
        mutableStateOf<ScheduleData?>(null)
    }

    var time1 by remember { mutableStateOf(List(24) { 0 }) }
    var time2 by remember { mutableStateOf(List(24) { 0 }) }
    var time3 by remember { mutableStateOf(List(24) { 0 }) }
    var time4 by remember { mutableStateOf(List(24) { 0 }) }
    var time5 by remember { mutableStateOf(List(24) { 0 }) }
    var time6 by remember { mutableStateOf(List(24) { 0 }) }
    var time7 by remember { mutableStateOf(List(24) { 0 }) }

    val context = LocalContext.current
    val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory(context))
    val userName = signInViewModel.userName.value

    LaunchedEffect(selectedItem) {
        initInfo(scheduleId, scheduleViewModel) {
            scheduleName = it.scheduleName
            startDate = it.scheduleStartDate
            startDay = getDayofWeek(startDate)
        }
        initUserTimeInfoinTimeSheet(
            scheduleId,
            userName.toString(),
            userTimeViewModel
        ) { userTime ->
            time1 = userTime.time1.split(",").map { it.toInt() }
            time2 = userTime.time2.split(",").map { it.toInt() }
            time3 = userTime.time3.split(",").map { it.toInt() }
            time4 = userTime.time4.split(",").map { it.toInt() }
            time5 = userTime.time5.split(",").map { it.toInt() }
            time6 = userTime.time6.split(",").map { it.toInt() }
            time7 = userTime.time7.split(",").map { it.toInt() }
        }
    }

    Column {
        //제목부분
        TitleWithXBtn(
            navController = navController,
            route = Routes.CreateGroupSchedule.route,
            title = scheduleName,
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
                            val date = LocalDate.parse(
                                startDate,
                                formatter
                            )
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
                            val date = LocalDate.parse(
                                startDate,
                                formatter
                            )
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
                        MemTimeBlock(memberCount, time1.toIntArray()) {
                        }
                        MemTimeBlock(memberCount, time2.toIntArray()) {
                        }
                        MemTimeBlock(memberCount, time3.toIntArray()) {
                        }
                    }
                } else if (page == 2) {
                    Row {
                        MemTimeBlock(memberCount, time4.toIntArray()) {
                        }
                        MemTimeBlock(memberCount, time5.toIntArray()) {
                        }
                        MemTimeBlock(memberCount, time6.toIntArray()) {
                        }
                    }
                } else {
                    MemTimeBlock(memberCount, time7.toIntArray()) {
                    }
                }
            }
            //TimeBlockGroup(page = page, memberCount = memberCount, startDate = startDate)
            NumberSection(memberCount = memberCount)
        }

        Spacer(modifier = Modifier.weight(1f))

        //버튼 입력
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {
            BottomBtn(
                navController = navController,
                route = "${Routes.TimeInput.route}?scheduleId=$scheduleId",
                value = "",
                btnName = "내 시간 입력하기",
                activate = true
            )
        }
    }
}

fun initInfo(
    scheduleId: Int,
    scheduleViewModel: ScheduleViewModel,
    onInit: (ScheduleData) -> Unit
) {
    scheduleViewModel.GetSchedule(scheduleId) {
        onInit(it)
    }
}

fun initUserTimeInfoinTimeSheet(
    scheduleId: Int,
    userName: String,
    userTimeViewModel: UserTimeViewModel,
    onInit: (UserTime) -> Unit
) {
    var res1 = List(24) { 0 }.toMutableList()
    var res2 = List(24) { 0 }.toMutableList()
    var res3 = List(24) { 0 }.toMutableList()
    var res4 = List(24) { 0 }.toMutableList()
    var res5 = List(24) { 0 }.toMutableList()
    var res6 = List(24) { 0 }.toMutableList()
    var res7 = List(24) { 0 }.toMutableList()

    val list = userTimeViewModel.GetAllUserTime(scheduleId)

    for (i in 0..<list.value.size) {
        val dayList1 = list.value[i].time1.split(",").map { it.toInt() }
        val dayList2 = list.value[i].time2.split(",").map { it.toInt() }
        val dayList3 = list.value[i].time3.split(",").map { it.toInt() }
        val dayList4 = list.value[i].time4.split(",").map { it.toInt() }
        val dayList5 = list.value[i].time5.split(",").map { it.toInt() }
        val dayList6 = list.value[i].time6.split(",").map { it.toInt() }
        val dayList7 = list.value[i].time7.split(",").map { it.toInt() }

        for (j in 0..23) {
            res1[j] += dayList1[j]
            res2[j] += dayList2[j]
            res3[j] += dayList3[j]
            res4[j] += dayList4[j]
            res5[j] += dayList5[j]
            res6[j] += dayList6[j]
            res7[j] += dayList7[j]
        }
    }
    onInit(
        UserTime(
            scheduleId.toString(),
            userName,
            res1.joinToString(separator = ","),
            res2.joinToString(separator = ","),
            res3.joinToString(separator = ","),
            res4.joinToString(separator = ","),
            res5.joinToString(separator = ","),
            res6.joinToString(separator = ","),
            res7.joinToString(separator = ",")
        )
    )

}

fun getDayofWeek(startDate: String): DayOfWeek {
    // 날짜 문자열을 파싱하기 위한 포맷터 정의
    val originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    // LocalDate 객체로 변환
    val date = LocalDate.parse(startDate, originalFormatter)
    return date.dayOfWeek
}



