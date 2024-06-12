package com.example.zibro.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zibro.R
import com.example.zibro.model.ChatRoom


class ChatRoomAdapter(private val onChatRoomClicked: (ChatRoom) -> Unit) : RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    private val chatRooms = mutableListOf<ChatRoom>()

    fun setChatRooms(rooms: List<ChatRoom>) {
        chatRooms.clear()
        chatRooms.addAll(rooms)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chatroom, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatRooms[position]
        holder.bind(chatRoom)
        holder.itemView.setOnClickListener { onChatRoomClicked(chatRoom) }
    }

    override fun getItemCount() = chatRooms.size

    class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chatRoomNameTextView: TextView = itemView.findViewById(R.id.chat_textview_title)
        private val chatLastMessageTextView: TextView = itemView.findViewById(R.id.chat_item_textview_lastmessage)

        fun bind(chatRoom: ChatRoom) {
            chatRoomNameTextView.text = chatRoom.roomname
            chatLastMessageTextView.text = chatRoom.createdate
        }
    }
}
