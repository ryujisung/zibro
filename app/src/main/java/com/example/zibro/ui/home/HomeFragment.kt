package com.example.zibro.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zibro.R
import com.example.zibro.databinding.FragmentCommunityBinding
import com.example.zibro.databinding.FragmentHomeBinding
import com.example.zibro.model.Friend
import com.example.zibro.model.Phrase
import com.example.zibro.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
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
        var range = (1..2)
        firestore.collection("MainPhrase").document(range.random().toString()).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val phrase = document.getString("phrase")
                    binding.phrase = phrase
                }
            }
        return view
    }

}