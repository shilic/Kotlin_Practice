package ktCore.continuation

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.milliseconds

/** 消费流必须在挂起函数 */
suspend fun flowTest() {
    // Flow 是冷流：每次 collect 都会重新执行 lambda
    val coldFlow = coldFlow1().flowOn(Dispatchers.Default).onCompletion { cause ->

    }
    // 第一次收集
    coldFlow.collect { println(it) } // 输出 A, B
    // 第二次收集会再次从头执行
    coldFlow.collect { println(it) } // 再次输出 A, B
}
/** 创建流可以在任意函数 */
fun coldFlow1(): Flow<String> = flow {
    emit("A")
    delay(100.milliseconds)
    emit("B")
}

fun backPress() {
    val bufferFlow = coldFlow1().buffer()
    val conflatedFlow = coldFlow1().conflate()
    
    val latestFlow = runBlocking {
        coldFlow1().collectLatest(
            action = {

            }
        )
    }
}


fun main() {

}