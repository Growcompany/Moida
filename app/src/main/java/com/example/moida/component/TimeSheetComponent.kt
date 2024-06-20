package com.example.moida.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moida.R
import com.example.moida.ui.theme.Pretendard
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HeadOfTime( //날짜+요일 3개 출력
    startDate: String,
    startDay: DayOfWeek,
    page: Int
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MM.dd")
    var currentDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    var currentDay = startDay

    Column(
        modifier = Modifier
    ) {
        Row(
        ) {
            if (page == 3) {
                DateOfTimeSheet(
                    date = currentDate.format(dateFormatter),
                    day = currentDay.getDisplayName(java.time.format.TextStyle.FULL, Locale.KOREAN)
                )
            } else {
                for (i in 0 until 3) {
                    DateOfTimeSheet(
                        date = currentDate.format(dateFormatter),
                        day = currentDay.getDisplayName(
                            java.time.format.TextStyle.FULL,
                            Locale.KOREAN
                        )
                    )

                    currentDate = currentDate.plusDays(1)
                    currentDay = currentDay.plus(1)
                }
            }
        }
    }
}

@Composable
fun DateOfTimeSheet( //날짜+요일
    date: String,
    day: String
) {
    val textStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 2.52.sp,
        fontFamily = Pretendard,
        fontWeight = FontWeight(700),
        color = colorResource(id = R.color.gray_800),
        textAlign = TextAlign.Center,
    )

    Row(
        modifier = Modifier
            .width(80.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val dayKorean = day.substring(0, 1)
        Text(text = date, style = textStyle)
        Spacer(modifier = Modifier.padding(end = 4.dp))
        Text(text = dayKorean, style = textStyle)
    }
}

@Composable
fun NumberSection(memberCount: Int) { //숫자블록전체
    Column(
        modifier = Modifier.padding(start = 10.dp, top = 13.dp)
    ) {
        if (memberCount < 4) {
            var end = 2 //기본으로 0,1,2
            if (memberCount == 3) end = 3 // 3명인 경우 0,1,2,3
            for (i in 0..end) {
                NumberBlock(peopleRange = i.toString(), fillColor = i)
            }
        } else {
            var startNum = 1
            var endNum = memberCount / 3
            NumberBlock(peopleRange = "0", fillColor = 0)

            for (i in 1..3) {
                if (startNum == endNum) NumberBlock(peopleRange = "$startNum", fillColor = i)
                else NumberBlock(peopleRange = "$startNum-$endNum", fillColor = i)

                startNum = endNum + 1
                if (i == 2) endNum = memberCount
                else endNum *= 2
            }
        }
    }
}

@Composable
fun NumberBlock( //숫자 블록 하나
    peopleRange: String,
    fillColor: Int
) {
    val backgroundColor: Int

    if (fillColor == 0) backgroundColor = R.color.white
    else if (fillColor == 1) backgroundColor = R.color.blue3
    else if (fillColor == 2) backgroundColor = R.color.blue1
    else backgroundColor = R.color.main_blue

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(33.dp)
                .background(color = colorResource(id = backgroundColor))
                .border(width = 1.dp, color = colorResource(id = R.color.gray_100))
        )
        Spacer(modifier = Modifier.padding(6.dp))
        Text(
            text = peopleRange,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(400),
            color = colorResource(id = R.color.gray_500),
        )
    }
}

@Composable
fun ShowTimeLine() { //시간대
    Column(
        modifier = Modifier
            .padding(start = 24.dp, top = 7.dp),
        horizontalAlignment = Alignment.End
    ) {
        for (i in 0..24) {
            Text(
                text = "$i:00",
                fontSize = 10.sp,
                lineHeight = 12.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(400),
                color = colorResource(id = R.color.gray_500),
                textAlign = TextAlign.Right,
            )
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
        }
    }
}

@Composable
fun TimeBlock( // 하루에 대한 타임블록
    memberCount: Int,
    timeList: IntArray
) {
    val box0 = 0
    val box1 = memberCount / 3
    val box2 = memberCount / 3 * 2

    Column(
        modifier = Modifier
    ) {
        for (index in 0 until 24) {
            var backgroundColor = 0
            if (timeList[index] == box0) backgroundColor = R.color.white
            else if (timeList[index] <= box1) backgroundColor = R.color.blue3
            else if (timeList[index] <= box2) backgroundColor = R.color.blue1
            else if (timeList[index] <= memberCount) backgroundColor = R.color.main_blue

            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 24.dp)
                    .background(colorResource(id = backgroundColor))
                    .border(width = 0.3.dp, color = colorResource(id = R.color.disabled))
                    .clickable {
                        //확정 dialog 보여주기
                    }
            )
        }
    }
}

