package com.example.line_generator.bottombar.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.line_generator.bottombar.create.SettingsContract
import com.example.line_generator.data.trick.Difficulty

class SettingsViewModel(application: Application) : AndroidViewModel(application), SettingsContract.ViewModel {

    private val difficultyData = MutableLiveData<Difficulty>()
    private val difficultyDataPercentage = Transformations.map(difficultyData, ::mapToPercentage)

    init {
        difficultyData.value = SettingsService.getDifficulty(application)
    }

    override fun getDifficulty(): LiveData<Difficulty> = difficultyData
    override fun getDifficultyPercentage(): LiveData<FloatArray> = difficultyDataPercentage

//    override fun setTricksPercentage(trickPercentageArray: IntArray) {
//        SettingsService.saveTricksPercentage(getApplication(), trickPercentageArray)
//    }

//    override fun calculateDifficultyPercentage(difficulty: difficulty): FloatArray {
//        return when(difficulty){
//            difficulty.SAVE -> floatArrayOf(100f, 0f, 0f, 0f, 0f)
//            difficulty.EASY -> floatArrayOf(50f, 50f, 0f, 0f, 0f)
//            difficulty.MIDDLE -> floatArrayOf(25f, 25f, 50f, 0f, 0f)
//            difficulty.HARD -> floatArrayOf(15f, 15f, 20f, 50f, 0f)
//            difficulty.CRAZY -> floatArrayOf(5f, 5f, 15f, 25f, 50f)
//        }
//    }

    override fun onMaxTricksSelected(maxTricks: Int) {
        SettingsService.saveMaxTricks(getApplication(), maxTricks)
    }

    override fun onDifficultySelected(difficulty: Difficulty) {
        difficultyData.value = difficulty
        SettingsService.saveDifficulty(getApplication(), difficulty)
    }

    fun mapToPercentage(difficulty: Difficulty): FloatArray{
        return when(difficulty){
            Difficulty.SAVE -> floatArrayOf(100f, 0f, 0f, 0f, 0f)
            Difficulty.EASY -> floatArrayOf(50f, 50f, 0f, 0f, 0f)
            Difficulty.MIDDLE -> floatArrayOf(25f, 25f, 50f, 0f, 0f)
            Difficulty.HARD -> floatArrayOf(15f, 15f, 20f, 50f, 0f)
            Difficulty.CRAZY -> floatArrayOf(5f, 5f, 15f, 25f, 50f)
        }
    }

}