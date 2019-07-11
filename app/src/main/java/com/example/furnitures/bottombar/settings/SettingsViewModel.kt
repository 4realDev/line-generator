package com.example.furnitures.bottombar.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.furnitures.calculator.trick.RepositoryFactory

class SettingsViewModel(application: Application) : AndroidViewModel(application), SettingsContract.ViewModel {

    override fun setDifficulty(difficulty: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMaxNumbOfTricks(maxNumbOfTricks: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var getInputEvent: MutableLiveData<Boolean>
    private val repository = RepositoryFactory.getFurnitureRepository()
}