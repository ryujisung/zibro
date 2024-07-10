package com.example.zibro.ui.community

import android.os.Bundle
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
        setContentView(R.layout.activity_community)
        val article = intent.getParcelableExtra<Article>("article")

        binding.articleTitle.text = article?.title
        binding.articleContent.text = article?.context
    }
}
