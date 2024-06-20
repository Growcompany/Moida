package com.example.moida.component

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.moida.R
import com.example.moida.model.BottomNavItem
import com.example.moida.model.GroupDetailViewModel
import com.example.moida.model.GroupInfo
import com.example.moida.model.Routes
import com.example.moida.screen.imageResources
import com.example.moida.ui.theme.Pretendard

@Composable
fun Title(
    navController: NavHostController, 
    route: String, 
    title: String, 
    rightBtn: String, 
    rightColor: Int?
) {
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
                    navController.navigate(route) {
                        popUpTo(route) { inclusive = true }
                    }
                }
        )
        Text(
            text = title,
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        if (rightBtn=="햄버거") {
            Image(
                painter = painterResource(id = R.drawable.ic_dots_vertical),
                contentDescription = "뒤로가기",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {

                    }
            )
        }

        if (rightColor != null) {
            Text(
                text = rightBtn,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                color =  colorResource(id = rightColor),
                fontSize = 18.sp
            )
        } else {
            Text(
                text = rightBtn,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun TitleWithXBtn(
    navController: NavHostController,
    route: String,
    title: String,
    rightBtn: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_x_close),
            contentDescription = "뒤로가기",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navController.navigate(route) {
                        popUpTo(route) { inclusive = true }
                    }
                }
        )
        Text(
            text = title,
            fontFamily = Pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        if (rightBtn) {
            Image(
                painter = painterResource(id = R.drawable.ic_dots_vertical),
                contentDescription = "뒤로가기",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { }
            )
        } else {
            Box(
                modifier = Modifier.size(24.dp)
            ) {

            }
        }

    }
}

@Composable
fun HomeTitle(
    navController: NavHostController,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.moida_logo),
            contentDescription = "로고",
        )

        OutlinedButton(
            onClick = {
                navController.navigate(Routes.ScheduleDetail.route)
            },
            modifier = Modifier
                .wrapContentSize(),
            border = BorderStroke(1.dp, colorResource(id = R.color.main_blue)),
        ) {
            Text(
                text = "개인 일정 추가",
                fontFamily = Pretendard,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = R.color.text_high)
            )
        }
    }
}

@Composable
fun GroupDetailTitle(group: GroupInfo, onMenuClick: () -> Unit, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            Image(
                painter = painterResource(id = imageResources[group.groupImg]),
                contentDescription = "그룹 대표 이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = "뒤로 가기",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f))
                        .padding(start = 6.dp, top = 8.dp, bottom = 8.dp, end = 10.dp)
                        .clickable { navController.navigate(BottomNavItem.Group.route) },
                    tint = colorResource(id = R.color.text_high)
                )
                Icon(
                    painter = painterResource(id = R.drawable.menu_24),
                    contentDescription = "메뉴",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f))
                        .padding(all = 6.dp)
                        .clickable {
                            onMenuClick()
                        },
                    tint = colorResource(id = R.color.text_high)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 37.dp, bottom = 6.dp),
                text = group.groupName,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = colorResource(id = R.color.text_high)
            )

            OutlinedButton(
                onClick = {
                    val route = Routes.CreateGroupSchedule.createRoute(group.groupId)
                    navController.navigate(route)
                },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 37.dp),
                border = BorderStroke(1.dp, colorResource(id = R.color.main_blue)),
            ) {
                Text(
                    text = "일정 잡기",
                    fontFamily = Pretendard,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.text_high)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.person_24),
                contentDescription = "사람 아이콘",
                modifier = Modifier
                    .width(16.dp)
                    .height(16.dp),
                tint = colorResource(id = R.color.text_medium)
            )
            Text(
                text = "${group.memberCount}명",
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = colorResource(id = R.color.text_medium),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

//@Composable
//@Preview
//fun TitlePreview() {
//    GroupDetailTitle(group = GroupInfo("Compose 스터디", R.drawable.sample_image8, 4))
//
//}