//package com.example.my.myService
//
//import android.app.Service
//import android.content.Intent
//import android.os.Binder
//import android.util.Log
//import com.example.my.myAPI.IRequest
//import com.example.my.myModel.User
//import com.example.my.myModel.UserListViewModel
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//
///**
// * Retrofit 网络请求服务
// */
//class RetrofitService : Service() {
//    private val tag :String = "Retrofit-请求服务"
//    private val binder : Binder = IBinder()
//    private var call : Call<List<User>>? = null
//    private val oK = 1
//    private lateinit var mViewModel : UserListViewModel
//    override fun onCreate() {
//        create()
//        super.onCreate()
//    }
//    fun create(){
//        println("RetrofitService服务创建成功")
//        val retrofit : Retrofit? = Retrofit.Builder()
//            .baseUrl("https://jsonplaceholder.typicode.com/") // 设置网络请求的Url地址  "https://jsonplaceholder.typicode.com/users"
//            .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
//            //.addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
//            .build()
//        // 创建 网络请求接口 的实例
//        val request: IRequest = retrofit!!.create(IRequest::class.java);
//        //对 发送请求 进行封装
//        call = request.getCall()
//    }
//    fun registerViewModel(viewModel: UserListViewModel){
//        mViewModel = viewModel
//    }
//    /**
//     * 异步获取用户数据
//     */
//    fun getUser() {
//        // 由于数据的获取是在子线程中获取的。故主线程无法等待返回值并进行数据的刷新，只能通过异步的方式刷新页面数据。 也就是通过Handler; 或者共享内存, 如ViewModel.
//        call?.enqueue(object : Callback<List<User>>{
//            //请求成功时回调
//            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
//                Log.d(tag,"Yes! Retrofit 发起远程http请求成功")
//                //请求处理,输出结果
//                val modelList : List<User>? = response.body()
//                if (modelList != null) {
////                    for (item in modelList) {
////                        Log.d(tag,"retrofit下的json串解析测试 ： $item")
////                    }
//                    Log.d(tag," Retrofit 成功获取到数据，尝试刷新 ViewModel 层数据")
//                    // 请求刷新数据
//                    mViewModel.updateUsers(modelList)
//                }
//                else{
//                    Log.d(tag,"retrofit下的modelList == null")
//                }
//            }
//            //请求失败时候的回调
//            override fun onFailure(call: Call<List<User>>, throwable: Throwable) {
//                Log.d(tag,"No! Retrofit 发起远程http请求失败")
//            }
//        })
//    } //getUser 异步获取用户数据
//    override fun onBind(intent: Intent): android.os.IBinder {
//        return binder
//    }
//    fun getMessage(): String {
//        return "这是一条 RetrofitService 的消息，证明绑定服务成功了"
//    }
//    // 内部类，用于返回当前服务的实例
//    inner class IBinder : Binder() {
//        fun getService(): RetrofitService = this@RetrofitService
//    }
//    override fun onUnbind(intent: Intent?): Boolean {
//        return super.onUnbind(intent)
//    }
//}