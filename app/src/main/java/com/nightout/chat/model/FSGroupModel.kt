package com.nightout.chat.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FSGroupModel : Serializable {
    @SerializedName("group_name")
    var group_name = ""

    @SerializedName("about_group")
    var about_group = ""

    @SerializedName("about_pic")
    var about_pic = ""

    @SerializedName("create_date")
    var create_date = ""

    @SerializedName("thumbnail")
    var thumbnail = ""


}