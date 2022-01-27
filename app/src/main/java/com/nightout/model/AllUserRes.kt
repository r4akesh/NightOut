package com.nightout.model

data class AllUserRes(
    val `data`: ArrayList<Data>,
    val image_path: String,
    val message: String,
    val response: String,
    val status_code: Int,
    val user_default_img: String
) {
    data class Data(
        var isChk : Boolean,
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
        val userID: String,
        val userprofile: Userprofile
    )

    data class Userprofile(
        val about_me: String,
        val address1: String,
        val address2: String,
        val city: String,
        val created_at: String,
        val id: String,
        val latitude: String,
        val longitude: String,
        val updated_at: String,
        val user_id: String
    )
}