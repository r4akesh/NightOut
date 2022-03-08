package com.nightout.chat.model

data class UploadFileModeNew(
    val `data`: Data,
    val status: Status
) {

    data class Data(
        val file_path: String,
        val status: Int
    )

    data class Status(
        val error: Boolean,
        val status: StatusX
    )

    data class StatusX(
        val code: Int,
        val message: String
    )
}