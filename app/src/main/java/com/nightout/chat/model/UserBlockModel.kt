package com.nightout.chat.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserBlockModel : Serializable {
    @SerializedName("blockedBy")
    var blockedBy = ""

    @SerializedName("blockedTo")
    var blockedTo = ""

    @SerializedName("isBlock")
    var isBlock = false
}