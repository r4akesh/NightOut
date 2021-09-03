package com.nightout.model

data class AddFavModel(
    val `data`: Data,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
) {

    data class Data(
        val created_at: String,
        val id: String,
        val status: String,
        val updated_at: String,
        val user_id: String,
        val vendor_id: String,
        val venue_id: String
    )
}