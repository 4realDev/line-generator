package com.example.line_generator.bottombar.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.line_generator.extensions.randomUUID
import com.example.line_generator.trick.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CreateViewModel(application: Application) : AndroidViewModel(application), CreateContract.ViewModel {

    private val repository: TricksRepositoryImp

    init {
        val trickDao = TrickRoomDatabase.getDatabase(application, viewModelScope).trickDao()
        repository = TricksRepositoryImp(trickDao)
    }

    override fun createTrick(name: String, directionIn: DirectionIn, directionOut: DirectionOut, category: Category, difficulty: Difficulty) {
        // new Typ needed -> USER_CREATED
        var categoryDependentType = when (category) {
            Category.SLIDE -> TrickType.USER_CREATED_SLIDE
            Category.GRIND -> TrickType.USER_CREATED_GRINDE
            Category.OTHER -> TrickType.USER_CREATED_OTHER
        }

        val newTrick = TrickViewState(
            id = randomUUID(),
            position = 0,
            trickType = categoryDependentType,
            directionIn = directionIn,
            directionOut = directionOut,
            category = category,
            difficulty = difficulty,
            name = null,
            userCreatedName = name,
            drawableResId = TrickTypeHelper.getDrawable(categoryDependentType),
            selected = false
        )
        insert(newTrick)
    }

    // Getter for a "Job" to insert the data in coroutine in a non-blocking way
    override fun insert(trickViewState: TrickViewState): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            repository.insert(trickViewState)
        }
    }

    // Job stoppen, wenn ViewModel destroyed wird -> vermeidet memory leak vom ViewModel
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}