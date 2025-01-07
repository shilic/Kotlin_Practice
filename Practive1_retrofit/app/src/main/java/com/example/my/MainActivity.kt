package com.example.my

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.my.myPage.myFrag.userInfo.UserInfoFrag
import com.example.my.myService.OkhttpService
import com.example.my.myService.RetrofitService
import com.example.myretrofit.R

/*一个大问号放在这里。不同的编译环境，结果截然不同。JUnit测试环境下，都能成功。而安卓模拟器却一直提示我网络请求失败。
* 直到我连了手机调试之后，网络请求却成功了。很奇怪的bug
* */
class MainActivity : AppCompatActivity() {
    private val logTag : String = "主活动"
    private var ifBind = false
    val userInfoFrag = UserInfoFrag()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUserInfoList()
        retrofitTest()
    }

    private fun initUserInfoList(){
        val fragManager = supportFragmentManager
        fragManager.commit {
            replace(R.id.ac1_container,userInfoFrag,userInfoFrag.logTag)
        }
    }
    private fun retrofitTest(){
        val retrofitIntent  = Intent(this,RetrofitService::class.java)
        bindService(retrofitIntent,retrofitConnection, Context.BIND_AUTO_CREATE)
    }
    private val retrofitConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as RetrofitService.IBinder
            val retrofitService = binder.getService()
            ifBind = true
            // 调用服务中的方法
            val message = retrofitService.getMessage()
            Log.d(logTag, message)
            retrofitService.registerViewModel(userInfoFrag.viewModel)
            retrofitService.getUser() // 拿到最新数据
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
            Log.d(logTag, message)
            okhttpService.runGetEnqueue()
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            ifBind = false
        }
    }
    @Deprecated("okhttp暂时弃用")
    private fun okhttpTest(){
        val okhttpIntent  = Intent(this, OkhttpService::class.java)
        bindService(okhttpIntent,okhttpConnection, Context.BIND_AUTO_CREATE)
    }
}