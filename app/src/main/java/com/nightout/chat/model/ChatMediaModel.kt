package com.nightout.chat.model

import java.io.Serializable

data class ChatMediaModel(
    val __v: Int,
    val _id: String,
    val media: String,
    val message: String,
    val message_content: MessageContent,
    val message_type: String,
    val receiver_id: String,
    val roomId: String,
    val sender_id: String,
    val time: String,
    val timestamp: Long
) :Serializable{

    data class MessageContent(
        val file_meta: FileMeta,
        val file_url: String,
        val latitude: String,
        val longitude: String,
        val address: String,
        val name: String
    ):Serializable

    data class FileMeta(
        val file_size: Int,
        val file_type: String,
        val thumbnail: String
    ):Serializable
}