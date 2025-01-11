package com.example.my.manager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    //创建一个retrofit对象
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)//标识所有retrofit请求的跟路径
        .addConverterFactory(GsonConverterFactory.create())//标识retrofit在解析数据时所使用的转换库
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

}