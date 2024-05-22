package com.example.zibro.model

data class Event(
    var date: String = "",
    var title: String ="",
    var notice: Boolean = false,
    var starttime: String="",
    var endtime: String = "",
    var public: Boolean = false
)