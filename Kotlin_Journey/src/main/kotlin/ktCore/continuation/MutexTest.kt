package ktCore.continuation

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit

val mutex = Mutex()
var sharedCounter = 0

suspend fun safeIncrement() {
    mutex.withLock {
        delay(1) // 临界区内也可以挂起
        sharedCounter++
    }
}

// 创建信号量，最多允许 3 个协程同时进入临界区
val semaphore = Semaphore(permits = 3)

suspend fun limitedAccess(taskId: Int) {
    semaphore.withPermit {
        println("Task $taskId: start")
        delay(1000) // 模拟耗时操作
        println("Task $taskId: end")
    }
}

fun main() = runBlocking {
    coroutineScope {
        repeat(1000) {
            launch { safeIncrement() }
        }
    }
    println(sharedCounter) // 输出 1000
}