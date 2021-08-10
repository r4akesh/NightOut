package com.nightout.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.google.gson.Gson
import com.nightout.model.LoginModel

/**
 * Class is used to save user data in preference.
 */
class PreferenceKeeper private constructor(context: Context?) {
    private var prefs: SharedPreferences? = null
    var PRIVATE_MODE = 0
    fun clearData() {
        val editor = prefs!!.edit()
        editor.clear()
        editor.apply()
    }

   var loginResponse: LoginModel.Data?
        get() = Gson().fromJson(prefs!!.getString(AppConstant.PrefsName.LOGIN_POJO, ""), LoginModel.Data::class.java)
        set(type) {
            val json = Gson().toJson(type)
            prefs!!.edit().putString(AppConstant.PrefsName.LOGIN_POJO, json.toString()).apply()
        }

    var isUserLogin: Boolean
        get() = prefs!!.getBoolean(AppConstant.PrefsName.IS_LOGIN, false)
        set(islogin) {
            prefs!!.edit().putBoolean(AppConstant.PrefsName.IS_LOGIN, islogin)
                .apply()
        }



    /*var userNotfCountUnRead: String?
        get() = prefs!!.getString(AppConstant.PrefsName.NOTF_COUNT_UNREAD, "")
        set(cnt) {
            prefs!!.edit().putString(AppConstant.PrefsName.NOTF_COUNT_UNREAD, cnt).apply()
        }*/






    companion object {
        private var keeper: PreferenceKeeper? = null
        private var context: Context? = null
        @JvmStatic
        val instance: PreferenceKeeper
            get() {
                if (keeper == null) {
                    keeper = PreferenceKeeper(context)
                }
                return keeper as PreferenceKeeper
            }

        fun setContext(ctx: Context?) {
            context = ctx
        }
    }

    init {
        if (context != null) prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }
}