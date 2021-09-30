package com.nightout.model

import java.io.Serializable

  data class BookEventMdlResponse(
    val `data`: Data,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
):Serializable

data class Data(
    val amount: Int,
    val created_at: String,
    val id: Int,
    val payment_mode: String,
    val qty: String,
    val status: Int,
    val ticket_download: String,
    val ticket_number: String,
    val updated_at: String,
    val user_id: Int,
    val vendor_id: String,
    val venue_id: String
):Serializable