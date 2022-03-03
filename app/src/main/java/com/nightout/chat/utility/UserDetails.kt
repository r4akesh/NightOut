package com.nightout.chat.utility


import com.nightout.model.FSUsersModel
import com.nightout.utils.DataManager
import java.util.*

class UserDetails {
    //	public static String roomId = "";

    companion object {
        var userDeatil : UserDetails?=null
        val instance: UserDetails
            get() {

                if (userDeatil == null) {
                    userDeatil = UserDetails()
                    return userDeatil!!
                } else {
                    return userDeatil as UserDetails
                }

            }
    }

    // var chatUsers: HashMap<String, FSUsersModel> = HashMap<String, FSUsersModel>()
    //lateinit var  myDetail:FSUsersModel
//	public static boolean isGroup = false;
    //	public static FSGroupModel groupDetails;
}