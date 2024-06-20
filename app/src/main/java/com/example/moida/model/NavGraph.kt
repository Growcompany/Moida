package com.example.moida.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.moida.R
import com.example.moida.component.CalendarBottomSheet
import com.example.moida.component.ScheduleBottomSheet
import com.example.moida.component.TimePicker
import com.example.moida.model.schedule.FixedScheduleRepo
import com.example.moida.model.schedule.FixedScheduleViewModel
import com.example.moida.model.schedule.FixedScheduleViewModelFactory
import com.example.moida.model.schedule.GroupScheduleViewModel
import com.example.moida.model.schedule.MyScheduleViewModel
import com.example.moida.model.schedule.Repository
import com.example.moida.model.schedule.ScheduleViewModel
import com.example.moida.model.schedule.ScheduleViewModelFactory
import com.example.moida.model.schedule.UserTimeRepo
import com.example.moida.model.schedule.UserTimeViewModel
import com.example.moida.model.schedule.UserTimeViewModelFactory
import com.example.moida.screen.ChangeName
import com.example.moida.screen.ChangedName
import com.example.moida.screen.CreateGroupSchedule
import com.example.moida.screen.CreateMeetingScreen
import com.example.moida.screen.CreateMySchedule
import com.example.moida.screen.GroupDetail
import com.example.moida.screen.JoinMembership
import com.example.moida.screen.LaunchPage
import com.example.moida.screen.MainHome
import com.example.moida.screen.MyGroup
import com.example.moida.screen.MyInfor
import com.example.moida.screen.MyPage
import com.example.moida.screen.ResignMemberShip
import com.example.moida.screen.ScheduleDetail
import com.example.moida.screen.ScheduleEdit
import com.example.moida.screen.SignIn
import com.example.moida.screen.TimeInput
import com.example.moida.screen.TimeSheet
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

sealed class BottomNavItem(val title: Int, val icon: Int, var route: String) {
    data object Home : BottomNavItem(R.string.home, R.drawable.home, "home")
    data object Group : BottomNavItem(R.string.group, R.drawable.group, "group")
    data object My : BottomNavItem(R.string.my, R.drawable.my, "my")
}

sealed class Routes(val route: String) {
    data object CreateMySchedule : Routes("createMySchedule")
    data object CreateGroupSchedule : Routes("createGroupSchedule/{groupId}") {
        fun createRoute(groupId: String) = "createGroupSchedule/$groupId"
    }

    data object ScheduleDetail : Routes("scheduleDetail")
    data object ScheduleEdit : Routes("scheduleEdit")
    data object TimeSheet : Routes("timeSheet")
    data object TimeInput : Routes("timeInput")
    data object SignIn : Routes("signIn")
    data object JoinMembership : Routes("joinMembership")
    data object LaunchPage : Routes("launchPage")

    data object Myinfor : Routes("Myinfor")

    data object ChangeName : Routes("ChangeName")

    data object ChangedName : Routes("ChangedName")

    data object ResignMemberShip : Routes("ResignMemberShip")

    data object CreateMeeting : Routes("createMeeting")
    data object CalendarBottomSheet : Routes("calendarBottomSheet")

    data object GroupDetail : Routes("groupDetail/{meetingJson}") {
        fun createRoute(meetingJson: String): String {
            return "groupDetail/$meetingJson"
        }
    }

    data object TimePicker : Routes("timePicker")
    data object ScheduleBottomSheet : Routes("scheduleBottomSheet")
}

