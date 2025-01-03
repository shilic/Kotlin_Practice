package com.example.myRetrofit.myModel

class Model1 {
    var id : Int? = null
    var name : String = ""
    var username : String? = null
    var email : String? = null

    override fun toString(): String {
        return "{id = $id ,\nname = $name,\nusername = $username," +
                "\nemail = $email" +
                "}"
    }
}