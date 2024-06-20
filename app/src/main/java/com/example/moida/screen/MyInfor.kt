package com.example.moida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.moida.R
import com.example.moida.component.FieldTitle
import com.example.moida.component.Title
import com.example.moida.model.BottomNavItem
import com.example.moida.model.Routes
import com.example.moida.ui.theme.MoidaTheme
import com.example.moida.ui.theme.Pretendard
import com.example.moida.util.SharedPreferencesHelper
import kotlinx.coroutines.launch

@Composable
fun MyInfor(navController: NavHostController) {
    val context = LocalContext.current
    val prefsHelper = remember { SharedPreferencesHelper(context) }
    val name = prefsHelper.getUsername()
    val id = prefsHelper.getEmail()
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val viewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory(context))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Title(
            navController = navController,
            route = BottomNavItem.Home.route,
            title = "회원정보",
            rightBtn = "    ",
            rightColor = R.color.main_blue
        )

        Column(
            Modifier.background(Color.White)
                .padding(start = 24.dp, end = 16.dp)
        ) {
            Box(
                modifier = Modifier.padding(vertical = 30.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "프로필 이미지",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .size(100.dp)

                )
            }

            FieldTitle("이름")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 14.dp)
                    .clickable {
                        navController.navigate(Routes.ChangeName.route){
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
                    text = name ?: "이름",
                    fontFamily = Pretendard,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_high)
                )
                Icon(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = "이름 변경 화면 이동",
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
            )
            Spacer(modifier = Modifier.height(16.dp))

            FieldTitle("연결된 계정")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = id ?: "이메일",
                    fontFamily = Pretendard,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_high)
                )

            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(42.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = androidx.compose.ui.text.SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("회원탈퇴")
                        }
                    },
                    fontFamily = Pretendard,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.gray_500),
                    modifier = Modifier.clickable {
                        showDialog = true
                    }
                )
            }
            if (showDialog) {
                CustomAlertDialog(
                    title = "탈퇴하시겠습니까?",
                    message = "계정 내 참여한 모임, 약속이 모두 삭제되며 복구되지 않습니다.",
                    confirmButtonText = "탈퇴",
                    dismissButtonText = "취소",
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        showDialog = false
                        scope.launch {
                            viewModel.deleteUser()
                            // 네비게이션 스택을 초기화하고 ResignMemberShip으로 이동
                            navController.navigate(Routes.ResignMemberShip.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyInforPreview() {
    MoidaTheme {
        MyInfor(navController = rememberNavController())
    }
}