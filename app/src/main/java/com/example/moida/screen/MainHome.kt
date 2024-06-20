package com.example.moida.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.moida.component.HomeTitle
import com.example.moida.component.MainCalendar
import com.example.moida.component.TodayItem
import com.example.moida.component.TodayItemList
import com.example.moida.component.UpcomingItem
import com.example.moida.component.UpcomingItemList
import com.example.moida.model.TodayViewModel
import com.example.moida.model.UpcomingViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MainHome(
    navController: NavHostController,
    todayViewModel: TodayViewModel = viewModel(),
    upcomingViewModel: UpcomingViewModel = viewModel(),
) {
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val todayEvents by todayViewModel.itemList.collectAsState()
    val upcomingEvents by upcomingViewModel.itemList.collectAsState()

    val groupedTodayEvents = todayEvents.groupBy { LocalDate.parse(it.date, dateFormatter) }
    val todayDate = remember { LocalDate.now() }
    var selectedEvents by remember { mutableStateOf(groupedTodayEvents[todayDate].orEmpty()) }
    var title by remember { mutableStateOf("오늘의 일정") }

    LaunchedEffect(todayEvents) {
        selectedEvents = groupedTodayEvents[todayDate].orEmpty()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        item {
            HomeTitle(navController)
        }
        item {
            MainCalendar(
                events = groupedTodayEvents,
                onDateClick = {date ->
                    selectedEvents = groupedTodayEvents[date].orEmpty()
                },
                updateTitle = {it ->
                    title = it
                },
                hasEvents = {date, events ->
                    events[date]?.isNotEmpty() == true
                }
            )
        }
        item {
            TodayItemList(selectedEvents.size, title)
        }
        // 오늘의 일정 리스트
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            ) {
                selectedEvents.forEach { item ->
                    TodayItem(item)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
        item {
            UpcomingItemList(upcomingViewModel)
        }
        // 대기 중인 일정 리스트
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 100.dp)
            ){
                upcomingEvents.forEach {item ->
                    UpcomingItem(item)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

