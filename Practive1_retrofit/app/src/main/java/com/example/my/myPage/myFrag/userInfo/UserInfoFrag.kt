package com.example.my.myPage.myFrag.userInfo

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my.myModel.UserListViewModel
import com.example.myretrofit.R

class UserInfoFrag : Fragment() {
    /**你遇到的问题是由于 Fragment 类中已经有一个名为 getTag() 的方法，而你在 UserInfoFrag 类中定义了一个名为 tag 的属性。
     * Kotlin 会自动为属性生成一个 getter 方法，因此 tag 属性的 getter 方法与 Fragment 类中的 getTag() 方法冲突了。
     */
    val logTag: String = "用户信息列表-碎片"
    lateinit var viewModel: UserListViewModel
    private lateinit var mAdapter: UserInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(logTag, "开始创建循环视图碎片")
        initUserListViewModel()
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)
        val recycleView: RecyclerView = view.findViewById(R.id.rv_user_info_list_container)
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycleView.layoutManager = linearLayoutManager
        mAdapter = UserInfoAdapter(requireContext(), arrayListOf())
        recycleView.adapter = mAdapter
        Log.d(logTag, "循环视图碎片创建成功")
        fetchData()
        return view
    }

    private fun initUserListViewModel() {
        /* 两者的区别，碎片实现了 ViewModelStoreOwner ，故可以直接获取 ViewModel ; 而碎片没有实现 LifecycleOwner
        ,故需要使用 getViewLifecycleOwner() 获取,添加到observe中 kotlin中则直接简化了步骤*/
        // 获取 ViewModel 实例
        viewModel = ViewModelProvider(this)[UserListViewModel::class.java]
        Log.d(logTag, "ViewModel创建成功")
    }

    private fun fetchData() {
        viewModel.fetchData(
            onSuccess = {data->
                Log.d("qjf", "${Looper.myLooper()==Looper.getMainLooper()}")
                mAdapter.updateData(data)

            },
            onError = {
                Log.d("qjf", "Throwable = $it")
            })
    }


}