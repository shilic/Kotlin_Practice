package ktCore.continuation

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

suspend fun asyncSupervisorScopeExceptionTest() {
    // 1. supervisorScope 中的异常
    val supervisorJob : Job = supervisorScope {
        val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            println("Global handler caught: ${e.message}")
        }

        // async 异常处理方案1: 使用 supervisorScope + 异常处理器
        // 对 async 启动的协程使用 全局处理器将会无效
        val errorJob1 = async(coroutineExceptionHandler) {
            delay(100.milliseconds)
            1
            error("errorJob1")
        }
        try {
            errorJob1.await()
        }
        catch (e : Exception) {
            println("Caught in errorJob: ${e.message}")
        }
        // async 异常处理方案2 : 在 内部捕获异常
        val errorJob2 = async {
            try {
                delay(50.milliseconds)
                error("errorJob2")
            }
            catch (e: Exception) {
                println("Caught in errorJob: ${e.message}")
            }
            1
        }
        // async 异常处理方案3 ：
        val errorJob3 = async {
            delay(100.milliseconds)
            1
            throw RuntimeException("errorJob3 failed")
        }
        try {
            errorJob3.await()
        }catch (e : Exception) {
            println("Caught in errorJob3: ${e.message}")
        }

        // normalJob1 会正常执行，
        val normalJob1 = async {
            println("normalJob1")
            1
        }

        val normalJob2 = async {
            delay(200.milliseconds)
            println("normalJob2")
            1
        }
        async {
            delay(220.milliseconds)
            println("normalJob3")
            1
        }
        // 等待内部所有协程结束
    }


}
suspend fun asyncGlobalScopeExceptionTest2() {
    val globalJob = GlobalScope.async(Dispatchers.Default) {
        val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            println("Global handler caught: ${e.message}")
        }

        // async 异常处理方案1: 使用 GlobalScope + 异常处理器
        val errorJob1 = GlobalScope.async(coroutineExceptionHandler) {
            delay(100.milliseconds)
            error("errorJob1")
        }
        try {
            errorJob1.await()
        }
        catch (e: Exception) {
            println("Caught in errorJob: ${e.message}")
        }
        // async 异常处理方案2 : 在 launch 内部捕获异常
        val errorJob2 = GlobalScope.async() {
            try {
                delay(150.milliseconds)
                error("errorJob2")
            }
            catch (e: Exception) {
                println("Caught in errorJob: ${e.message}")
            }
            2
        }
        // 使用 GlobalScope，则每一个异常都需要全局处理。
        val errorJob3 = GlobalScope.async(coroutineExceptionHandler) {
            delay(100.milliseconds)
            throw RuntimeException("child1 failed")
        }

        // normalJob1 会正常执行，
        val normalJob1 = async {
            println("normalJob1")
            2
        }
        // 然后 errorJob 发生异常，如果不捕获，后边的 normalJob2 无法执行。
        val normalJob2 = async {
            delay(200.milliseconds)
            println("normalJob2")
            2
        }
        async {
            delay(220.milliseconds)
            println("normalJob3")
            2
        }
    }
    Await of globalJob
}

suspend fun asyncRunBlockingExceptionTest3() {
    val runBlockingJob : Job = runBlocking {
        val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            println("Global handler caught: ${e.message}")
        }

        // async 异常处理方案1: 使用 异常处理器
        val errorJob1 =  async(coroutineExceptionHandler) {
            delay(100.milliseconds)
            3
            error("errorJob1")
        }
        // 即便捕获了异常，并打印，但是后边的任务仍然取消了。
        try {
            errorJob1.await()
        }
        catch (e: Exception) {
            println("Caught in errorJob1: ${e.message}")
        }

        // async 异常处理方案2 : 在 launch 内部捕获异常
        val errorJob2 = async() {
            try {
                delay(50.milliseconds)

                3
                error("errorJob2")
            }
            catch (e: Exception) {
                println("Caught in errorJob2: ${e.message}")
            }
        }

        val errorJob3 = async {
            delay(100.milliseconds)
            3
            throw RuntimeException("child1 failed")
        }
        try {
            errorJob3.await()
        }catch (e: Exception) {
            println("Caught in errorJob3: ${e.message}")
        }


        val normalJob1 = async {
            println("normalJob1")
            3
        }
        val normalJob2 = async {
            delay(200.milliseconds)
            println("normalJob2")
            3
        }
        async {
            delay(220.milliseconds)
            println("normalJob3")
            3
        }
        // 等待内部所有协程结束
    }
    runBlockingJob.join()
}

suspend fun asyncRunBlockingExceptionTest5() = runBlocking {
    val failing = async {
        delay(50.milliseconds)
        throw RuntimeException("fail")
    }
    val normal = async {
        delay(100.milliseconds)
        println("normal completed")
    }

    try {
        failing.await()
    } catch (e: Exception) {
        println("Caught: ${e.message}")
    }

    // 等待一段时间，看看 normal 是否还能完成
    delay(200.milliseconds)
    println("Done")
}

fun asyncCatchExceptionTest6() = runBlocking {
    val deferred = launch {
        try {
            coroutineScope {
                launch {
                    delay(100)
                    throw RuntimeException("inner fail1")
                }
//                val failing = async {
//                    delay(50.milliseconds)
//                    throw RuntimeException("inner fail2")
//                }
//                failing.await()
            }
        } catch (e: Exception) {
            println("Outer catch: ${e.message}")
        }
    }
    //deferred.await() // 不会抛出异常，因为外层 try-catch 已捕获
}

suspend fun asyncCoroutineScopeExceptionTest4() {
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
    // asyncSupervisorScopeExceptionTest()
    // asyncGlobalScopeExceptionTest2()
    //asyncRunBlockingExceptionTest3()
    asyncRunBlockingExceptionTest5()
    // asyncCatchExceptionTest6()
}