package com.example.myRetrofit

import com.example.okHttpTest.Ok1
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    private val tag : String = "test";
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun test1(): Unit {
        println("\n\n hello kotlin\n\n ")
    }
    @Test
    fun okTest1(): Unit {
        val ok1 = Ok1()
        ok1.runGetEnqueue()
        // 因为单元测试的缘故，程序到这里就会终止，故增加休眠，等待 callback
        Thread.sleep(5000)
    }

}