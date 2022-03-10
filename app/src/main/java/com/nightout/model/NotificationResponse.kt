package com.nightout.model

data class NotificationResponse(
    val `data`: Data,
    val message: String,
    val response: String,
    val status_code: Int
) {
    data class Data(
        val read: ArrayList<Read>,
        val unread: ArrayList<Unread>
    )

    data class Read(
        val created_at: String,
        val id: String,
        val message: String,
        val reciever_id: String,
        val reply: String,
        val seen: String,
        val sender_id: String,
        val slug: String,
        val status: String,
        val subject: String,
        val type: String,
        val updated_at: String
    )

    data class Unread(
        val created_at: String,
        val id: String,
        val message: String,
        val reciever_id: String,
        val reply: String,
        val seen: String,
        val sender_id: String,
        val slug: String,
        val status: String,
        val subject: String,
        val type: String,
        val updated_at: String
    )
}