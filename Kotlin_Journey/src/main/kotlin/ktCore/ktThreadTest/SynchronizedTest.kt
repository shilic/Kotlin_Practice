package ktCore.ktThreadTest

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class Counter {
    private var count = 0

    fun increment() {
        synchronized(this) {
            count++
        }
    }

    fun getCount(): Int {
        synchronized(this) {
            return count
        }
    }


    fun lockTest(){
        // 1. ReentrantLock
        val lock = ReentrantLock()
        lock.withLock {

        }
        val condition = lock.newCondition()
        fun update() {
            lock.lock()
            try {
                // 临界区
            } finally {
                lock.unlock()
            }
        }
    }
}

fun main() {
    val counter = Counter()

    // Thread 1
    Thread {
        for (i in 1..1000) {
            counter.increment()
        }
    }.start()

    // Thread 2
    Thread {
        for (i in 1..1000) {
            counter.increment()
        }
    }.start()

    Thread.sleep(1000) // 等待线程完成

    println("Final count: ${counter.getCount()}") // 输出: Final count: 2000
}