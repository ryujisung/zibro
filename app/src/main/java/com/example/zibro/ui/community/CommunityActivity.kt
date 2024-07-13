package com.example.zibro.ui.community

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zibro.R
import com.example.zibro.databinding.ActivityCommunityBinding
import com.example.zibro.model.Article
import com.example.zibro.model.Comment
import com.example.zibro.model.Friend
import com.example.zibro.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CommunityActivity : BaseActivity<ActivityCommunityBinding>(R.layout.activity_community) {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var name: String
    private var comments = mutableListOf<Comment>()
    private var articleId: String? = null
    private var isRecommended = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val article = intent.getParcelableExtra<Article>("article")
        Log.e("CommunityActivity", article?.title.toString())
        articleId = article?.id

        db = Firebase.firestore

        binding.title = article?.title.toString()
        binding.content = article?.context.toString()
        binding.classify = article?.classify.toString()
        binding.name = article?.username.toString()
        binding.time = article?.time.toString()
        binding.comment = "댓글 " + article?.comment.toString()
        binding.comments = article?.comment.toString()
        binding.view = "조회수 " + article?.view.toString()
        binding.goods = "추천 " + article?.good.toString()
        binding.good = article?.good.toString()

        // RecyclerView 초기화
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(this)
        loadComments(articleId)
        Log.e("CommunityActivity", comments.toString())
        commentAdapter = CommentAdapter(comments)
        binding.recyclerViewComments.adapter = commentAdapter

        // 조회수 증가
        incrementView()

        // 추천 여부 확인 및 초기화
        isRecommended = getRecommendationStatus()
        updateRecommendButton()

        // 댓글 불러오기


        // 추천 버튼 클릭 리스너 설정
        binding.recommendLayout.setOnClickListener {
            toggleRecommendation()
        }

        // 댓글 추가 버튼 클릭 리스너 설정
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
                        Log.e("CommunityActivity", comment.toString())
                        comments.add(comment)
                    }
                    commentAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun addComment(article: Article?, content: String) {
        if (article == null) return
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val email = user?.email // 현재 로그인한 사용자의 이메일
        db.collection(email.toString()).get()
            .addOnSuccessListener { documents ->
                val friend = documents.toObjects(Friend::class.java)
                val name = friend[0].name

                val comment = Comment(
                    good = 0,
                    content = content,
                    time = currentDate.toString(),
                    username = name // 실제 사용자 이름을 넣어야 합니다.
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

    private fun incrementView() {
        val articleId = this.articleId ?: return
        val articleRef = db.collection("community").document(articleId)
        articleRef.update("view", FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d("CommunityActivity", "조회수 증가 성공")
            }
            .addOnFailureListener { e ->
                Log.w("CommunityActivity", "조회수 증가 실패", e)
            }
    }

    private fun getRecommendationStatus(): Boolean {
        val sharedPreferences = getSharedPreferences("recommendations", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(articleId, false)
    }

    private fun setRecommendationStatus(isRecommended: Boolean) {
        val sharedPreferences = getSharedPreferences("recommendations", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(articleId, isRecommended)
            apply()
        }
    }

    private fun updateRecommendButton() {
        if (isRecommended) {
            binding.recommend.setImageResource(R.drawable.ic_recommend_true)
            binding.goodic.setTextColor(Color.parseColor("#5592FC"))
            binding.recommendLayout.background = ContextCompat.getDrawable(this, R.drawable.edt_bg_main_true)
        } else {
            binding.recommend.setImageResource(R.drawable.ic_recommend_false)
            binding.goodic.setTextColor(Color.parseColor("#838A94"))
            binding.recommendLayout.background = ContextCompat.getDrawable(this, R.drawable.edt_background_main)
        }
    }

    private fun toggleRecommendation() {
        val articleId = this.articleId ?: return
        val articleRef = db.collection("community").document(articleId)

        if (isRecommended) {
            // 이미 추천한 경우
            articleRef.update("good", FieldValue.increment(-1))
                .addOnSuccessListener {
                    isRecommended = false
                    setRecommendationStatus(false)
                    updateRecommendButton()
                    binding.good = (binding.good!!.toInt() - 1).toString()
                    binding.goods = "추천 " + binding.good
                    Log.d("CommunityActivity", "추천 취소 성공")
                }
                .addOnFailureListener { e ->
                    Log.w("CommunityActivity", "추천 취소 실패", e)
                }
        } else {
            // 추천하지 않은 경우
            articleRef.update("good", FieldValue.increment(1))
                .addOnSuccessListener {
                    isRecommended = true
                    setRecommendationStatus(true)
                    updateRecommendButton()
                    binding.good = (binding.good!!.toInt() + 1).toString()
                    binding.goods = "추천 " + binding.good
                    Log.d("CommunityActivity", "추천 성공")
                }
                .addOnFailureListener { e ->
                    Log.w("CommunityActivity", "추천 실패", e)
                }
        }
    }
}
