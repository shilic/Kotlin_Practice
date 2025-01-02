package com.example.okHttpTest

import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException


class Ok1() {

    private val client = OkHttpClient() // 新建OkHttpClient客户端

    // GET请求（同步）
    fun getExecute(url: String): String {
        val request = Request.Builder().url(url).build() // 新建Request对象
        val mCall: Call = client.newCall(request)
        val response = mCall.execute() // Response为OkHttp中的响应
        return response.body?.string() ?: ""
    }

    // POST请求（同步）
    private val json = "application/json; charset=utf-8".toMediaType()
    fun postExecute(url: String, json: String): String {
        val body = RequestBody.create(this.json, json)
        val request = Request.Builder().url(url).post(body).build()
        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }

    // GET请求（异步）
    fun runGetEnqueue() {
        println("尝试 GET请求（异步）")
        val uri1 = "http://publicobject.com/helloworld.txt"
        val uri2 = "https://jsonplaceholder.typicode.com/users"
        val request = Request.Builder()
            .url(uri1)
            .get()
            .build()

        val mCall: Call = client.newCall(request)
        //val responseCallback : Callback
        mCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("GET请求（异步）请求失败")
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                println("GET请求（异步）请求成功")
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("OK$name: $value")
                    }
                    println("OK"+response.body!!.string())
                }
            }
        })
    }

    // POST请求（异步）
    fun runPostEnqueue() {
        val formBody = FormBody.Builder()
            .add("search", "Jurassic Park")
            .build()
        val request = Request.Builder()
            .url("https://en.wikipedia.org/w/index.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body!!.string())
                }
            }
        })
    }
}