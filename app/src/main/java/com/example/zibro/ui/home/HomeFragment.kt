package com.example.zibro.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zibro.R
import com.example.zibro.databinding.FragmentCommunityBinding
import com.example.zibro.databinding.FragmentHomeBinding
import com.example.zibro.model.Article
import com.example.zibro.model.Friend
import com.example.zibro.model.Phrase
import com.example.zibro.ui.base.BaseFragment
import com.example.zibro.ui.community.CommunutyAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var communutyAdapter: CommunutyAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        setupRecyclerView()
        fetchArticles()
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
    private fun setupRecyclerView() {
        communutyAdapter = CommunutyAdapter { article ->
            // Handle item click
        }

        binding.recyclerHotcommunity.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = communutyAdapter
        }
    }

    private fun fetchArticles() {
        firestore.collection("community")
            .get()
            .addOnSuccessListener { documents ->
                val articles = mutableListOf<Article>()
                for (document in documents) {
                    val article = document.toObject(Article::class.java)

                    articles.add(article)
                }
                articles.sortedByDescending  { it.good }
                communutyAdapter.setChatRooms(articles)
            }
    }
}
