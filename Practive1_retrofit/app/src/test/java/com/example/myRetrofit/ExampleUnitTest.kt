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
        /*
        *  {
        "id": 1,
        "name": "Leanne Graham",
        "username": "Bret",
        "email": "Sincere@april.biz",
        "address": {
          "street": "Kulas Light",
          "suite": "Apt. 556",
          "city": "Gwenborough",
          "zipcode": "92998-3874",
          "geo": {
            "lat": "-37.3159",
            "lng": "81.1496"
           }
        },
         "phone": "1-770-736-8031 x56442",
          "website": "hildegard.org",
            "company": {
            "name": "Romaguera-Crona",
            "catchPhrase": "Multi-layered client-server neural-net",
            "bs": "harness real-time e-markets"
         }
        },
        *
        * */
    }


}