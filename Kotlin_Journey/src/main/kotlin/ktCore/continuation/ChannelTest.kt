package ktCore.continuation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope


suspend fun channelTest1() = coroutineScope {
    val channel = Channel<Int>(1)
}

fun main() {

}