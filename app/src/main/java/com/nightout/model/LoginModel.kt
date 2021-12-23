package com.nightout.model


data class LoginModel(
    val `data`: Data,
    val message: String,
    val response: String,
    val status_code: Int
) {

    data class Data(
        val address: String,
        val admin_detail: AdminDetail,
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
        val token: String,
        val updated_at: String,
        val userID: String,
        val user_email_setting: UserEmailSetting,
        val user_notification_setting: UserNotificationSetting,
        val userprofile: Userprofile
    )

    data class AdminDetail(
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

    data class UserEmailSetting(
        val created_at: String,
        val id: String,
        var status: String,
        val updated_at: String,
        val user_id: String
    )

    data class UserNotificationSetting(
        val created_at: String,
        val id: String,
        var status: String,
        val updated_at: String,
        val user_id: String
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