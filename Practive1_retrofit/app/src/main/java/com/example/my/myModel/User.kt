package com.example.my.myModel

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

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
/**
 * 用户数据模型
 */
class User {
    val id: Int? = null
    val name: String? = null
    val username: String? = null
    val email: String? = null
    val address: Address? = null
    val phone: String? = null
    val website: String? = null
    val company: Company? = null
    //此处踩坑，定义数据类，不可以像 javascript 一样使用lambda表达式。
    override fun toString(): String {
        return "\n{ id=$id, name=$name, username=$username, email=$email" +
                "\n  address=$address, \nphone=$phone, website=$website, company=$company" +
                "}\n"
    }
    companion object {
        fun getDefault(): List<User> {
            val json = " [{\n" +
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
                    "        }]"
            val gson = Gson()
            return gson.fromJson(json, object : TypeToken<List<User>>() {}.type) // 多个对象
        }
    }
}
class Address {
    @SerializedName("street")
    val street: String? = null
    @SerializedName("suite")
    val suite: String? = null
    @SerializedName("city")
    val city: String? = null
    @SerializedName("zipcode")
    val zipcode: String? = null
    val geo: Geo? = null
    override fun toString(): String {
        return "\n{ street=$street, suite=$suite, city=$city, zipcode=$zipcode, geo=$geo" +
                "}\n"
    }
}
class Geo {
    @SerializedName("lat")
    val lat: String? = null
    @SerializedName("lng")
    val lng: String? = null
    override fun toString(): String {
        return "\n{ lat=$lat, lng=$lng" +
                "}\n"
    }
}
class Company {
    @SerializedName("name")
    val name: String? = null
    @SerializedName("catchPhrase")
    val catchPhrase: String? = null
    @SerializedName("bs")
    val bs: String? = null
    override fun toString(): String {
        return "\n{ name=$name, catchPhrase=$catchPhrase, bs=$bs" +
                "}\n"
    }
}