package com.example.furnitures.bottombar.selection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.furnitures.calculator.trick.*

private const val START_HEADER_POSITION: Int = 0

// ID sollten immer eindeutig sein in ihrem Context
// Sehr unwahrscheinlich das in diesem Context in dieser Liste FIRST_HEADER_ID nochmal verwendet wird
// Selbst gewählte ID besser zum Debuggen und zum Verständnis des Entwicklers - d.h. kein randomUUID()
private const val FIRST_HEADER_ID: String = "FIRST_HEADER_ID"
private const val SLIDE_HEADER_ID: String = "SLIDE_HEADER_ID"
private const val OTHER_HEADER_ID: String = "OTHER_HEADER_ID"

class SelectionViewModel(application: Application) : AndroidViewModel(application), FurnitureContract.ViewModel{

    private val repository = RepositoryFactory.getFurnitureRepository()
    private val furnitures = repository.getSortedFurnitures()
    private val furnituresViewState = Transformations.map(furnitures, ::mapList)
    private val furnituresWithHeader = Transformations.map(furnituresViewState, ::addHeaders)

    override fun onFurnitureClicked(furniture: FurnitureViewState) {
        repository.updateFurnitureOnClick(furniture)
    }

    override fun getFurnitureViewStateWithHeader(): LiveData<List<RowViewState>> = furnituresWithHeader

    private fun mapList(furnitureList: List<Furniture>): List<RowViewState> {
        return furnitureList
            .map { mapToViewState(it) }
    }

    private fun mapToViewState(furniture: Furniture): FurnitureViewState {
        return FurnitureViewState(
            id = furniture.id,
            position = furniture.position,
            furnitureType = furniture.furnitureType,
            furnitureCategory = furniture.furnitureCategory,
            name = FurnitureTypeHelper.getString(furniture.furnitureType),
            userCreatedName = furniture.userCreateName,
            drawableResId = FurnitureTypeHelper.getDrawable(furniture.furnitureType),
            isSelected = furniture.isSelected
        )
    }

    private fun addHeaders(furnitureList: List<RowViewState>): List<RowViewState> {
        val completeRowViewStateList = furnitureList.toMutableList()
        var headerIndexGrind: Int
        var headerIndexSlide: Int? = null
        var headerIndexOther: Int? = null

        var grindBoolean: Boolean = false
        var slideBoolean: Boolean = false
        var otherBoolean: Boolean = false

        fun getHeaderIndexGrind(): Int? {
            headerIndexGrind = START_HEADER_POSITION
            return headerIndexGrind
        }

        fun getHeaderIndexSlide(): Int? {
            completeRowViewStateList.forEachIndexed { index, rowViewState ->
                if (rowViewState.furnitureCategory == FurnitureCategory.GRIND && index != completeRowViewStateList.lastIndex) {
                    if (completeRowViewStateList[index].furnitureCategory != completeRowViewStateList[index + 1].furnitureCategory) {
                        headerIndexSlide = index + 1
                    }
                }
            }
            return headerIndexSlide
        }

        fun getHeaderIndexOther(): Int? {
            completeRowViewStateList.forEachIndexed { index, rowViewState ->
                if (rowViewState.furnitureCategory == FurnitureCategory.SLIDE && index != completeRowViewStateList.lastIndex) {
                    if (completeRowViewStateList[index].furnitureCategory != completeRowViewStateList[index + 1].furnitureCategory) {
                        headerIndexOther = index + 1
                    }
                }
            }
            return headerIndexOther
        }

        completeRowViewStateList.forEach {
            if (it is FurnitureViewState) {
                if(it.isSelected && it.furnitureCategory == FurnitureCategory.GRIND) grindBoolean = true
                if(it.isSelected && it.furnitureCategory == FurnitureCategory.SLIDE) slideBoolean = true
                if(it.isSelected && it.furnitureCategory == FurnitureCategory.OTHER) otherBoolean = true
            }
        }

        completeRowViewStateList.add(getHeaderIndexGrind()!!, HeaderViewState(FIRST_HEADER_ID, getHeaderIndexGrind()!!, FurnitureCategory.GRIND, grindBoolean))
        completeRowViewStateList.add(getHeaderIndexSlide()!!, HeaderViewState(SLIDE_HEADER_ID, getHeaderIndexSlide()!!, FurnitureCategory.SLIDE, slideBoolean))
        completeRowViewStateList.add(getHeaderIndexOther()!!, HeaderViewState(OTHER_HEADER_ID, getHeaderIndexOther()!!, FurnitureCategory.OTHER, otherBoolean))

        return completeRowViewStateList
    }
}