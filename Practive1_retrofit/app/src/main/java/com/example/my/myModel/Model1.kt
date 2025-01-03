package com.example.my.myModel

class Model1() {
    var id : Int? = null
    var name : String = ""
    var username : String? = null
    var email : String? = null

    override fun toString(): String {
        return "\n{"+"id = $id ,\nname = $name,\nusername = $username," +
                "\nemail = $email" + "\n}\n"
    }
}