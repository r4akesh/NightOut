package com.nightout.model

import java.io.Serializable

data class BookEventMdlResponse(
    val `data`: Data,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
):Serializable {

    data class Data(
        val amount: String,
        val created_at: String,
        val id: Int,
        val order_id: String,
        val transaction_id: String,
        val type: String,
        val updated_at: String,
        val user_id: String,
        val vendor_id: String,
        val venue_id: String
    ) : Serializable
}