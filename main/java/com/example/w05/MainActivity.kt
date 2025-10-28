package com.example.w05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.w05.ui.theme.MyApplicationTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // TODO: 여기에 카운터와 스톱워치 UI를 만들도록 안내
                val count: MutableState<Int> = remember { mutableStateOf(0) }
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CounterApp(count = count)
                    Spacer(modifier = Modifier.height(32.dp))
                    StopWatchApp()
                }

            }
        }
    }
}


@Composable
fun CounterApp(count: MutableState<Int>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Count: ${count.value}"
        ) // TODO: 상태값 표시
        Row {
            Button(onClick = { count.value++ }) { Text("Increase") }
            Button(onClick = { count.value = 0 }) { Text("Reset") }
        }
    }
}

@Composable
fun StopWatchApp() {
    // 1. 시간(밀리초)과 타이머 실행 여부를 기억할 State 변수 추가
    var timeInMillis by remember { mutableStateOf(1234L) }
    var isRunning by remember { mutableStateOf(false) }
    // 1. isRunning 상태가 true일 때만 실행되는 LaunchedEffect 추가
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                delay(10L) // 10밀리초마다
                timeInMillis += 10L // 시간을 10밀리초씩 증가
            }
        }
    }
    StopwatchScreen(
        timeInMillis = timeInMillis,
        onStartClick = { isRunning = true },
        onStopClick = { isRunning = false },
        onResetClick = {
            isRunning = false
            timeInMillis = 0L
        }
    )
}

@Composable
fun StopwatchScreen(
    timeInMillis: Long, // 3. 상태를 직접 소유하지 않고 파라미터로 받습니다.
    onStartClick: () -> Unit, // 4. 이벤트가 발생했을 때 호출할 람다 함수를 받습니다.
    onStopClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = formatTime(timeInMillis), // 전달받은 상태로 UI를 그립니다.

        )

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            // 5. 버튼 클릭 시, 상태를 직접 변경하는 대신 전달받은 람다 함수를 호출합니다.
            Button(onClick = onStartClick) { Text("Start") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onStopClick) { Text("Stop") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onResetClick) { Text("Reset") }
        }
    }
}


// 시간을 MM:SS:ss 형식으로 변환하는 헬퍼 함수
private fun formatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    val millis = (timeInMillis % 1000) / 10
    return String.format("%02d:%02d:%02d", minutes, seconds, millis)
}



@Preview(showBackground = true)
@Composable
fun CounterAppPreview() {
    val count = remember { mutableStateOf(0) }
    CounterApp(count = count)
}


@Preview(showBackground = true)
@Composable
fun StopWatchPreview() {
    StopWatchApp()
}
