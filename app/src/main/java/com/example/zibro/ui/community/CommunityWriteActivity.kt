package com.example.zibro.ui.community

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zibro.R
import com.example.zibro.databinding.ActivityCommunityWriteBinding
import com.example.zibro.model.Article
import com.example.zibro.model.Friend
import com.example.zibro.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CommunityWriteActivity : BaseActivity<ActivityCommunityWriteBinding>(R.layout.activity_community_write) {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
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

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val newArticle = Article(
                    classify = channel,
                    context = content,
                    title = title,
                    username = name,
                    time = System.currentTimeMillis().toString(),
                )

                // Firestore에 Article 객체 저장
                firestore.collection("community").add(newArticle)
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
