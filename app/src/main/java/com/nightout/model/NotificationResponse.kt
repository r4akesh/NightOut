package com.nightout.model

data class NotificationResponse(
    val `data`: ArrayList<Data>,
    val message: String,
    val response: String,
    val status_code: Int
) {

    data class Data(
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