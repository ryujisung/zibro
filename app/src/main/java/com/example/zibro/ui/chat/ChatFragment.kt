package com.example.zibro.ui.chat
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zibro.databinding.FragmentChatBinding
import com.example.zibro.model.ChatRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private lateinit var auth: FirebaseAuth

    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.add.setOnClickListener {
            createChatRoom("새대화")
        }
        setupRecyclerView()
        fetchChatRooms()

        return binding.root
    }
    private fun createChatRoom(name: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email // 현재 로그인한 사용자의 이메일
        val newRoomId = database.child("chatrooms").push().key
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)

        newRoomId?.let {
            val users = mapOf( email?.dropLast(10).toString() to  true) // 샘플 사용자 목록, 실제 구현에서는 현재 사용자 정보를 사용해야 함
            val chatRoom = ChatRoom(
                roomId = it,
                roomname = name,
                users = users,
                createdate = currentDate.toString()
            )
            database.child("chatrooms").child(it).setValue(chatRoom)
        }
    }

    private fun setupRecyclerView() {
        chatRoomAdapter = ChatRoomAdapter { chatRoom ->
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("chatRoomId", chatRoom.roomId)
            startActivity(intent)
        }
        binding.recyclerChatrooms.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatRoomAdapter
        }
    }

    private fun fetchChatRooms() {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email // 현재 로그인한 사용자의 이메일
        database.child("chatrooms").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chatRooms = mutableListOf<ChatRoom>()
                for (snapshot in dataSnapshot.children) {
                    val chatRoom = snapshot.getValue(ChatRoom::class.java)
                    auth = FirebaseAuth.getInstance()
                    if(chatRoom?.users?.containsKey(user?.email?.dropLast(10).toString()) == true) {
                        chatRoom?.let {
                            chatRooms.add(it)
                        }
                    }
                }
                Log.d("ChatFragment", "Loaded chat rooms: ${chatRooms.size}") // 추가된 로그
                chatRoomAdapter.setChatRooms(chatRooms)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 채팅방 목록 불러오기 실패 처리
                Log.e("ChatFragment", "Error loading chat rooms: ${databaseError.message}") // 오류 로그 추가
            }
        })
    }
}
