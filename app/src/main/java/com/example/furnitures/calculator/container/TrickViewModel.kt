package com.example.furnitures.calculator.container.selection

import android.app.Application
import android.arch.lifecycle.*
import com.example.furnitures.calculator.trick.*
import java.util.*

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
class TrickViewModel(application: Application) : AndroidViewModel(application), NewTrickContract.ViewModel {

    private val repository = RepositoryFactory.getFurnitureRepository()
    private var viewStateData = MutableLiveData<ViewState>()
//    private val selectedFurnitures = repository.getSelectedFurnitures()
//    private val selectedFurnituresViewState = Transformations.map(selectedFurnitures, ::mapList)

    private val random = Random()

    init {
        if(viewStateData.value == null)
            viewStateData.value = ViewState.INITIAL_STATE
    }


    override fun getViewState(): LiveData<ViewState> = viewStateData

//    override fun getSelectedFurnitureList(): LiveData<List<FurnitureViewState>> {
//        return selectedFurnituresViewState
//    }

    override fun changeViewState() {
        when (viewStateData.value) {
            ViewState.INITIAL_STATE -> viewStateData.value = ViewState.CHANGED_STATE
            ViewState.CHANGED_STATE -> viewStateData.value = ViewState.INITIAL_STATE
        }
    }

//    private fun map(furniture: Furniture): FurnitureViewState {
//        return FurnitureViewState(
//            id = furniture.id,
//            furnitureType = furniture.furnitureType,
//            furnitureCategory = furniture.furnitureCategory,
//            name = FurnitureTypeHelper.getString(furniture.furnitureType),
//            drawableResId = FurnitureTypeHelper.getDrawable(furniture.furnitureType),
//            isSelected = furniture.isSelected
//        )
//    }
//
//    private fun mapList(furnitureList: List<Furniture>): List<FurnitureViewState> {
//        return furnitureList
//            .map { map(it) }
//            .shuffled()
//    }


}