package com.example.my.myPage.myHandler

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.my.MainActivity
import java.lang.ref.WeakReference

/**
 * 用户信息处理者
 */
class UserHandler(context : Context,looper: Looper) : Handler(looper) {
    private val weakRef : WeakReference<Context>
    init {
        weakRef = WeakReference(context)
    }
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (msg.what == 1 && (weakRef.get() is MainActivity) ){
            val context = weakRef.get() as MainActivity
        }
    }
}