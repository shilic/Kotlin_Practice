package ktCore.continuation

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.random.Random


@OptIn(ExperimentalCoroutinesApi::class)
suspend fun fanOut() = coroutineScope {
    // 使用 produce 创建一个生产者
    val receiveChannel : ReceiveChannel<Int> = produce<Int> {
        repeat(10) {
            send(it)
        }
        // 生产者只能发
        // receive()
    }
    // 多消费者，但是一个元素只会被一个消费者消费。
    repeat(3) { id ->
        launch {
            for (msg in receiveChannel) {
                println("Consumer #$id got $msg")
            }
        }
    }
}

@OptIn(ObsoleteCoroutinesApi::class)
suspend fun fanIn() = coroutineScope {
    // 使用 actor 创建一个消费者
    val sendChannel : SendChannel<String> = actor<String> {
        for (msg in channel) {
            println(msg)
        }
    }
    //val channel = Channel<String>()
    // 创建3个协程进行数据的发送
    repeat(3) { id ->
        launch {
            while (true) {
                delay(Random.nextLong(100))
                sendChannel.send("from $id")
            }
        }
    }
}

fun main() = runBlocking {
    // val BroadcastChannel: BroadcastChannel
    //fanOut()
    fanIn()
}