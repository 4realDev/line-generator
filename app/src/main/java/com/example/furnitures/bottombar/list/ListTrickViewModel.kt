package com.example.furnitures.bottombar.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.furnitures.bottombar.settings.SettingsService
import com.example.furnitures.trick.*
import kotlin.math.pow
import kotlin.math.roundToInt

// numbPickerIndex = 0 -> position = 1

class ListTrickViewModel(application: Application) : AndroidViewModel(application), ListTrickContract.ViewModel {

    private val repository = RepositoryFactory.getFurnitureRepository()

    // dynamisch Änderungen müssen am ListTrickView nichtmehr übernommen werden
    // Verwendung von statische Liste
    // LiveData nur sinnvoll, wenn beide Screen parallel laufen könnten
    private val selectedFurnituresList = repository.getSelectedFurnituresList()
    private val shuffledSelectedFurnitureList = randomizeWithSettings(selectedFurnituresList)
//    private val shuffledSelectedFurnitureList = selectedFurnituresList.shuffled()

    // List wird übergeben anstatt LiveData
    // Transformations.map auf selectedFurnituresLIVEDATA wird nicht aufgerufen
    // -> arbeiten mit der Liste, abspeichern (wenn gewollt) dann verwerfen
    // -> wenn Fragment verworfen wird, wird Liste verworfen
    // -> Scoping liegt nämlich auf Fragment (nicht auf Activity (this statt activity))
    // -> viewModel = ViewModelProviders.of(this).get(ListTrickViewModel::class.java)
    private val selectedFurnitureListViewState = MutableLiveData<List<FurnitureViewState>>()

    init {
        // einmalig, da kein Transformations.map
        // Anonyme Funktion -> ruft mapIndexed(numbPickerIndex, it) auf und springt da rein
        // selectedFurnitureListViewState.value = selectedFurnituresList.map{map(numbPickerIndex, it)}
        // Methoden Referenz
        // for-Schleife beider ein Element der Liste in ein anderes Element der Liste gemappt wird
        // numbPickerIndex = Listen Position
        selectedFurnitureListViewState.value = shuffledSelectedFurnitureList.mapIndexed(::map)
    }

    private fun randomizeWithSettings(list: List<Furniture>): List<Furniture> {

        var newRandomizedList = emptyList<Furniture>().toMutableList()

        val choosenMaxTricks = SettingsService.getMaxTricks(getApplication())
        val choosenDifficulty = SettingsService.getDifficulty(getApplication())

        val arrayItems = choosenDifficulty.weight

        val difficultyArray: Array<FurnitureDifficulty> = enumValues()

        val difficultyPercentageArray = FloatArray(arrayItems)
        val numberOfTricksArray = IntArray(arrayItems)
        // Berechnung des Nenners im Bruch -> x/fraction
        val fraction = (2.0f.pow(arrayItems - 1) - 1) * 2

        for (index in 0 until arrayItems) {


            // forelast element -> make the last the sum of the percentageArray
            if (index == arrayItems - 1)
                difficultyPercentageArray[difficultyPercentageArray.lastIndex] = difficultyPercentageArray.sum()
            // calculate the percentage for each difficulty ((2^index)/fraction)
            else
                difficultyPercentageArray[index] = ((2.0f.pow(index) / fraction))

            // calculate the number of tricks for each difficulty (maxTricks * percentage)
            numberOfTricksArray[index] = (choosenMaxTricks * difficultyPercentageArray[index]).roundToInt()


            // get filteredList for each difficulty
            // shuffle filteredList, to select random set
            var filteredList = list
                .filter { it.furnitureDifficulty == difficultyArray[index] }
                .shuffled()

            // if difficulty exists
            if (filteredList.isNotEmpty()) {
                // existed elements < calculated, requested elements -> add all existed elements
                // requested 5 - existed 3 -> add all 3
                if (numberOfTricksArray[index] > filteredList.size)
                    newRandomizedList.addAll(filteredList.subList(0, filteredList.size))
                // existed elements > calculated, requested elements -> add the calculated number of elements
                else
                    newRandomizedList.addAll(filteredList.subList(0, numberOfTricksArray[index]))
            }


        }


        // BUGGY
        // remove elements while the randomizedList > choosenMaxTricks
        // always remove the first element (the easiest difficulty)
//        if(newRandomizedList.size > choosenMaxTricks) {
//            val removedItemList = newRandomizedList
//            while (newRandomizedList.size > choosenMaxTricks) {
//                removedItemList.minus(removedItemList.first())
//            }
//            newRandomizedList = removedItemList
//        }
//
        // temporäre Lösung
//        if(newRandomizedList.size < choosenMaxTricks) {
//            val addItemList = newRandomizedList
//            while (newRandomizedList.size < choosenMaxTricks) {
//                addItemList.plus(list.random())
//            }
//        }


        return newRandomizedList.shuffled()

        // region oldcode
//        when (choosenDifficulty) {
//
//            newRandomizedList.addAll(list
//                .filter { it.furnitureDifficulty == choosenDifficulty }
//                .subList(0, numberOfTricksArray[choosenDifficulty.weight])
//
//            FurnitureDifficulty.JOKE -> {
//                newRandomizedList.addAll(list
//                    .filter { it.furnitureDifficulty == FurnitureDifficulty.JOKE }
//                    .ifEmpty { return emptyList() }
//                    .subList(0, numberOfTricksArray[0])
//                )
//            }
//
//            FurnitureDifficulty.EASY -> {
//                newRandomizedList.addAll(list
//                    .filter { it.furnitureDifficulty == FurnitureDifficulty.JOKE }
//                    .ifEmpty { emptyList() }
//                    .subList(0, numberOfTricksArray[0])
//                )
//                newRandomizedList.addAll(list
//                    .filter { it.furnitureDifficulty == FurnitureDifficulty.EASY }
//                    .ifEmpty { emptyList() }
//                    .subList(0, numberOfTricksArray[1])
//                )
//            }
//
//            FurnitureDifficulty.MIDDLE -> {
//                val remainingNumOfJokeItems = list.size * 0.165
//                val remainingNumOfEasyItems = list.size * 0.335
//                val remainingNumOfMiddleItems = list.size * 0.5
//                newRandomizedList.addAll(list
//                    .filter { it.furnitureDifficulty == FurnitureDifficulty.JOKE }
//                    .dropWhile { list.size > remainingNumOfJokeItems }
//                )
//                newRandomizedList.addAll(list
//                    .filter { it.furnitureDifficulty == FurnitureDifficulty.EASY }
//                    .dropWhile { list.size > remainingNumOfEasyItems }
//                )
//                newRandomizedList.addAll(list
//                    .filter { it.furnitureDifficulty == FurnitureDifficulty.MIDDLE }
//                    .dropWhile { list.size > remainingNumOfMiddleItems }
//                )
//            }
//
//            FurnitureDifficulty.HARD -> newRandomizedList = list.toMutableList() //.dropWhile { maximumListValue < list.size }.toMutableList()
//            FurnitureDifficulty.CRAZY -> newRandomizedList = list.toMutableList()
//        }
//        return newRandomizedList.shuffled()
        // endregion
    }

