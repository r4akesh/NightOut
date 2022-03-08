package com.nightout.chat.model

import com.google.gson.annotations.SerializedName

//{"status":{"error":false,"status":{"message":"Success","code":200}},
//    "data":{"status":200,"file_path":"http://testapi.newdevpoint.in/files/0e535aad60d8c4edbbc67aacd6968bbe.jpg"}}

  class UploadFileMode {
    @SerializedName("thumbnail")
    val thumbnail: String? = null

    @SerializedName("file")
    val file: String? = null

    @SerializedName("updated_at")
    val updatedAt: String? = null

    @SerializedName("user_id")
    val userId: String? = null

    @SerializedName("created_at")
    val createdAt: String? = null

    @SerializedName("id")
    val id: String? = null

    @SerializedName("channel_id")
    val channelId: String? = null
}