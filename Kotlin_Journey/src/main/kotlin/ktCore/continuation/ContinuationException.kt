package ktCore.continuation

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration.Companion.milliseconds

suspend fun launchExceptionTest() {
    // 这里没有 CoroutineScope，所以不能直接 launch
    // launch { } // ❌


    val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        println("Global handler caught: ${e.message}")
    }
    // 创建了一个临时作用域
    val job : Job = supervisorScope() {
        // ✅ 现在有作用域了

        // launch 异常处理方案1: 使用 supervisorScope + 异常处理器
        val errorJob1 = launch(coroutineExceptionHandler) {
            delay(100.milliseconds)
            error("errorJob1")
        }
        // launch 异常处理方案2 : 在 launch 内部捕获异常
        val errorJob2 = launch() {

            try {
                delay(150.milliseconds)
                error("errorJob2")
            }
            catch (e: Exception) {
                println("Caught in errorJob: ${e.message}")
            }
        }
        // launch 异常处理方案3 ： 直接在 supervisorScope 运行，不会传递到子协程
        val errorJob3 = launch {
            delay(100.milliseconds)
            throw RuntimeException("child1 failed")
        }

        // normalJob1 会正常执行，
        val normalJob1 = launch {
            println("normalJob1")
        }
        // 然后 errorJob 发生异常，后边的 normalJob2 无法执行。
        val normalJob2 = launch {
            delay(200.milliseconds)
            println("normalJob2")
        }
        launch {
            delay(220.milliseconds)
            println("normalJob3")
        }
        // 等待内部所有协程结束
    }
}



fun main() = runBlocking {
    launchExceptionTest()
}