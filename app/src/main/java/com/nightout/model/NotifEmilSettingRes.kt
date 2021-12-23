package com.nightout.model

data class NotifEmilSettingRes(
    val `data`: Data,
    val message: String,
    val response: String,
    val status_code: Int
) {

    data class Data(
        val emailsetting: Emailsetting,
        val notificationsetting: Notificationsetting
    )

    data class Emailsetting(
        val created_at: String,
        val id: Int,
        val status: String,
        val updated_at: String,
        val user_id: Int
    )

    data class Notificationsetting(
        val created_at: String,
        val id: Int,
        val status: String,
        val updated_at: String,
        val user_id: Int
    )
}