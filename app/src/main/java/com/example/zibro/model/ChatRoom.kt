package com.example.zibro.model

data class ChatRoom(
    val roomId: String = "",
    val roomname: String = "",
    var profileImageUrl: String = "",
    val users: Map<String, Boolean> =  emptyMap(), // 사용자 ID와 해당 사용자가 채팅방에 있는지 여부를 나타내는 맵
    val lastMessage: String? = null // 마지막 메시지 (옵션)
)