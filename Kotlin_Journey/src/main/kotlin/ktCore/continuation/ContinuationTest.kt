package ktCore.continuation

import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.*
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds


class ContinuationTest {
    suspend fun suspend1(a : Int) : Int = suspendCancellableCoroutine<Int> { continuation ->
        println("In suspendCoroutine")
        thread {
            //Thread.sleep(1000)
            continuation.resume(a + 1)
        }

    }
    suspend fun suspend2(a : Int) : Int {
        delay(1000.milliseconds)
        println("In suspendCoroutine")
        return  a + 2
    }

    fun test1() {
        val continuation1 : Continuation<Unit> = suspend {
            //delay(1000.milliseconds) // ✅ 完全可以加在这里
            println("In continuation1")
            suspend1(2)
        }.createCoroutine(object : Continuation<Int> {
            override val context: CoroutineContext get() = EmptyCoroutineContext

            override fun resumeWith(result: Result<Int>) {
                println("End continuation1 : $result")
            }
        })
        continuation1.resume(Unit)

        val continuation2: Continuation<Unit> = suspend {
            println("In continuation2")
            10
        }.createCoroutine( Continuation(EmptyCoroutineContext) {
            println("End continuation2 : $it")
        })
        continuation2.resume(Unit)

    }

    fun test2() = runBlocking {
        val deferred : Deferred<Int> = async<Int> {
            2
        }
        // JobSupport
        println("deferred.await() = ${Await of deferred}")
        val job: Job = launch(Dispatchers.Default) {
            // 有delay函数，死循环也可以取消；没有，则无法取消；故，使用 isActive 检查才是正解
            while (isActive) {
                // ← 这是挂起点，也是取消检查点
                delay(100.milliseconds)
                ensureActive()
                println("In job")
            }
            // 这里抛不抛异常都会取消
//            if (!isActive) {
//                throw CancellationException()
//            }
        }
        delay(1200.milliseconds)
        job.cancel()
        Await of job
    }
    suspend fun test3() {
        val job = coroutineScope {
            launch(Dispatchers.Default) {
                delay(1000.milliseconds)
                println("In job3")
            }
        }
    }
    suspend fun test6() {
        val job = supervisorScope {
            launch(Dispatchers.Default) {
                delay(1000.milliseconds)
                println("In job")
            }
        }
    }
    fun test4() {
        val job = runBlocking {
            launch(Dispatchers.Default) {
                delay(1000.milliseconds)
                println("In job")
            }
        }
    }
    fun test5() {
        val job = GlobalScope.launch(Dispatchers.Default) {
            delay(1000.milliseconds)
            println("In job")
        }
    }
    fun test7() {
        val scope = MainScope()
        val job = scope.launch(Dispatchers.Default) {
            delay(1000.milliseconds)
            println("In job")
        }
    }


}

fun main() = runBlocking {
    ContinuationTest().test2()
}

object Await
suspend infix fun <T> Await.of(deferred: Deferred<T>): T = deferred.await()
suspend infix fun Await.of(job: Job): Unit = job.join()
