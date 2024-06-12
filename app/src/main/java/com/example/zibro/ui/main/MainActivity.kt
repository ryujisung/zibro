package com.example.zibro.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import com.example.zibro.R
import com.example.zibro.databinding.ActivityMainBinding
import com.example.zibro.ui.base.BaseActivity
import com.example.zibro.ui.chat.ChatFragment
import com.example.zibro.ui.community.CommunityFragment
import com.example.zibro.ui.home.HomeFragment
import com.example.zibro.ui.my.MyFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var bnv_main = binding.bnvMain

        bnv_main.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.main_menu_home -> {
                        // 다른 프래그먼트 화면으로 이동하는 기능
                        val homeFragment = HomeFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_main, homeFragment).commit()
                    }
                    R.id.main_menu_community -> {
                        val friendFragment = CommunityFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_main, friendFragment).commit()
                    }
                    R.id.main_menu_counsel -> {
                        val caleanderFragment = ChatFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_main, caleanderFragment).commit()
                    }
                    R.id.main_menu_my -> {
                        val chatFragment = MyFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_main, chatFragment).commit()
                    }
                }
                true
            }
            selectedItemId = R.id.main_menu_home
        }

    }
}