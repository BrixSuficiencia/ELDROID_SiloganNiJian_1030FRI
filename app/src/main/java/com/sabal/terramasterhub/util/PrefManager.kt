package com.sabal.terramasterhub.util

import android.content.Context
import android.content.SharedPreferences

object PrefManager {
    private const val PREF_NAME = "TerraMasterPrefs"
    private const val TOKEN_KEY = "token"
    private const val USER_INFO_KEY = "user_info"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        return getSharedPreferences(context).getString(TOKEN_KEY, null)
    }

    fun saveUserInfo(context: Context, userInfo: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(USER_INFO_KEY, userInfo)
        editor.apply()
    }

    fun getUserInfo(context: Context): String? {
        return getSharedPreferences(context).getString(USER_INFO_KEY, null)
    }

    fun clear(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
    }
}
