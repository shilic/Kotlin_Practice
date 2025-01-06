package com.example.my.myAPI

import com.example.my.myModel.Model1
import retrofit2.Call
import retrofit2.http.GET

public interface IRequest {
    @GET("users") // users
    fun getCall(): Call<List<Model1>>

}