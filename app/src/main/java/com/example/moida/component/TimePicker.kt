package com.example.moida.component

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.moida.R
import com.example.moida.ui.theme.Pretendard
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

@Composable
fun TimePicker(
    navController: NavHostController,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val hours = remember { (0..23).map {it.toString()}}
            val minutes = remember { (0..59).map {it.toString()}}
            val units = remember { listOf("오전", "오후") }

            val hoursPickerState = rememberPickerState()
            val minutesPickerState = rememberPickerState()
            val unitsPickerState = rememberPickerState()

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically) {
                Picker(
                    state = hoursPickerState,
                    items = hours,
                    visibleItemsCount = 3,
                    modifier = Modifier.weight(1f),
                    textModifier = Modifier.padding(18.dp),
                    startIndex = LocalDateTime.now().hour,
                    textStyle = TextStyle(
                        fontFamily = Pretendard,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.padding(14.dp))
                Picker(
                    state = minutesPickerState,
                    items = minutes,
                    visibleItemsCount = 3,
                    modifier = Modifier.weight(1f),
                    textModifier = Modifier.padding(18.dp),
                    startIndex = LocalDateTime.now().minute,
                    textStyle = TextStyle(
                        fontFamily = Pretendard,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.padding(14.dp))
                Picker(
                    state = unitsPickerState,
                    items = units,
                    visibleItemsCount = 3,
                    modifier = Modifier.weight(1f),
                    textModifier = Modifier.padding(18.dp),
                    startIndex = if (LocalDateTime.now().hour < 12) 0 else 1,
                    textStyle = TextStyle(
                        fontFamily = Pretendard,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            Button(
                onClick = {
                    var finalHour = ""
                    //onDismiss()값 넘기기
                    if (unitsPickerState.selectedItem == "오전") {
                        if (hoursPickerState.selectedItem.toInt() > 12) {
                            finalHour = (hoursPickerState.selectedItem.toInt() - 12).toString()
                        } else {
                            finalHour = hoursPickerState.selectedItem
                        }
                        if (finalHour.length == 1) finalHour = "0$finalHour"
                    } else {
                        if (hoursPickerState.selectedItem.toInt() < 12) {
                            finalHour = (hoursPickerState.selectedItem.toInt() + 12).toString()
                        } else {
                            finalHour = hoursPickerState.selectedItem
                        }
                    }
                    val selectedHour = "${finalHour}:${minutesPickerState.selectedItem}"
                    selectedHour?.let { time->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selectedTime", time)
                        navController.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (true) {
                        colorResource(id = R.color.main_blue)
                    } else colorResource(id = R.color.gray_200)
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "적용하기",
                    color = Color.White,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Composable
fun Picker(
    items: List<String>,
    state: PickerState,
    modifier: Modifier = Modifier,
    startIndex: Int,
    visibleItemsCount: Int,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = Color.LightGray
){
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.value)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item}
    }

    Box(modifier = modifier) {

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                Text(
                    text = getItem(index),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle,
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.value = size.height }
                        .then(textModifier)
                )
            }
        }
        Divider(
            color = dividerColor,
            modifier = Modifier.offset(y = itemHeightDp * visibleItemsMiddle)
        )
        Divider(
            color = dividerColor,
            modifier = Modifier.offset(y = itemHeightDp * (visibleItemsMiddle + 1))
        )
    }
}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf("")
}