package com.nightout.model

import java.io.Serializable

data class PlaceOrderResponse(
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
        val customer_id: String,
        val ephemeralKey: String,
        val id: Int,
        val order_id: String,
        val partically_order: String,
        val payment_intent_key: String,
        val payment_mode: String,
        val status: String,
        val transaction_id: String,
        val type: String,
        val updated_at: String,
        val user_id: String,
        val vendor_id: String,
        val venue_id: String
    ):Serializable
}