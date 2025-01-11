package com.example.my.myService

import com.example.my.bean.UserBean
import retrofit2.Call
import retrofit2.http.GET

interface NetworkService {
    @GET("users")
    fun getUsers( ): Call<List<UserBean>>

}