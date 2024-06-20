package com.example.moida

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moida.model.BottomNavigationItem
import com.example.moida.model.NavGraph
import com.example.moida.screen.TimeInput
import com.example.moida.ui.theme.MoidaTheme
import com.example.moida.ui.theme.Pretendard
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MoidaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (currentRoute !in listOf(
                                    "launchPage",
                                    "signIn",
                                    "joinMembership",
                                    "ResignMemberShip"
                                )
                            ) {
                                NavigationBar(
                                    containerColor = colorResource(id = R.color.white),
                                    tonalElevation = 10.dp
                                ) {
                                    BottomNavigationItem().bottomNavigationItems()
                                        .forEachIndexed { _, item ->
                                            NavigationBarItem(
                                                selected = item.route == currentRoute,
                                                label = {
                                                    Text(
                                                        text = stringResource(item.label),
                                                        fontFamily = Pretendard,
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 12.sp
                                                    )
                                                },
                                                onClick = {
                                                    navController.navigate(item.route) {
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                },
                                                icon = {
                                                    Icon(
                                                        painterResource(id = item.icon),
                                                        contentDescription = item.label.toString()
                                                    )
                                                },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = colorResource(id = R.color.main_blue),
                                                    unselectedIconColor = colorResource(id = R.color.disabled),
                                                    selectedTextColor = colorResource(id = R.color.main_blue),
                                                    unselectedTextColor = colorResource(id = R.color.disabled)
                                                )
                                            )
                                        }
                                }
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier.padding(it)
                        ) {
                            NavGraph(
                                navController = navController,
                            )
                        }
                    }
                }
//                GroupDetail();
                //TimeInput(navController = rememberNavController());
            }
        }
    }
}
