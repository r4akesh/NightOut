package com.nightout.chat.model

import com.google.gson.annotations.SerializedName
import com.nightout.model.FSUsersModel

class RoomResponseModel {
    @SerializedName("roomList")
    var roomList = ArrayList<FSRoomModel>()

    @SerializedName("userList")
    var userList = ArrayList<FSUsersModel>()
    val userListMap: HashMap<String, FSUsersModel>
        get() {
            val tmpHashList = HashMap<String, FSUsersModel>()
            for (element in userList) {
                tmpHashList[element.id] = element
            }
            return tmpHashList
        }
}