package com.example.my

//import com.example.my.myService.OkhttpService
//import com.example.my.myService.RetrofitService
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.my.myPage.myFrag.userInfo.UserInfoFrag
import com.example.myretrofit.R

/*一个大问号放在这里。不同的编译环境，结果截然不同。JUnit测试环境下，都能成功。而安卓模拟器却一直提示我网络请求失败。
* 直到我连了手机调试之后，网络请求却成功了。很奇怪的bug
* */
class MainActivity : AppCompatActivity() {
    private val logTag: String = "主活动"
    private var ifBind = false
    val userInfoFrag = UserInfoFrag()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUserInfoList()
    }

    private fun initUserInfoList() {
        val fragManager = supportFragmentManager
        fragManager.commit {
            replace(R.id.ac1_container, userInfoFrag, userInfoFrag.logTag)
        }
    }

}