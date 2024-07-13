package com.example.zibro.ui.community

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zibro.R
import com.example.zibro.databinding.ActivityCommunityBinding
import com.example.zibro.model.Article
import com.example.zibro.model.Comment
import com.example.zibro.ui.base.BaseActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommunityActivity : BaseActivity<ActivityCommunityBinding>(R.layout.activity_community) {

    private lateinit var db: FirebaseFirestore
    private lateinit var commentAdapter: CommentAdapter
    private var comments = mutableListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val article = intent.getParcelableExtra<Article>("article")
        Log.e("ddd", article?.title.toString())

        db = Firebase.firestore

        binding.title = article?.title.toString()
        binding.content = article?.context.toString()

        binding.recyclerViewComments.layoutManager = LinearLayoutManager(this)
        commentAdapter = CommentAdapter(comments)
        binding.recyclerViewComments.adapter = commentAdapter

        loadComments(article?.id)

        binding.buttonSubmit.setOnClickListener {
            val reply = binding.editTextReply.text.toString()
            if (reply.isNotBlank()) {
                addComment(article, reply)
                binding.editTextReply.text.clear()
            }
        }
    }

    private fun loadComments(articleId: String?) {
        if (articleId == null) return

        db.collection("community").document(articleId).collection("comments")
            .orderBy("time")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("CommunityActivity", "댓글 로드 실패", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    comments.clear()
                    for (document in snapshot) {
                        val comment = document.toObject(Comment::class.java)
                        comments.add(comment)
                    }
                    commentAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun addComment(article: Article?, content: String) {
        if (article == null) return

        val comment = Comment(
            good = 0,
            content = content,
            time = "방금 전",
            username = "현재 사용자 이름" // 실제 사용자 이름을 넣어야 합니다.
        )

        // Firestore에서 현재 글의 reference 가져오기
        val articleRef = db.collection("community").document(article.id)

        // 댓글 추가
        articleRef.collection("comments").add(comment)
            .addOnSuccessListener { documentReference ->
                Log.d("CommunityActivity", "댓글 추가 성공: ${documentReference.id}")

                // 댓글 수 증가시키기
                articleRef.update("comment", FieldValue.increment(1))
                    .addOnSuccessListener {
                        Log.d("CommunityActivity", "댓글 수 증가 성공")
                    }
                    .addOnFailureListener { e ->
                        Log.w("CommunityActivity", "댓글 수 증가 실패", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("CommunityActivity", "댓글 추가 실패", e)
            }
    }
}
