package com.example.zibro.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.zibro.R
import com.example.zibro.databinding.ActivityMainBinding
import com.example.zibro.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}