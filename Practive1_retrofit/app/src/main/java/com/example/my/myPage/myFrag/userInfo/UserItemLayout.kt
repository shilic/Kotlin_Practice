package com.example.my.myPage.myFrag.userInfo

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.myretrofit.R
import java.util.jar.Attributes

/**
 * 用户信息 item,自定义组件
 */
@Deprecated("弃用，RecyclerView 相比于 之前老套的 ScrollView ，不需要手动添加布局管理文件了。与之相对的，有 ViewHolder 来持有页面数据。")
class UserItemLayout(context : Context) : LinearLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.item_linear_layout_user_info, this) //绑定布局
    }
    /**
     * 次级构造函数
     */
    constructor(context: Context, attributes: Attributes) : this(context) {

    }
    fun initView(context: Context): Unit {

    }
}