@Composable
fun NavGraph(navController: NavHostController) {
    val table = Firebase.database.getReference("pendingSchedule")
    val scheduleViewModel: ScheduleViewModel =
        viewModel(factory = ScheduleViewModelFactory(Repository(table)))
    val table2 = Firebase.database.getReference("userTime")
    val userTimeViewModel: UserTimeViewModel =
        viewModel(factory = UserTimeViewModelFactory(UserTimeRepo(table2)))
    val fixedScheduleTable = Firebase.database.getReference("fixedSchedule")
    val fixedScheduleViewModel: FixedScheduleViewModel =
        viewModel(factory = FixedScheduleViewModelFactory(FixedScheduleRepo(fixedScheduleTable)))

    NavHost(navController = navController, startDestination = Routes.LaunchPage.route) {

        composable(BottomNavItem.Home.route) {
            MainHome(navController)
        }
        composable(BottomNavItem.Group.route) {
            MyGroup(navController)
        }
        composable(BottomNavItem.My.route) {
            MyPage(navController)
        }

        composable(Routes.Myinfor.route) {
            MyInfor(navController)
        }

        composable(Routes.ChangeName.route) {
            ChangeName(navController)
        }

        composable(Routes.ChangedName.route) {
            ChangedName(navController)
        }

        composable(Routes.ResignMemberShip.route) {
            ResignMemberShip(navController)
        }

        composable(Routes.CreateMySchedule.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.CreateMySchedule.route)
            }
            val myScheduleViewModel: MyScheduleViewModel = viewModel(parentEntry)
            CreateMySchedule(navController, myScheduleViewModel, fixedScheduleViewModel)
        }

        composable(
            route = Routes.CreateGroupSchedule.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.CreateGroupSchedule.route)
            }
            val groupScheduleViewModel: GroupScheduleViewModel = viewModel(parentEntry)
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            CreateGroupSchedule(
                navController,
                groupScheduleViewModel,
                scheduleViewModel,
                userTimeViewModel,
                groupId
            )
        }

        composable(
            route = Routes.ScheduleDetail.route + "?scheduleId={scheduleId}",
            arguments = listOf(
                navArgument("scheduleId") {
                    type = NavType.IntType
                }
            )
        ) {
            it.arguments?.getInt("scheduleId")?.let { it1 ->
                ScheduleDetail(navController, fixedScheduleViewModel, scheduleId = it1)
            }
        }

        composable(
            route = Routes.ScheduleEdit.route + "?scheduleId={scheduleId}",
            arguments = listOf(
                navArgument("scheduleId") {
                    type = NavType.IntType
                }
            )
        ) {
            it.arguments?.getInt("scheduleId")?.let { it1 ->
                ScheduleEdit(navController, fixedScheduleViewModel, scheduleId = it1)
            }
        }

        composable(Routes.CreateMeeting.route) {
            CreateMeetingScreen(
                onDismiss = { navController.popBackStack() },
                onCreate = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.GroupDetail.route,
            arguments = listOf(navArgument("meetingJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val meetingJson = backStackEntry.arguments?.getString("meetingJson")
            val gson = Gson()
            val decodedMeetingJson =
                URLDecoder.decode(meetingJson, StandardCharsets.UTF_8.toString())
            val meeting = gson.fromJson(decodedMeetingJson, Meeting::class.java)
            GroupDetail(navController, meeting)
        }

        composable(
            route = Routes.TimeSheet.route + "?scheduleId={scheduleId}",
            arguments = listOf(
                navArgument("scheduleId") {
                    type = NavType.IntType
                }
            )
        ) {
            it.arguments?.getInt("scheduleId")?.let { it1 ->
                TimeSheet(
                    navController,
                    scheduleViewModel,
                    userTimeViewModel,
                    fixedScheduleViewModel,
                    scheduleId = it1,
                )
            }
        }

        composable(
            route = Routes.TimeInput.route + "?scheduleId={scheduleId}",
            arguments = listOf(
                navArgument("scheduleId") {
                    type = NavType.IntType
                }
            )
        ) {
            it.arguments?.getInt("scheduleId")?.let { it1 ->
                TimeInput(
                    navController,
                    scheduleViewModel,
                    userTimeViewModel,
                    scheduleId = it1,
                )
            }
        }

        composable(Routes.LaunchPage.route) {
            LaunchPage(navController)
        }

        composable(Routes.SignIn.route) {
            SignIn(navController)
        }

        composable(Routes.JoinMembership.route) {
            JoinMembership(navController)
        }

        composable(Routes.CalendarBottomSheet.route) {
            CalendarBottomSheet(navController)
        }

        composable(Routes.TimePicker.route) {
            TimePicker(navController)
        }

        composable(
            route = Routes.ScheduleBottomSheet.route + "?scheduleId={scheduleId}",
            arguments = listOf(
                navArgument("scheduleId") {
                    type = NavType.IntType
                }
            )
        ) {
            it.arguments?.getInt("scheduleId")?.let { it1 ->
                ScheduleBottomSheet(navController, fixedScheduleViewModel, scheduleId = it1 )
            }
        }
    }
}