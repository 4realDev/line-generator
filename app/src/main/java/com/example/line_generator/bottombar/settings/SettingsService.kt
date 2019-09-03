package com.example.line_generator.bottombar.settings

import android.content.Context
import android.content.SharedPreferences
import com.example.line_generator.data.trick.Difficulty

object SettingsService {
    private const val PREF_FILE_NAME = "settings_preferences"
    private const val DIFFICULTY_STORAGE = "difficulty"
    private const val MAX_TRICKS_STORAGE = "max_tricks"
    //defValue nur sichtbar bei Neuinstallieren der App
    private const val MAX_TRICKS_DEFAULT_VALUE = 5

    fun saveMaxTricks(context: Context, maxTricks: Int){
        sharedPreferences(context).edit().putInt(MAX_TRICKS_STORAGE, maxTricks).apply()
    }

    fun getMaxTricks(context: Context): Int{
        return sharedPreferences(context).getInt(MAX_TRICKS_STORAGE, MAX_TRICKS_DEFAULT_VALUE)
    }

    fun saveDifficulty(context: Context, difficulty: Difficulty) {
        sharedPreferences(context).edit().putString(DIFFICULTY_STORAGE, difficulty.name).apply()
    }

    fun getDifficulty(context: Context): Difficulty {
        return Difficulty.valueOf(sharedPreferences(context).getString(DIFFICULTY_STORAGE, Difficulty.MIDDLE.name) ?: Difficulty.MIDDLE.name)
    }

    private fun sharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
}