    override fun getSelectedItemsViewState(): LiveData<List<FurnitureViewState>> = selectedFurnitureListViewState

    override fun getFurnitureAt(position: Int): FurnitureViewState = selectedFurnitureListViewState.value.orEmpty()[position]

    override fun changeFurnitureItemPosition(fromPosition: Int, toPosition: Int) {

        // Wenn fromPosition auf dem View über toPosition liegt (der Index kleiner ist)
        // Dann soll fromPosition - fromPosition bleiben (StartPosition)

        // Variablen dürfen nicht den selben namen haben, sonst switched es zurück

        val startPosition = if (fromPosition < toPosition) fromPosition else toPosition
        val endPosition = if (fromPosition < toPosition) toPosition else fromPosition


        var newSwapUnitList = selectedFurnitureListViewState.value.orEmpty().toMutableList()
        val removed = newSwapUnitList.removeAt(fromPosition)
        newSwapUnitList.add(toPosition, removed)

        // Überschreibe die Liste mit der neuen gemappten Liste
        // Am Anfang ist gemappte Liste leer
        // Befindet sich das Item in der gesuchten Position, kopiere dieses mit neuer Position
        // Falls nicht, übertrage das Item eins zu eins in die gemappte Liste
        // Die Position, darf aber nicht direkt verändert werden (VAR position -> furniture.position = numbPickerIndex + 1)
        // Das würde zu einer Veränderung in allen Instancen führen (auch im Adapter)
        // Daten Properties sollen immer VAL (final sein) & so nur durch mapping "veränderbar"
        newSwapUnitList = newSwapUnitList.mapIndexed { index, furnitureViewState ->
            // Wenn der numbPickerIndex größer gleich fromPosition ist && der numbPickerIndex kleiner toPosition ist
            // Von - Bis sollen alle Items aktualisiert werden
            if (index in startPosition..endPosition) {
                furnitureViewState.copy(position = index + 1)
            } else furnitureViewState
        }.toMutableList()

        selectedFurnitureListViewState.value = newSwapUnitList

        Log.d("DEBUGG", "fromViewModelPosition: $fromPosition toViewModelPosition: $toPosition")
    }

    override fun removeFurnitureItem(furnitureViewState: FurnitureViewState) {
        var newRemoveUnit = selectedFurnitureListViewState.value?.toMutableList()
        val removeIndex = furnitureViewState.position - 1
        newRemoveUnit?.removeAt(removeIndex)
        newRemoveUnit = newRemoveUnit?.mapIndexed { index, furnitureViewState ->
            if (index + 1 <= furnitureViewState.position) {
                furnitureViewState.copy(position = index + 1)
            } else furnitureViewState
        }?.toMutableList()
        selectedFurnitureListViewState.value = newRemoveUnit
        //selectedFurnitureListViewState.value = selectedFurnitureListViewState.value?.minus(furnitureViewState)
    }

    private fun map(index: Int, furniture: Furniture): FurnitureViewState {
        return FurnitureViewState(
            id = furniture.id,
            position = index + 1,
            furnitureType = furniture.furnitureType,
            furnitureCategory = furniture.furnitureCategory,
            furnitureDifficulty = furniture.furnitureDifficulty,
            name = FurnitureTypeHelper.getString(furniture.furnitureType),
            userCreatedName = furniture.userCreateName,
            drawableResId = FurnitureTypeHelper.getDrawable(furniture.furnitureType),
            isSelected = furniture.isSelected
        )
    }
}