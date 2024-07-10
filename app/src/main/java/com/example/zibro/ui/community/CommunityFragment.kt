package com.example.zibro.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zibro.R
import com.example.zibro.databinding.FragmentCommunityBinding
import com.example.zibro.model.Article
import com.example.zibro.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community) {
    private lateinit var communutyAdapter: CommunutyAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setupRecyclerView()
        fetchArticles()

        return view
    }

    private fun setupRecyclerView() {
        communutyAdapter = CommunutyAdapter { article ->
            // Handle item click
        }

        binding.recyclerCommunity.apply {
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
                communutyAdapter.setChatRooms(articles)
            }
    }
}
