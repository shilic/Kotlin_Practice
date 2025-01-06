package com.example.my.myService

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.util.Log
import com.example.my.myAPI.IRequest
import com.example.my.myModel.Model1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitService : Service() {
    private val tag :String = "RetrofitService"
    private val binder = IBinder()
    private var call : Call<List<Model1>>? = null

    override fun onCreate() {
        create()
        super.onCreate()
    }
    fun create(): Unit {
        println("RetrofitService服务创建成功")
        val retrofit : Retrofit? = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/") // 设置网络请求的Url地址  "https://jsonplaceholder.typicode.com/users"
            .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
            //.addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
            .build()
        // 创建 网络请求接口 的实例
        val request: IRequest = retrofit!!.create(IRequest::class.java);
        //对 发送请求 进行封装
        call = request.getCall()
    }
    public fun getResult(){
        call?.enqueue(object : Callback<List<Model1>>{
            //请求成功时回调
            override fun onResponse(call: Call<List<Model1>>, response: Response<List<Model1>>) {
                println("Yes! Retrofit 发起远程http请求成功")
                //请求处理,输出结果
                val modelList : List<Model1>? = response.body()
                if (modelList != null) {
                    for (item in modelList) {
                        println("retrofit下的json串解析测试 ： $item")
                    }
                }
                else{
                    println("retrofit下的modelList == null")
                }
            }
            //请求失败时候的回调
            override fun onFailure(call: Call<List<Model1>>, throwable: Throwable) {
                println("No! Retrofit 发起远程http请求失败")
            }
        })
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent): android.os.IBinder {
        return binder
    }
    fun getMessage(): String {
        return "这是一条 RetrofitService 的消息，证明绑定服务成功了"
    }
    // 内部类，用于返回当前服务的实例
    inner class IBinder : Binder() {
        fun getService(): RetrofitService = this@RetrofitService
    }
    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }
}