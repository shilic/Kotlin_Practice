package com.example.my.myModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserListViewModel: ViewModel() {
    private val logTag : String = "用户信息列表-视图模型"
    // 使用 MutableLiveData 来封装 User 对象
    private val mUserList = MutableLiveData<List<User>>()

    // 对外暴露不可变的 LiveData 。只可以 get ; 不可以 set
    val listLiveData: LiveData<List<User>> get() = mUserList
    // 更新 User 对象的方法
    fun updateUsers(newUserList: List<User>) {
        mUserList.value = newUserList
        Log.d(logTag,"恭喜你，成功刷新ViewModel层数据")
    }
    override fun onCleared() {
        super.onCleared()
    }
}