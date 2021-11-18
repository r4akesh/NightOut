package com.nightout.model

data class EndLocationResponse(
    val `data`: Data,
    val message: String,
    val response: String,
    val status_code: Int
) {

    data class Data(
        val address: String,
        val created_at: String,
        val id: String,
        val lattitude: String,
        val longitude: String,
        val updated_at: String,
        val user_id: String
    )
}