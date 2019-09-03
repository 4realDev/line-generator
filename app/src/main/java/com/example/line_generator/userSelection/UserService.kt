package com.example.line_generator.userSelection

import android.content.Context
import android.content.SharedPreferences

private const val USER_ID_STORAGE = "user_id_storage"
private const val USER_NAME_STORAGE = "user_name_storage"
private const val PREF_USER_ID_FILE = "user_id_preferences"

class UserService(private val context: Context) {

    fun saveUserId(userId: String) {
        sharedPreferences().edit().putString(USER_ID_STORAGE, userId).apply()
    }

    fun getUserId(): String {
        return sharedPreferences().getString(USER_ID_STORAGE, "")
    }

    fun saveUserName(userId: String) {
        sharedPreferences().edit().putString(USER_NAME_STORAGE, userId).apply()
    }

    fun getUserName(): String {
        return sharedPreferences().getString(USER_NAME_STORAGE, "")
    }

    private fun sharedPreferences(): SharedPreferences =
        context.getSharedPreferences(PREF_USER_ID_FILE, Context.MODE_PRIVATE)
}