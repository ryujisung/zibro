package com.example.zibro.ui.community

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zibro.R
import com.example.zibro.model.Article

class CommunutyAdapter(private val onChatRoomClicked: (Article) -> Unit) : RecyclerView.Adapter<CommunutyAdapter.ArticleViewHolder>() {

    private val articles = mutableListOf<Article>()

    fun setChatRooms(rooms: List<Article>) {
        articles.clear()
        articles.addAll(rooms)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CommunityActivity::class.java)
            intent.putExtra("article", article)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = articles.size

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val articleTitleTextView: TextView = itemView.findViewById(R.id.article_textview_title)
        private val articleCommentCount: TextView = itemView.findViewById(R.id.article_item_textview_comment)
        private val articleClassify : TextView = itemView.findViewById(R.id.article_item_textview_classify)
        private val articlaTime : TextView = itemView.findViewById(R.id.article_item_textview_time)
        private val articleUserName: TextView = itemView.findViewById(R.id.article_item_textview_username)

        fun bind(article: Article) {
            articleTitleTextView.text = article.title
            articleCommentCount.text = "[" + article.comment.toString() +"]"
            articleClassify.text = article.classify
            articlaTime.text = article.time
            articleUserName.text = article.username
        }
    }
}

