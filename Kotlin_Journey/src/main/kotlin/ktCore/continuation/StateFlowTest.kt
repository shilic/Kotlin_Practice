package ktCore.continuation

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import okhttp3.internal.wait
import kotlin.time.Duration.Companion.milliseconds

sealed class UiState {
    data object Loading: UiState()
    data class Success(val data: String): UiState()
    data class Error(val message: String): UiState()
}
private data class UiState2(val count: Int = 0) {
}

class StateFlowTest {
    // 定义
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // 更新状态（在 ViewModel 或其他协程中）
    fun updateData(data: String) {
        _uiState.value = UiState.Success(data)
    }

    // 在 Compose 中收集
    // val state by viewModel.uiState.collectAsState()
}

suspend fun collectTest() {
    val uiState = MutableStateFlow<UiState>(UiState.Loading)
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    // 状态的收集必须在协程中
    scope.launch {
        uiState.collect {
            println("Collected1 : $it")
        }
    }
    // 可以有多个收集者
    scope.launch {
        uiState.collect {
            println("Collected2 : $it")
        }
    }
    // 在 Compose 中收集
    // val state by uiState.collectAsState()
    repeat(10) {
        uiState.value = UiState.Success(it.toString())
        delay(1000.milliseconds)
    }
    scope.cancel()
}

suspend fun badCollect() {
    val uiState = MutableStateFlow<UiState2>(UiState2())
    // 每次都出新值 → 无限循环
    uiState.collect { state ->
        delay(100.milliseconds)
        // ❌ 危险的写法，在收集函数中修改状态，导致无限循环
        uiState.value = state.copy(count = state.count + 1)
        println("oldState : $state, newState : ${uiState.value}")
    }
}


fun main(): Unit = runBlocking {
//    collectTest()
    badCollect()
}


