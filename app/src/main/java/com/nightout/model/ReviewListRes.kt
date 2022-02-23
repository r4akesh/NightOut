package com.nightout.model

import java.io.Serializable

data class ReviewListRes(
    val `data`: ArrayList<Data>,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int
):Serializable {
    data class Data(
        val amount: String,
        val created_at: String,
        val grand_total: String,
        val id: String,
        val order_id: String,
        val payment_mode: String,
        val product_id: String,
        val qty: String,
        val status: String,
        val store_logo: String,
        val store_name: String,
        val updated_at: String,
        val user_id: String,
        val vendor_id: String,
        val venue_id: String
    ):Serializable
}