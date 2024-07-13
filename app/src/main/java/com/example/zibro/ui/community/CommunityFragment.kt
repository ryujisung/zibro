package com.example.zibro.ui.community

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zibro.R
import com.example.zibro.databinding.FragmentCommunityBinding
import com.example.zibro.model.Article
import com.example.zibro.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout
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

        val titles = listOf("전체", "자유", "유머", "질문", "상담")

        setupRecyclerView()
        setupTabLayout(titles)
        fetchArticles()
        binding.writeButton.setOnClickListener {
            val intent = Intent(context, CommunityWriteActivity::class.java)
            startActivityForResult(intent, WRITE_REQUEST_CODE)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        fetchArticles(binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.text.toString())
    }

    private fun setupRecyclerView() {
        communutyAdapter = CommunutyAdapter { article ->
            // Handle item click
            val intent = Intent(context, CommunityActivity::class.java)
            intent.putExtra("article", article)
            startActivityForResult(intent, VIEW_ARTICLE_REQUEST_CODE)
        }

        binding.recyclerCommunity.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = communutyAdapter
        }
    }

    private fun setupTabLayout(titles: List<String>) {
        titles.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val selectedCategory = tab.text.toString()
                fetchArticles(selectedCategory)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun fetchArticles(category: String = "전체") {
        firestore.collection("community")
            .get()
            .addOnSuccessListener { documents ->
                val articles = mutableListOf<Article>()
                for (document in documents) {
                    val article = document.toObject(Article::class.java)
                    if (category == "전체" || article.classify == category) {
                        articles.add(article)
                    }
                }
                communutyAdapter.setChatRooms(articles)
            }
    }

    companion object {
        private const val WRITE_REQUEST_CODE = 1
        private const val VIEW_ARTICLE_REQUEST_CODE = 2
    }
}
