import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.moida.component.DrawerContent
import com.example.moida.component.GroupDetailTitle
import com.example.moida.component.GroupItem
import com.example.moida.component.MainCalendar
import com.example.moida.component.TodayItemList
import com.example.moida.model.GroupDetailViewModel
import com.example.moida.model.GroupDetailViewModelFactory
import com.example.moida.model.Meeting
import com.example.moida.screen.MeetingManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun GroupDetail(
    navController: NavHostController,
    meeting: Meeting
) {
    val groupDetailViewModel: GroupDetailViewModel = viewModel(
        factory = GroupDetailViewModelFactory(meeting)
    ) // 각 그룹에 들어올 때마다 넘겨받은 meeting 정보로 뷰모델을 세팅해줌

    val groupInfo by groupDetailViewModel.groupInfo.collectAsState()
    val itemList by groupDetailViewModel.itemList.collectAsState()

    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val todayEvents = itemList.groupBy { LocalDate.parse(it.date, dateFormatter) }
    val todayDate = remember { LocalDate.now() }
    var selectedEvents by remember { mutableStateOf(todayEvents[todayDate].orEmpty()) }
    var title by remember { mutableStateOf("오늘의 일정") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true, // 사이드바 슬라이드로 열고 닫기 가능
        drawerContent = {
            groupInfo?.let {
                DrawerContent(
                    group = it,
                    drawerState = drawerState,
                    scope = scope,
                    onDeleteMeeting = { meetingId ->
                        Log.d("GroupDetail", "onDeleteMeeting called with meetingId: $meetingId")
                        val meetingManager = MeetingManager()
                        meetingManager.deleteMeeting(meetingId)
                        navController.popBackStack()
                    },
                    onLeaveMeeting = { meetingId ->
                        Log.d("GroupDetail", "onLeaveMeeting called with meetingId: $meetingId")
                        val meetingManager = MeetingManager()
                        meetingManager.leaveMeeting(meetingId)
                        navController.popBackStack()
                    }
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                groupInfo?.let {
                    GroupDetailTitle(
                        group = it,
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                        , navController = navController)
                }
            }
            item {
                MainCalendar(
                    events = todayEvents,
                    onDateClick = { date ->
                        selectedEvents = todayEvents[date].orEmpty()
                    },
                    updateTitle = { it ->
                        title = it
                    },
                    hasEvents = { date, events ->
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
                        .padding(bottom = 100.dp)
                ) {
                    selectedEvents.forEach { item ->
                        GroupItem(item)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}