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

    var isNotificationOpen: Boolean
        get() = prefs!!.getBoolean(AppConstant.PrefsName.ISNotificationOpen, false)
        set(islogin) {
            prefs!!.edit().putBoolean(AppConstant.PrefsName.ISNotificationOpen, islogin)
                .apply()
        }

    var isUpdatedBarcrwalSuccesfully: Boolean
        get() = prefs!!.getBoolean(AppConstant.PrefsName.IsUpdatedBarcrwalSuccesfully, false)
        set(islogin) {
            prefs!!.edit().putBoolean(AppConstant.PrefsName.IsUpdatedBarcrwalSuccesfully, islogin)
                .apply()
        }
    var isUserDeviceAPICall: Boolean
        get() = prefs!!.getBoolean(AppConstant.PrefsName.IS_USERDEVICE_API, false)
        set(islogin) {
            prefs!!.edit().putBoolean(AppConstant.PrefsName.IS_USERDEVICE_API, islogin)
                .apply()
        }

     var bearerTokenSave: String?
        get() = prefs!!.getString(AppConstant.PrefsName.BearerTOKEN, "")
        set(cnt) {
            prefs!!.edit().putString(AppConstant.PrefsName.BearerTOKEN, cnt).apply()
        }
    var fcmTokenSave: String?
        get() = prefs!!.getString(AppConstant.PrefsName.FCMTOKEN, "")
        set(cnt) {
            prefs!!.edit().putString(AppConstant.PrefsName.FCMTOKEN, cnt).apply()
        }

    var imgPathSave: String?
        get() = prefs!!.getString(AppConstant.PrefsName.IMAGE_PATH, "")
        set(cnt) {
            prefs!!.edit().putString(AppConstant.PrefsName.IMAGE_PATH, cnt).apply()
        }

    var currentLat: String?
        get() = prefs!!.getString(AppConstant.PrefsName.LATITUDE_CURRENT, "")
        set(cnt) {
            prefs!!.edit().putString(AppConstant.PrefsName.LATITUDE_CURRENT, cnt).apply()
        }

    var currentLong: String?
        get() = prefs!!.getString(AppConstant.PrefsName.LONGITUDE_CURRENT, "")
        set(cnt) {
            prefs!!.edit().putString(AppConstant.PrefsName.LONGITUDE_CURRENT, cnt).apply()
        }

    var currentAddrs: String?
        get() = prefs!!.getString(AppConstant.PrefsName.ADDRESS_CURRENT, "")
        set(cnt) {
            prefs!!.edit().putString(AppConstant.PrefsName.ADDRESS_CURRENT, cnt).apply()
        }

    var currentCity: String?
        get() = prefs!!.getString(AppConstant.PrefsName.CITY_CURRENT, "")
        set(cnt) {
            prefs!!.edit().putString(AppConstant.PrefsName.CITY_CURRENT, cnt).apply()
        }
    var currentFilterValue: String?
        get() = prefs!!.getString(AppConstant.PrefsName.FilterValue, "")
        set(cnt) {
            prefs!!.edit().putString(AppConstant.PrefsName.FilterValue, cnt).apply()
        }
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