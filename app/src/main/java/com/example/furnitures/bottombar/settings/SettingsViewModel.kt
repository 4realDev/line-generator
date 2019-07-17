package com.example.furnitures.bottombar.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.furnitures.bottombar.settings.SettingsService
import com.example.furnitures.trick.FurnitureDifficulty

class SettingsViewModel(application: Application) : AndroidViewModel(application), SettingsContract.ViewModel {

    override fun setMaxTricks(maxTricks: Int) {
        SettingsService.saveMaxTricks(getApplication(), maxTricks)
    }

    override fun setDifficulty(difficulty: FurnitureDifficulty) {
        SettingsService.saveDifficulty(getApplication(), difficulty)
    }
}