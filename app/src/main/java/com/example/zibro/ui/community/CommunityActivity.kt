package com.example.zibro.ui.community

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.zibro.R
import com.example.zibro.model.Article
import android.widget.TextView
import com.example.zibro.databinding.ActivityChatBinding
import com.example.zibro.databinding.ActivityCommunityBinding
import com.example.zibro.ui.base.BaseActivity

class CommunityActivity : BaseActivity<ActivityCommunityBinding>(R.layout.activity_community) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val article = intent.getParcelableExtra<Article>("article")
        Log.e("ddd",article?.title.toString())

        binding.title = article?.title.toString()
        binding.content = article?.context.toString()
        binding.buttonSubmit.setOnClickListener {
            val reply = binding.editTextReply.text.toString()
            binding.editTextReply.text.clear()
        }
    }
}
