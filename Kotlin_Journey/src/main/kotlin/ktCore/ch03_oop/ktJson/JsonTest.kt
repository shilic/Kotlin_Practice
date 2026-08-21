package ktCore.ch03_oop.ktJson

import com.google.gson.Gson

class JsonTest {



}
private val jsonStr :String = """
        {
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
        }

    """.trimIndent()
fun main() {
    val gson = Gson()
    // val 字段也可以反序列化
    val user: User = gson.fromJson(jsonStr, User::class.java)
    println("user: $user")
}