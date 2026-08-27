package ktCore.continuation

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.milliseconds


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

fun main(): Unit = runBlocking {
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
}

sealed class UiState {
    data object Loading: UiState()
    data class Success(val data: String): UiState()
    data class Error(val message: String): UiState()
}
