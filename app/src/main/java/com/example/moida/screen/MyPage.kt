package com.example.moida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.moida.R
import com.example.moida.model.Routes
import com.example.moida.ui.theme.MoidaTheme
import com.example.moida.ui.theme.Pretendard
import kotlinx.coroutines.launch

@Composable
fun MyPage(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory(context))
    val userName by viewModel.userName.collectAsState()
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    Column(
        Modifier.background(Color.White)
    ) {
        Text(
            text = "MY",
            fontFamily = Pretendard,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 38.dp, start = 34.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 24.dp, bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(60.dp)
            )
            Text(
                text = userName ?: "닉네임",
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.text_high),
                modifier = Modifier
                    .padding(start = 20.dp)
            )
        }
        Divider(
            color = colorResource(id = R.color.gray_50),
            modifier = Modifier
                .height(18.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 16.dp, top = 22.dp, bottom = 22.dp)
                .clickable {
                    navController.navigate(Routes.Myinfor.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "회원정보",
                fontFamily = Pretendard,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.text_high)
            )
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = "회원 정보 화면 이동",
                tint = colorResource(id = R.color.disabled),
                modifier = Modifier
                    .size(24.dp)
                    .padding(horizontal = 9.dp, vertical = 6.dp)

            )
        }
        Divider(
            color = colorResource(id = R.color.gray_100),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 16.dp, top = 22.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "공지사항",
                fontFamily = Pretendard,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.text_high)
            )
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = "공지사항 화면 이동",
                tint = colorResource(id = R.color.disabled),
                modifier = Modifier
                    .size(24.dp)
                    .padding(horizontal = 9.dp, vertical = 6.dp)

            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 16.dp, bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "문의하기",
                fontFamily = Pretendard,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.text_high)
            )
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = "문의하기 화면 이동",
                tint = colorResource(id = R.color.disabled),
                modifier = Modifier
                    .size(24.dp)
                    .padding(horizontal = 9.dp, vertical = 6.dp)

            )
        }
        Divider(
            color = colorResource(id = R.color.gray_100),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 16.dp, top = 22.dp, bottom = 24.dp)
                .clickable {
                    showDialog = true
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "로그아웃",
                fontFamily = Pretendard,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(id = R.color.text_high)
            )
        }
        if (showDialog) {
            CustomAlertDialog(
                title = "로그아웃 하시겠습니까?",
                message = "",
                confirmButtonText = "로그아웃",
                dismissButtonText = "취소",
                onDismiss = { showDialog = false },
                onConfirm = {
                    showDialog = false
                    scope.launch {
                        viewModel.signOut()
                        // 네비게이션 스택을 초기화하고 LaunchPage로 이동
                        navController.navigate(Routes.LaunchPage.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPage() {
    MoidaTheme {
        MyPage(navController = rememberNavController())
    }
}