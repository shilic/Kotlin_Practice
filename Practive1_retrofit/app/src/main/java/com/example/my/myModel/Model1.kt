package com.example.my.myModel
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
import com.google.gson.annotations.SerializedName

class Model1 {
    val id: Int? = null
    val name: String? = null
    val username: String? = null
    val email: String? = null
    val address: Address? = null
    val phone: String? = null
    val website: String? = null
    val company: Company? = null
    override fun toString(): String {
        return "\n{ id=$id, name=$name, username=$username, email=$email" +
                "\n  address=$address, \nphone=$phone, website=$website, company=$company" +
                "}\n"
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