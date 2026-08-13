package ktCore.continuation

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class SharedFlowTest {
    // 创建：无重放，无额外缓冲，丢弃旧事件
    private val events = MutableSharedFlow<MyEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    class MyEvent {

    }

    // 发送事件（非 suspend，但可能阻塞？实际上 SharedFlow 的 emit 是 suspend 的，除非使用 tryEmit）
    suspend fun sendEvent(myEvent: MyEvent) {
        events.emit(myEvent)
    }

    // 或使用 tryEmit 在非挂起上下文中发送
    fun sendEventNonSuspend(myEvent: MyEvent) {
        events.tryEmit(myEvent)
    }

    fun collectEvent() {
        val viewModelScope = GlobalScope
        // 收集事件， 在 viewModel 中
        viewModelScope.launch {
            events.collect { event ->
                // 处理事件
            }
        }
    }
}