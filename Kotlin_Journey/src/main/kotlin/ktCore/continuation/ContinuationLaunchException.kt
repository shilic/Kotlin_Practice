package ktCore.continuation

import kotlinx.coroutines.*

import kotlin.time.Duration.Companion.milliseconds

suspend fun launchSupervisorScopeExceptionTest() {
    // 这里没有 CoroutineScope，所以不能直接 launch
    // launch { } // ❌



    // 1. supervisorScope 中的异常
    val supervisorJob : Job = supervisorScope {
        val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            println("Global handler caught: ${e.message}")
        }

        // launch 异常处理方案1: 使用 supervisorScope + 异常处理器
        val errorJob1 = launch(coroutineExceptionHandler) {
            delay(100.milliseconds)
            error("errorJob1")
        }
        // launch 异常处理方案2 : 在 launch 内部捕获异常， 嵌套一层 coroutineScope，会被外部 try - catch 捕获;
        val errorJob2 = launch {
            try {
                // 嵌套一层 coroutineScope ；异步代码的异常，则会被外部 try - catch 捕获;
                coroutineScope {  // 🛑 挂起！等所有子协程结束
                    launch {
                        delay(10.milliseconds)
                        error("errorJob2")  // 子协程异常 → coroutineScope 接住 → 向上重新抛出
                    }
                }
            }
            catch (e: Exception) {
                println("Caught in errorJob2: ${e.message}")
            }
        }
        // launch 异常处理方案2 : 在 launch 内部捕获异常，直接嵌套launch， 无法捕获异步代码的异常 ❌
        val errorJob3 = launch {
            /* try 块只包裹了"发射 launch"这个瞬间动作，try-catch 执行完后，被发射的那个协程才抛出异常。异常的传播路径是协程的 Job 层级，不是调用栈。 */
            try {
                // 直接嵌套 launch 外部 try - catch 无法捕获(不是万能的)；只能被最外层的 supervisorScope 拦截
                launch {  // 立即返回 Job，继续往下走
                    delay(150.milliseconds)
                    error("errorJob3") // 此时 try 块早已执行完毕！
                }
            }
            catch (e: Exception) {
                println("Caught in errorJob3: ${e.message}")
            }
        }
        // 子任务直接在 supervisorScope 抛出异常，不会传递到子协程；被最外层的 supervisorScope 拦截。
        val errorJob4 = launch {
            delay(100.milliseconds)
            throw RuntimeException("errorJob4")
        }



        // normalJob1 会正常执行，
        val normalJob1 = launch {
            println("normalJob1")
        }
        // 然后 errorJob1 发生异常，如果不捕获，后边的 normalJob2 无法执行。
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
suspend fun launchGlobalScopeExceptionTest2() {
    val globalJob = GlobalScope.launch(Dispatchers.Default) {
        val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            println("Global handler caught: ${e.message}")
        }

        // launch 异常处理方案1: 使用 GlobalScope + 异常处理器
        val errorJob1 = GlobalScope.launch(coroutineExceptionHandler) {
            delay(100.milliseconds)
            error("errorJob1")
        }
        // launch 异常处理方案2 : 在 launch 内部捕获异常
        val errorJob2 = GlobalScope.launch() {
            try {
                delay(150.milliseconds)
                error("errorJob2")
            }
            catch (e: Exception) {
                println("Caught in errorJob: ${e.message}")
            }
        }
        // 使用 GlobalScope，则每一个异常都需要全局处理。
        val errorJob3 = GlobalScope.launch(coroutineExceptionHandler) {
            delay(100.milliseconds)
            throw RuntimeException("child1 failed")
        }

        // normalJob1 会正常执行，
        val normalJob1 = launch {
            println("normalJob1")
        }
        // 然后 errorJob 发生异常，如果不捕获，后边的 normalJob2 无法执行。
        val normalJob2 = launch {
            delay(200.milliseconds)
            println("normalJob2")
        }
        launch {
            delay(220.milliseconds)
            println("normalJob3")
        }
    }
    Await of globalJob
}

suspend fun launchRunBlockingExceptionTest3() {
    val runBlockingJob : Job = runBlocking {
        val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            println("Global handler caught: ${e.message}")
        }

        // launch 异常处理方案1: 使用 异常处理器
        val errorJob1 =  launch(coroutineExceptionHandler) {
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
                println("Caught in errorJob2: ${e.message}")
            }
        }
        // launch 异常处理方案3 ：
//        val errorJob3 = launch {
//            delay(100.milliseconds)
//            throw RuntimeException("child1 failed")
//        }

        // normalJob1 会正常执行，
        val normalJob1 = launch {
            println("normalJob1")
        }
        // 然后 errorJob 发生异常，如果不捕获，后边的 normalJob2 无法执行。
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
    runBlockingJob.join()
}

suspend fun launchCoroutineScopeExceptionTest4() {
    val coroutineScopeJob : Job = coroutineScope {
        val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            println("Global handler caught: ${e.message}")
        }

        // launch 异常处理方案1: 使用 supervisorScope + 异常处理器
//        val errorJob1 = launch(coroutineExceptionHandler) {
//            delay(100.milliseconds)
//            error("errorJob1")
//        }
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
//        val errorJob3 = launch {
//            delay(100.milliseconds)
//            throw RuntimeException("child1 failed")
//        }

        // normalJob1 会正常执行，
        val normalJob1 = launch {
            println("normalJob1")
        }
        // 然后 errorJob 发生异常，如果不捕获，后边的 normalJob2 无法执行。
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
    coroutineScopeJob.join()
}



fun main() = runBlocking {
     launchSupervisorScopeExceptionTest()
    // launchGlobalScopeExceptionTest2()
    //    launchRunBlockingExceptionTest3()
    // launchCoroutineScopeExceptionTest4()
}