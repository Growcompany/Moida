package com.example.moida.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moida.R
import com.example.moida.ui.theme.Pretendard
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun <T> MainCalendar(
    events: Map<LocalDate, List<T>>,
    onDateClick: (LocalDate) -> Unit,
    updateTitle: (String) -> Unit,
    hasEvents: (LocalDate, Map<LocalDate, List<T>>) -> Boolean
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(50) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }
    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }


    Column(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 24.dp)
    ) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first()
        )
        val visibleMonth by remember { derivedStateOf { state.firstVisibleMonth.yearMonth } }
        val coroutineScope = rememberCoroutineScope()

        CalendarTitle(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            visibleMonth = visibleMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNext = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            }
        )
        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                if (day.position == DayPosition.MonthDate) {
                    MainDay(
                        day,
                        isSelected = selectedDate == day.date,
                        hasEvents = hasEvents(day.date, events)
                    ) { selectedDay ->
                        selectedDate = selectedDay.date
                        val title = if (selectedDate == today) {
                            "오늘의 일정"
                        } else {
                            "${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일의 일정"
                        }
                        updateTitle(title)
                        onDateClick(selectedDay.date)
                    }
                }
            },
            monthHeader = {
                MonthHeader(daysOfWeek = daysOfWeek)
            }
        )
    }
}

@Composable
fun BottomSheetCalendar(
    onDateClick: (LocalDate) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(50) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }
    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }

    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
    ) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first()
        )
        val visibleMonth by remember { derivedStateOf { state.firstVisibleMonth.yearMonth } }
        val coroutineScope = rememberCoroutineScope()

        CalendarTitle(
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 4.dp),
            visibleMonth = visibleMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNext = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            }
        )
        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                if (day.position == DayPosition.MonthDate) {
                    BottomSheetDay(
                        day,
                        isSelected = selectedDate == day.date
                    ) { selectedDay ->
                        selectedDate  = if (selectedDate == selectedDay.date) null else selectedDay.date
                        onDateClick(selectedDate)
                    }
                }
            },
            monthHeader = {
                MonthHeader(daysOfWeek = daysOfWeek)
            }
        )
    }

}


@Composable
fun CalendarTitle(
    modifier: Modifier,
    visibleMonth: YearMonth,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalendarNavigationIcon(
            icon = painterResource(id = R.drawable.chevron_left),
            contentDescription = "Previous",
            onClick = goToPrevious
        )
        Text(
            modifier = Modifier
                .weight(1f),
            text = "${visibleMonth.year}년 ${visibleMonth.monthValue}월",
            fontFamily = Pretendard,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        CalendarNavigationIcon(
            icon = painterResource(id = R.drawable.chevron_right),
            contentDescription = "Next",
            onClick = goToNext
        )
    }
}

@Composable
fun CalendarNavigationIcon(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit
) = Box(
    modifier = Modifier
        .aspectRatio(1f)
        .clip(shape = CircleShape)
        .clickable(role = Role.Button, onClick = onClick)
) {
    Icon(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .align(Alignment.Center),
        painter = icon,
        contentDescription = contentDescription
    )
}

@Composable
fun MonthHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        for (dayOfWeek in daysOfWeek) {
            val textColor =
                if (dayOfWeek == DayOfWeek.SUNDAY) colorResource(id = R.color.error)
                else colorResource(id = R.color.text_high)
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                color = textColor,
                text = dayOfWeek.getDisplayName(
                    TextStyle.SHORT, Locale.KOREAN
                )
            )
        }
    }
}

@Composable
fun MainDay(
    day: CalendarDay,
    isSelected: Boolean,
    hasEvents: Boolean,
    onClick: (CalendarDay) -> Unit
) {
    val textColor = when {
        day.date == LocalDate.now() -> colorResource(id = R.color.main_blue)
        day.date.dayOfWeek == DayOfWeek.SUNDAY -> colorResource(id = R.color.error)
        isHoliday(day.date) -> colorResource(id = R.color.error)
        else -> colorResource(id = R.color.text_high)
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f) // square size
            .clickable(
                onClick = { onClick(day) },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(13.dp)
                .clip(CircleShape)
                .background(color = if (isSelected) colorResource(id = R.color.blue3) else Color.Transparent)
        )
        Text(
            text = day.date.dayOfMonth.toString(),
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = textColor
        )
        if (hasEvents) {
            Box(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.blue1))
            )
        }
    }
}

@Composable
fun BottomSheetDay(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit
) {
    val textColor = when {
        day.date == LocalDate.now() -> colorResource(id = R.color.main_blue)
        day.date.dayOfWeek == DayOfWeek.SUNDAY -> colorResource(id = R.color.error)
        isHoliday(day.date) -> colorResource(id = R.color.error)
        else -> colorResource(id = R.color.text_high)
    }
    Box(
        modifier = Modifier
            .aspectRatio(1f) // square size
            .clickable(
                onClick = {
                    onClick(day)
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .padding(13.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) colorResource(id = R.color.blue3) else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = textColor
        )
    }
}

// 공휴일 리스트 2024년 업데이트
fun isHoliday(date: LocalDate): Boolean {
    val holidays = listOf(
        LocalDate.of(date.year, 1, 1), // 신정
        LocalDate.of(date.year, 3, 1), // 삼일절
        LocalDate.of(date.year, 5, 5), // 어린이날
        LocalDate.of(date.year, 5, 6), // 대체 휴일
        LocalDate.of(date.year, 5, 15), // 부처님 오신날
        LocalDate.of(date.year, 6, 6), // 현충일
        LocalDate.of(date.year, 8, 15), // 광복절
        LocalDate.of(date.year, 9, 16), // 추석 연휴
        LocalDate.of(date.year, 9, 17), // 추석
        LocalDate.of(date.year, 9, 18), // 추석 연휴
        LocalDate.of(date.year, 10, 3), // 개천절
        LocalDate.of(date.year, 10, 9), // 한글날
        LocalDate.of(date.year, 12, 25), // 성탄절
    )
    return date in holidays
}

//@Composable
//@Preview
//fun CalendarPreview() {
//    BottomSheetCalendar()
//}