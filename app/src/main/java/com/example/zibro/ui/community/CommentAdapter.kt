package com.example.zibro.ui.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zibro.R
import com.example.zibro.model.Comment

class CommentAdapter(private val comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewUsername: TextView = itemView.findViewById(R.id.textViewUsername)
        private val textViewContent: TextView = itemView.findViewById(R.id.textViewContent)
        private val textViewTime : TextView = itemView.findViewById(R.id.textViewTime)

        fun bind(comment: Comment) {
            textViewUsername.text = comment.username
            textViewContent.text = comment.content
            textViewTime.text = comment.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size
}
