package com.nightout.model

data class BarcrwalCreatedRes(
    val `data`: List<Data>,
    val message: String,
    val response: String,
    val status_code: Int
) {
    data class Data(
        val created_at: String,
        val host_guest: String,
        val id: String,
        val status: String,
        val updated_at: String,
        val user_bar_crawl_id: String,
        val user_detail: UserDetail,
        val user_id: String
    )

    data class UserDetail(
        val address: String,
        val created_at: String,
        val deleted_at: String,
        val device_id: String,
        val device_type: String,
        val email: String,
        val email_verified_at: String,
        val first_name: String,
        val id: String,
        val last_name: String,
        val name: String,
        val otp: String,
        val otp_expire: String,
        val phonenumber: String,
        val profile: String,
        val slug: String,
        val status: String,
        val updated_at: String,
        val userID: String
    )
}