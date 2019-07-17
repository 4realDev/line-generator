package com.example.furnitures.bottombar.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.furnitures.extensions.randomUUID
import com.example.furnitures.trick.*

class CreateViewModel(application: Application) : AndroidViewModel(application), CreateContract.ViewModel {

    private lateinit var getInputEvent: MutableLiveData<Boolean>
    private val repository = RepositoryFactory.getFurnitureRepository()

    override fun getCheckInputEvent(): LiveData<Boolean> = getInputEvent

    override fun checkInput(input: String?) {
        if (input == null)
            getInputEvent.value = false
        else getInputEvent.value = true
    }

    override fun createTrick(name: String, category: FurnitureCategory, difficulty: FurnitureDifficulty) {
        // new Typ needed -> USER_CREATED
        var categoryDependentType = when (category) {
            FurnitureCategory.SLIDE -> FurnitureType.USER_CREATED_SLIDE
            FurnitureCategory.GRIND -> FurnitureType.USER_CREATED_GRINDE
            FurnitureCategory.OTHER -> FurnitureType.USER_CREATED_OTHER
            else -> throw IllegalStateException("\"Could not find FurnitureCategory\" + $category")
        }

        val newFurniture = FurnitureViewState(
            id = randomUUID(),
            position = 0,
            furnitureType = categoryDependentType,
            furnitureCategory = category,
            furnitureDifficulty = difficulty,
            name = null,
            userCreatedName = name,
            drawableResId = FurnitureTypeHelper.getDrawable(categoryDependentType),
            isSelected = false
        )

        repository.createFurniture(newFurniture)
    }
}