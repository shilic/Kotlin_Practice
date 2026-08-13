package ktCore.continuation

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*


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

sealed class UiState {
    data object Loading: UiState()
    companion object {
        fun Success(data: String): UiState {
            return Loading
        }
    }

}
