package com.example.my

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.my.myService.OkhttpService
import com.example.my.myService.RetrofitService
import com.example.my.okHttpTest.Ok1
import com.example.myretrofit.R

/*一个大问号放在这里。不同的编译环境，结果截然不同。JUnit测试环境下，都能成功。而安卓模拟器却一直提示我网络请求失败。
* 直到我连了手机调试之后，网络请求却成功了。很奇怪的bug
* */
class MainActivity : AppCompatActivity() {
    private val tag : String = "MainActivity"
    private var ifBind = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //okhttpTest()
        retrofitTest()
    }
    private val retrofitConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as RetrofitService.IBinder
            val retrofitService = binder.getService()
            ifBind = true
            // 调用服务中的方法
            val message = retrofitService.getMessage()
            Log.d(tag, message)
            retrofitService.getResult()
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            ifBind = false
        }
    }
    private val okhttpConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as OkhttpService.IBinder
            val okhttpService = binder.getService()
            ifBind = true
            // 调用服务中的方法
            val message = okhttpService.getMessage()
            Log.d(tag, message)
            okhttpService.runGetEnqueue()
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            ifBind = false
        }
    }
    private fun okhttpTest(){
        val okhttpIntent  = Intent(this, OkhttpService::class.java)
        bindService(okhttpIntent,okhttpConnection, Context.BIND_AUTO_CREATE)
    }
    private fun retrofitTest(){
        val retrofitIntent  = Intent(this,RetrofitService::class.java)
        bindService(retrofitIntent,retrofitConnection, Context.BIND_AUTO_CREATE)
    }
    private fun okhttpTest2(){
        val ok1 = Ok1()
        ok1.runGetEnqueue()
        // 因为单元测试的缘故，程序到这里就会终止，故增加休眠，等待 callback
    }

}