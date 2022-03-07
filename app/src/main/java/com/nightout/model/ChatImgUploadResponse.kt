package com.nightout.model

data class ChatImgUploadResponse(
    val `data`: Data,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
) {
    data class Data(
        val `file`: String,
        val room_id: String,
        val thumbnail: String
    )
}