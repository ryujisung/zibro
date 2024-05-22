package com.example.zibro.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.lets_meet.ui.utils.State
import com.example.zibro.R
import com.example.zibro.databinding.ActivitySignUpBinding
import com.example.zibro.ui.base.BaseActivity
import com.example.zibro.ui.main.MainActivity
import com.google.firebase.FirebaseApp

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up){
    private lateinit var navController: NavController

    val viewModel : SignUpViewModel by viewModels{
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SignUpViewModel() as T
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)

        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_register) as NavHostFragment


        navController = navHostFragment.navController
        viewModel.state.observe(this){
            when(it){
                State.OK -> {
                    navController.navigate(R.id.action_signUp5Fragment_to_signUp6Fragment)
                }
                State.LOADING -> {}
                State.FAIL -> {
                    Toast.makeText(this, "회원가입에 실패하셨습니다. 잠시후 다시 실행해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    val gotoMain : (View) -> Unit = {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}