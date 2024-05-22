package com.example.zibro.model

import java.io.Serializable

data class Message(
    var senderName: String = "",

    var senderUid: String = "",
    var sended_date: String = "",
    var content: String = "",
    var confirmed:Boolean=false
) : Serializable {
}