@Composable
fun TimeBlockGroup( //화면에 대한 타임블록
    page: Int,
    memberCount: Int,
    startDate: String
) {
    val timeList =
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    Row(
        modifier = Modifier
            .width(250.dp)
            .padding(top = 13.dp, start = 10.dp)
    ) {
        if (page == 3) {
            //서버로부터 해당 날에 대한 타임라인 받아옴
            TimeBlock(memberCount = memberCount, timeList = timeList)
        } else {
            for (i in 1..3) {
                //서버로부터 해당 날에 대한 타임라인 받아옴
                TimeBlock(memberCount = memberCount, timeList = timeList)
            }
        }
    }
}

@Composable
fun MemTimeBlock( //화면에 대한 타임블록
    memberCount: Int,
    timeList: IntArray,
    onClick: () -> Unit
) {
    val box0 = 0
    val box1 = memberCount / 3
    val box2 = memberCount / 3 * 2

    Column(
        modifier = Modifier
    ) {
        for (index in 0 until 24) {
            var backgroundColor = 0
            if (timeList[index] == box0) backgroundColor = R.color.white
            else if (timeList[index] <= box1) backgroundColor = R.color.blue3
            else if (timeList[index] <= box2) backgroundColor = R.color.blue1
            else if (timeList[index] <= memberCount) backgroundColor = R.color.main_blue

            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 24.dp)
                    .background(colorResource(id = backgroundColor))
                    .border(width = 0.3.dp, color = colorResource(id = R.color.disabled))
                    .clickable {
                        //확정 dialog 보여주기
                    }
            )
        }
    }
}

@Composable
fun TimeBlockInput(
    isBtnClicked: Boolean,
    timeList: IntArray,
    onClick: (List<Boolean>)->Unit
) {
    val boxCount = 24
    val boxHeight = 24.dp
    val boxHeightPx = with(LocalDensity.current) { boxHeight.toPx() }
    var coloredBoxes by remember { mutableStateOf(List(boxCount) { false }) }
    val paintedBoxIndices = remember { mutableStateListOf<Int>() }
    var lastIndex by remember { mutableStateOf(-1) }

    for (i in 0..23) {
        if (timeList[i]==1) {
            coloredBoxes = coloredBoxes.toMutableList().apply {
                this[i] = true
            }
        } else {
            coloredBoxes = coloredBoxes.toMutableList().apply {
                this[i] = false
            }
        }
    }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    val yPosition = change.position.y
                    val index = (yPosition / boxHeightPx).toInt()

                    if (index in 0 until boxCount) {
                        // Check if the current index is different from the last processed index
                        if (index != lastIndex) {
                            coloredBoxes = coloredBoxes.toMutableList().apply {
                                this[index] = !this[index]
                            }
                            if (index in paintedBoxIndices) {
                                paintedBoxIndices.remove(index)
                            } else {
                                paintedBoxIndices.add(index)
                            }
                            onClick(coloredBoxes)
                            lastIndex = index
                        }
                    }
                    change.consume()
                }
            }
    ) {
        Column {
            for (index in 0 until boxCount) {
                val isColored = coloredBoxes[index]
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = boxHeight)
                        .background(
                            if (isColored && !isBtnClicked) colorResource(id = R.color.blue3)
                            else colorResource(id = R.color.white)
                        )
                        .border(width = 0.3.dp, color = colorResource(id = R.color.disabled))
                        .clickable {
                            coloredBoxes = coloredBoxes
                                .toMutableList()
                                .apply {
                                    this[index] = !this[index]
                                }
                            if (index in paintedBoxIndices) {
                                paintedBoxIndices.remove(index)
                            } else {
                                paintedBoxIndices.add(index)
                            }
                            onClick(coloredBoxes)
                        }
                )
            }
        }
    }
}

//fun initColoredBoxes(timeList: IntArray): List<Boolean> {
//    var result = List(24) { false }.toMutableStateList()
//    for (i in 0 until timeList.size) {
//        if (timeList[i] == 0) result[i] = false
//        else result[i] = true
//    }
//    return result
//}

//@Composable
//fun TimeOfDayBlockInput( //화면에 대한 타임블록
//     timeList: IntArray
//) {
//    Row(
//        modifier = Modifier
//            .width(250.dp)
//            .padding(top = 13.dp, start = 10.dp)
//    ) {
//
//    }
//}

//@Composable
//fun TimeBlockInputGroup( //화면에 대한 타임블록
//    page: Int,
//    startDate: String
//) {
//    Row(
//        modifier = Modifier
//            .width(250.dp)
//            .padding(top = 13.dp, start = 10.dp)
//    ) {
//        if (page == 3) {
//            //서버로부터 해당 날에 대한 타임라인 받아옴
//            TimeBlockInput()
//        } else {
//            for (i in 1..3) {
//                //서버로부터 해당 날에 대한 타임라인 받아옴
//                TimeBlockInput()
//            }
//        }
//    }
//}