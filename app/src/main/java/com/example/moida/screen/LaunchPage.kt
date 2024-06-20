package com.example.moida.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.moida.R
import com.example.moida.model.BottomNavItem
import com.example.moida.model.Routes
import com.example.moida.ui.theme.MoidaTheme
import com.example.moida.ui.theme.Pretendard

@Composable
fun LaunchPage(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory(context))

    // 자동 로그인 확인
    val id by viewModel.userName.collectAsState()
    LaunchedEffect(id) {
        if (id != null) {
            navController.navigate(BottomNavItem.Home.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .weight(1f), // Use weight to push content to the center
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "빠르고 간편한 일정관리",
                fontFamily = Pretendard,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            Icon(
                painter = painterResource(id = R.drawable.moida_logo),
                contentDescription = "로고",
                Modifier.width(204.dp).height(111.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    navController.navigate(Routes.JoinMembership.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.gray_200)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "회원가입",
                    color = Color.White,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    navController.navigate(Routes.SignIn.route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.main_blue)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "로그인",
                    color = Color.White,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LaunchPagePreview() {
    MoidaTheme {
        LaunchPage(navController = rememberNavController())
    }
}
