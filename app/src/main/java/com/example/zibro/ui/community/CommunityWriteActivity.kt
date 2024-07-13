package com.example.zibro.ui.community

import android.os.Bundle
import android.widget.Toast
import com.example.zibro.R
import com.example.zibro.databinding.ActivityCommunityWriteBinding
import com.example.zibro.model.Article
import com.example.zibro.model.Friend
import com.example.zibro.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CommunityWriteActivity : BaseActivity<ActivityCommunityWriteBinding>(R.layout.activity_community_write) {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        firestore = FirebaseFirestore.getInstance()

        val email = user?.email // 현재 로그인한 사용자의 이메일
        firestore.collection(email.toString()).get()
            .addOnSuccessListener { documents ->
                val friend = documents.toObjects(Friend::class.java)
                name = friend[0].name
            }

        binding.buttonSubmit.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val content = binding.editTextContent.text.toString()
            val channel = binding.spinnerChannel.selectedItem.toString()
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
            val currentDate = dateFormat.format(calendar.time)
            if (title.isNotEmpty() && content.isNotEmpty()) {
                val documentId = firestore.collection("community").document().id // 새로운 문서 ID 생성
                val newArticle = Article(
                    id = documentId,
                    classify = channel,
                    context = content,
                    title = title,
                    username = name,
                    time = currentDate.toString(),
                )

                // Firestore에 Article 객체 저장
                firestore.collection("community").document(documentId).set(newArticle)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Article submitted successfully", Toast.LENGTH_SHORT).show()
                        binding.editTextTitle.text.clear()
                        binding.editTextContent.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error submitting article: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
