package com.gereso.terramasterhub

data class Message(
    val senderId: String = "",
    val senderName: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = 0
)

