package com.example.furnitures.bottombar.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.furnitures.trick.Furniture
import com.example.furnitures.trick.FurnitureTypeHelper
import com.example.furnitures.trick.FurnitureViewState
import com.example.furnitures.trick.RepositoryFactory

// index = 0 -> position = 1

class ListTrickViewModel(application: Application) : AndroidViewModel(application), ListTrickContract.ViewModel {

    private val repository = RepositoryFactory.getFurnitureRepository()

    // dynamisch Änderungen müssen am ListTrickView nichtmehr übernommen werden
    // Verwendung von statische Liste
    // LiveData nur sinnvoll, wenn beide Screen parallel laufen könnten
    private val selectedFurnituresList = repository.getSelectedFurnituresList()
    private val shuffledSelectedFurnitureList = selectedFurnituresList.shuffled()

    // List wird übergeben anstatt LiveData
    // Transformations.map auf selectedFurnituresLIVEDATA wird nicht aufgerufen
    // -> arbeiten mit der Liste, abspeichern (wenn gewollt) dann verwerfen
    // -> wenn Fragment verworfen wird, wird Liste verworfen
    // -> Scoping liegt nämlich auf Fragment (nicht auf Activity (this statt activity))
    // -> viewModel = ViewModelProviders.of(this).get(ListTrickViewModel::class.java)
    private val selectedFurnitureListViewState = MutableLiveData<List<FurnitureViewState>>()

    init {
        // einmalig, da kein Transformations.map
        // Anonyme Funktion -> ruft mapIndexed(index, it) auf und springt da rein
        // selectedFurnitureListViewState.value = selectedFurnituresList.map{map(index, it)}
        // Methoden Referenz
        // for-Schleife beider ein Element der Liste in ein anderes Element der Liste gemappt wird
        // index = Listen Position
        selectedFurnitureListViewState.value = shuffledSelectedFurnitureList.mapIndexed(::map)
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
        // Die Position, darf aber nicht direkt verändert werden (VAR position -> furniture.position = index + 1)
        // Das würde zu einer Veränderung in allen Instancen führen (auch im Adapter)
        // Daten Properties sollen immer VAL (final sein) & so nur durch mapping "veränderbar"
        newSwapUnitList = newSwapUnitList.mapIndexed { index, furnitureViewState ->
            // Wenn der index größer gleich fromPosition ist && der index kleiner toPosition ist
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
            name = FurnitureTypeHelper.getString(furniture.furnitureType),
            userCreatedName = furniture.userCreateName,
            drawableResId = FurnitureTypeHelper.getDrawable(furniture.furnitureType),
            isSelected = furniture.isSelected
        )
    }
}