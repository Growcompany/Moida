package com.example.moida.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moida.R
import com.example.moida.model.GroupItemData
import com.example.moida.model.TodayItemData
import com.example.moida.model.UpcomingItemData
import com.example.moida.model.UpcomingViewModel
import com.example.moida.ui.theme.Pretendard

@Composable
fun TodayItem(item: TodayItemData) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {
        Box(
            modifier = Modifier.padding(all = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.time,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.text_high),
                    modifier = Modifier.padding(end = 20.dp)
                )

                Divider(
                    color = colorResource(id = R.color.disabled),
                    modifier = Modifier
                        .height(26.dp)
                        .width(1.dp)
                )

                Column(
                    modifier = Modifier.padding(start = 20.dp)
                ) {
                    Text(
                        text = item.name,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.text_high),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = item.category,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.text_medium)
                    )
                }
            }
        }
    }
}

@Composable
fun UpcomingItem(item: UpcomingItemData) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

        ) {
        Box(
            modifier = Modifier.padding(all = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(end = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = item.startDate,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.text_high)
                    )
                    Text(
                        text = "-",
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.text_high)
                    )
                    Text(
                        text = item.endDate,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.text_high)
                    )
                }


                Divider(
                    color = colorResource(id = R.color.disabled),
                    modifier = Modifier
                        .height(26.dp)
                        .width(1.dp)
                )

                Column(
                    modifier = Modifier.padding(start = 20.dp)
                ) {
                    Text(
                        text = item.name,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.text_high),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = item.category,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.text_medium)
                    )
                }

            }
        }
    }

}

@Composable
fun GroupItem(item: GroupItemData) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {
        Box(
            modifier = Modifier.padding(all = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.time,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.text_high),
                    modifier = Modifier.padding(end = 20.dp)
                )

                Divider(
                    color = colorResource(id = R.color.disabled),
                    modifier = Modifier
                        .height(26.dp)
                        .width(1.dp)
                )

                Column(
                    modifier = Modifier.padding(start = 20.dp)
                ) {
                    Text(
                        text = item.scheduleName,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.text_high),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TodayItemList(todayItemCount: Int, title: String) {
    Column(
        modifier = Modifier
        .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 6.dp)
            )

            OutlinedCard(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, colorResource(id = R.color.main_blue)),
                modifier = Modifier
                    .size(width = 20.dp, height = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = todayItemCount.toString(),
                        fontSize = 12.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.main_blue)
                    )
                }
            }
        }
    }
}

@Composable
fun UpcomingItemList(upcomingViewModel: UpcomingViewModel) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "대기 중인 일정",
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 6.dp)
            )

            OutlinedCard(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, colorResource(id = R.color.main_blue)),
                modifier = Modifier
                    .size(width = 20.dp, height = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = upcomingViewModel.getItemCount().toString(),
                        fontSize = 12.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.main_blue)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ItemPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TodayItem(
            item = TodayItemData(
                "2024.05.28", "12:00", "2차 스터디", "compose 스터디"
            )
        )
        UpcomingItem(
            item = UpcomingItemData(
                "2024.05.10", "2024.05.17", "3차 스터디", "compose 스터디"
            )
        )
        GroupItem(
            item = GroupItemData(
                "1차 스터디", "2024.05.31", "12:00"
            )
        )
    }
}