package com.example.zibro.ui.chat

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zibro.R
import com.example.zibro.databinding.ActivityChatBinding
import com.example.zibro.model.GenerateTextRequest
import com.example.zibro.model.GeneratedTextResponse
import com.example.zibro.model.Message
import com.example.zibro.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ChatActivity : BaseActivity<ActivityChatBinding>(R.layout.activity_chat) {
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private val messagesList = mutableListOf<Message>()
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser ?: return
        val currentUserId = currentUser.uid

        messageAdapter = MessageAdapter(currentUserId)
        binding.recyclerViewChat.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }

        val chatRoomId = intent.getStringExtra("chatRoomId") ?: return

        setupChatRoom(chatRoomId)
        val client = OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS) // 연결 timeout 설정
            .readTimeout(40, TimeUnit.SECONDS) // 읽기 timeout 설정
            .writeTimeout(40, TimeUnit.SECONDS) // 쓰기 timeout 설정
            .build()

// Retrofit.Builder를 사용하여 Retrofit 인스턴스 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://846d-35-247-177-59.ngrok-free.app/")
            .client(client) // OkHttpClient 설정을 Retrofit에 적용
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        // ApiService 인스턴스 생성
        apiService = retrofit.create(ApiService::class.java)

        binding.buttonSendMessage.setOnClickListener {
            val messageText = binding.edittextChatMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val message = Message(
                    senderUid = currentUserId,
                    content = messageText,
                    senderName = currentUser.displayName ?: "Unknown",
                    sended_date = getCurrentDateTime()
                )
                sendMessage(chatRoomId, message)
                callAPI(messageText)
                binding.edittextChatMessage.text.clear()
            }
        }
    }

    // 현재 날짜와 시간을 문자열로 반환
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun setupChatRoom(chatRoomId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("chatrooms/$chatRoomId/messages")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    message?.let { messagesList.add(it) }
                }
                messageAdapter.setMessages(messagesList)
                binding.recyclerViewChat.scrollToPosition(messagesList.size - 1)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    private fun sendMessage(chatRoomId: String, message: Message) {
        val database = FirebaseDatabase.getInstance()
        val messagesRef = database.getReference("chatrooms/$chatRoomId/messages")

        messagesRef.push().setValue(message).addOnCompleteListener { messageTask ->
            if (messageTask.isSuccessful) {
                val lastMessageRef = database.getReference("chatrooms/$chatRoomId/lastMessage")
                lastMessageRef.setValue(message.content)
            }
        }
    }

    // GPT API 호출 함수 (Retrofit을 사용하여 Flask 서버로 요청)
    fun callAPI(question: String) {
        val request = GenerateTextRequest("질문 : "+question+"답변:")
        apiService.generateText(request).enqueue(object : Callback<GeneratedTextResponse> {
            override fun onResponse(call: Call<GeneratedTextResponse>, response: Response<GeneratedTextResponse>) {
                if (response.isSuccessful) {
                    val generatedText = response.body()?.generated_text ?: ""
                    addResponse(generatedText)
                } else {
                    addResponse("Failed to load response due to ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<GeneratedTextResponse>, t: Throwable) {
                addResponse("Failed to load response due to ${t.message}")
            }
        })
    }

    private fun addResponse(response: String) {
        val chatRoomId = intent.getStringExtra("chatRoomId") ?: return
        runOnUiThread {
            // 여기서 response 문자열을 처리합니다.
            // 예를 들어, 채팅 메시지 목록에 추가하거나 화면에 표시할 수 있습니다.
            val botMessage = Message(
                senderUid = "bot",
                content = response,
                senderName = "ChatBot",
                sended_date = getCurrentDateTime()
            )
            sendMessage(chatRoomId, botMessage)
        }
    }
}
