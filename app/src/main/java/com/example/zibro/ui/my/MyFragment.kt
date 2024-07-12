package com.example.zibro.ui.my

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zibro.R
import com.example.zibro.databinding.FragmentMyBinding
import com.example.zibro.model.Friend
import com.example.zibro.ui.base.BaseFragment
import com.example.zibro.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyFragment: BaseFragment<FragmentMyBinding>(R.layout.fragment_my) {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        firestore = FirebaseFirestore.getInstance()
        val email = user?.email // 현재 로그인한 사용자의 이메일
        firestore.collection(email.toString()).get()
            .addOnSuccessListener { documents ->

                val friend = documents.toObjects(Friend::class.java)
                binding.nickname = friend[0].name
                binding.state = friend[0].email
            }
        binding.myTxtLogout.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}