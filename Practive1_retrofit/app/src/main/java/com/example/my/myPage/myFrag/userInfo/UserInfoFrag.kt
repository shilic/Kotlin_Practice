package com.example.my.myPage.myFrag.userInfo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my.myModel.User
import com.example.my.myModel.UserListViewModel
import com.example.myretrofit.R

class UserInfoFrag : Fragment() {
    /**你遇到的问题是由于 Fragment 类中已经有一个名为 getTag() 的方法，而你在 UserInfoFrag 类中定义了一个名为 tag 的属性。
     * Kotlin 会自动为属性生成一个 getter 方法，因此 tag 属性的 getter 方法与 Fragment 类中的 getTag() 方法冲突了。
     */
    val logTag : String = "用户信息列表-碎片"
    lateinit var viewModel: UserListViewModel
    private lateinit var mAdapter:UserInfoAdapter
    private lateinit var userData: List<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun initUserListViewModel(){
        /* 两者的区别，碎片实现了 ViewModelStoreOwner ，故可以直接获取 ViewModel ; 而碎片没有实现 LifecycleOwner
        ,故需要使用 getViewLifecycleOwner() 获取,添加到observe中 kotlin中则直接简化了步骤*/
        // 获取 ViewModel 实例
        viewModel = ViewModelProvider(this)[UserListViewModel::class.java]
        Log.d(logTag,"ViewModel创建成功")
        // 观察 LiveData
        viewModel.listLiveData.observe(viewLifecycleOwner) { liveData ->
            // 更新 UI
            if (liveData != null) {
                Log.d(logTag,"LiveData 观察到数据更新，数据不为空。尝试通知 RecyclerView 更新数据")
                // 例如，更新 TextView 显示用户信息。例如，这里就是调用循环布局适配器 的更新函数
                userData = liveData
//                for (item in userData) {
//                    Log.d(logTag,"retrofit下的json串解析测试 ： $item")
//                }
                mAdapter.updateData(userData)
            }
            else {
                Log.d(logTag,"LiveData 数据为空")
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(logTag,"开始创建循环视图碎片")
        initUserListViewModel()
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)
        val recycleView : RecyclerView = view.findViewById(R.id.rv_user_info_list_container)
        val linearLayoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        recycleView.layoutManager = linearLayoutManager
        userData = User.getDefault()
        mAdapter = UserInfoAdapter(requireContext(), userData)
        recycleView.adapter = mAdapter
        Log.d(logTag,"循环视图碎片创建成功")
        return view
    }
}