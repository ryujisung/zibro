package com.example.zibro.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zibro.R
import com.example.zibro.databinding.FragmentHomeBinding
import com.example.zibro.model.Article
import com.example.zibro.ui.base.BaseFragment
import com.example.zibro.ui.home.hotcommunity.HotCommunityAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var hotCommunityAdapter: HotCommunityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        setupRecyclerView()
        fetchArticles()

        val user = FirebaseAuth.getInstance().currentUser

        val range = (1..2)
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
        hotCommunityAdapter = HotCommunityAdapter { article ->
            // Handle item click
        }

        binding.recyclerHotcommunity.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = hotCommunityAdapter
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
                hotCommunityAdapter.setChatRooms(articles)
            }
    }
}
