package com.example.my.myPage.myFrag.userInfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.my.bean.UserBean
import com.example.myretrofit.R

class UserInfoAdapter(context : Context, userList : List<UserBean>) : RecyclerView.Adapter<ViewHolder>() {
    private var mUserList : List<UserBean>
    private val mContext : Context
    init {
        mUserList = userList
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_linear_layout_user_info,parent,false)
        return UserHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* 在 Kotlin 中，你应该使用 as 或 as? 进行类型转换，而不是使用 Java 风格的 (Type) object。
        使用 as 进行强制类型转换，使用 as? 进行安全的类型转换。使用 is 进行类型检查，Kotlin 会自动进行智能转换。 */
        val vh :UserHolder = holder as UserHolder
        vh.nickNameTv.text = mUserList[position].name
        vh.nameTv.text = mUserList[position].username
        vh.emailTv.text = mUserList[position].email
        vh.phoneTv.text = mUserList[position].phone
    }
    fun updateData(newUserList: List<UserBean>) {
        mUserList = newUserList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return mUserList.size
    }
    class UserHolder(itemView: View) : ViewHolder(itemView){
        val nickNameTv : TextView
        val nameTv : TextView
        val emailTv :TextView
        val phoneTv :TextView
        init {
            nickNameTv = itemView.findViewById(R.id.tv_item_user_nick_name)
            nameTv = itemView.findViewById(R.id.tv_item_user_name)
            emailTv = itemView.findViewById(R.id.tv_item_user_email)
            phoneTv = itemView.findViewById(R.id.tv_item_user_phone)
        }
    }
}
