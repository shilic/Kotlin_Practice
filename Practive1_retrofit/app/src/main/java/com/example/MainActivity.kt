package com.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myretrofit.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        retrofitTest()
    }
    private fun retrofitTest(){

    }
}