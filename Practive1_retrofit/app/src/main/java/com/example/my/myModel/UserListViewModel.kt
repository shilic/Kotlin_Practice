package com.example.my.myModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.my.bean.UserBean
import com.example.my.manager.NetworkManager
import com.example.my.myService.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModel : ViewModel() {


    fun fetchData(onSuccess: (data: List<UserBean>) -> Unit, onError: (errMsg: String) -> Unit) {
        NetworkManager.create(NetworkService::class.java).getUsers().enqueue(
            object : Callback<List<UserBean>> {
                override fun onFailure(call: Call<List<UserBean>>, t: Throwable) {
                    Log.d("qjf", "Throwable = ${t.message}")
                    onError.invoke(t.message ?: "")
                }

                override fun onResponse(call: Call<List<UserBean>>, resp: Response<List<UserBean>>) {
                    val users = resp.body()
                    Log.d("qjf", "users = $users")
                    users?.let { data ->
                        onSuccess(data)
                    }
                }
            }
        )
    }


}