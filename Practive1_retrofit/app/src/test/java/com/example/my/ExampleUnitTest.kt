package com.example.my

import com.example.my.myModel.Model1
import com.example.my.okHttpTest.Ok1
import com.google.gson.Gson
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
    @Test
    fun jsonTest(): Unit {
        val json = " {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Leanne Graham\",\n" +
                "        \"username\": \"Bret\",\n" +
                "        \"email\": \"Sincere@april.biz\",\n" +
                "        \"address\": {\n" +
                "          \"street\": \"Kulas Light\",\n" +
                "          \"suite\": \"Apt. 556\",\n" +
                "          \"city\": \"Gwenborough\",\n" +
                "          \"zipcode\": \"92998-3874\",\n" +
                "          \"geo\": {\n" +
                "            \"lat\": \"-37.3159\",\n" +
                "            \"lng\": \"81.1496\"\n" +
                "           }\n" +
                "        },\n" +
                "         \"phone\": \"1-770-736-8031 x56442\",\n" +
                "          \"website\": \"hildegard.org\",\n" +
                "            \"company\": {\n" +
                "            \"name\": \"Romaguera-Crona\",\n" +
                "            \"catchPhrase\": \"Multi-layered client-server neural-net\",\n" +
                "            \"bs\": \"harness real-time e-markets\"\n" +
                "         }\n" +
                "        }"
        val gson = Gson()
        val model1 : Model1 = gson.fromJson(json, Model1::class.java) // 单个对象
        println("json串解析测试 ： $model1")
    }


}