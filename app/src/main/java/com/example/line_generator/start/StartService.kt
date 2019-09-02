package com.example.line_generator.start

import android.content.Context
import android.content.SharedPreferences

private const val FIRST_DATABASE_INIT = "first_database_init"
private const val PREF_DATABASE_INIT_FILE_NAME = "database_init_preferences"

class StartService {

    fun saveIsInitialized(context: Context, isInitialized: Boolean) {
        sharedPreferences(context).edit().putBoolean(FIRST_DATABASE_INIT, isInitialized).apply()
    }

    fun getIsInitialized(context: Context): Boolean {
        return sharedPreferences(context).getBoolean(FIRST_DATABASE_INIT, false)
    }

    private fun sharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_DATABASE_INIT_FILE_NAME, Context.MODE_PRIVATE)
}