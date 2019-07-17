package com.example.furnitures.trick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.furnitures.extensions.randomUUID

class FurnitureRepositoryImp : FurnitureRepository {

    // Transformations.map wird erst Aufgerufen, wenn die LiveData Observed wird.

    private val furnitures = MutableLiveData<List<Furniture>>()
    // Mapping Funktion findet nur statt, wenn selectedFurnitures bereits observed wird!!!
    //private val selectedFurnitures = Transformations.map(furnitures, ::filterSelect)

    init {
        furnitures.value = createInitialFurnitures()
    }

    // sortierung vorher behebt fehler beim header einfügen
    override fun getSortedFurnitures(): LiveData<List<Furniture>>{
        sortFurnitures()
        return furnitures
    }

    //override fun getSelectedFurnitures(): LiveData<List<Furniture>> = selectedFurnitures

    override fun getSelectedFurnituresList(): List<Furniture> {
        // Collections sollten nie null sein
        return furnitures.value.orEmpty().filter { it.isSelected }
    }

    override fun getFurnitureById(id: String): Furniture? {
        lateinit var outputFurniture: Furniture
        furnitures.value?.forEach { furniture ->
            if (id == furniture.id) {
                outputFurniture = furniture
            }
        }
        return outputFurniture
    }

    override fun createFurniture(furnitureViewState: FurnitureViewState) {
        val furniture = map(furnitureViewState)
        furnitures.value = furnitures.value?.plus(furniture)
        sortFurnitures()
    }

    override fun deleteFurnitureById(id: String) {

    }

    override fun updateFurnitureOnClick(clickedFurniture: FurnitureViewState) {
        //if (furnitureListViewState.value == null) throw IllegalStateException("WTF Liste sollte nicht null sein wenn schon eins heklickt wurde")
        //val clickedFurnite = furnitureListViewState.value!!.find { it.id == furniture.id }
        //if (clickedFurnite == null) throw IllegalStateException("Wie kann etwas geklickt worden sein dass ich als ViewModel nicht kenne?")

        // Beim Click: nimm den Wert der Daten
        // falls Liste Leer ist, gib eine Leere Liste zurück (nicht null, sonst Crash)
        // Convertiere die Liste (ggf. leere Liste) zu einer MutableList (um remove und add anwenden zu können)
        // .let um die Werte zwischen zu  speichern
        // val clickedFurniture -> geclicktes altes Element
        // finde das geclickte Element anhand seiner id
        // Auch wenn es nicht das selbe Element ist, sondern eine neue kopierte Referenz
        // ?: falls das Element nicht exisiert
        // finde den Index des geclickten Element und zwischenspeichere ihn
        // Kopiere das alte Element mit umgekehrten !isSelected Wert in die neue Element Instanz
        // Entferne das alte
        // Füge das neue Element (furnitureNowSelected) an die Stelle das alten (indexOfClicked)
        // setze die neue Liste ins LiveData

        furnitures.value.orEmpty().toMutableList().let { furnituresList ->

            val clickedFurniture: Furniture = furnituresList.find { it.id == clickedFurniture.id }
                ?: throw IllegalStateException("Unknown Furniture clicked")
            val indexOfClicked: Int = furnituresList.indexOf(clickedFurniture)
            val furnitureNowSelected: Furniture = clickedFurniture.copy(isSelected = !clickedFurniture.isSelected)
            furnituresList.removeAt(indexOfClicked)
            furnituresList.add(indexOfClicked, furnitureNowSelected)
            furnitures.value = furnituresList
        }
    }

    // randomUUID
    // universally unique identifier class (UUID) generates a random 128-bit value
    // 44e128a5-ac7a-4c9a-be4c-224b6bf81b20
    override fun createInitialFurnitures() = listOf(
        Furniture(randomUUID(), 0, FurnitureType.SOFA, FurnitureCategory.GRIND, FurnitureDifficulty.JOKE, null, true, true),
        Furniture(randomUUID(), 1, FurnitureType.SOFA, FurnitureCategory.GRIND, FurnitureDifficulty.JOKE, null, true, true),
        Furniture(randomUUID(), 2, FurnitureType.SOFA, FurnitureCategory.GRIND, FurnitureDifficulty.JOKE, null, true, true),
        Furniture(randomUUID(), 3, FurnitureType.SOFA, FurnitureCategory.GRIND, FurnitureDifficulty.EASY, null, false, true),
        Furniture(randomUUID(), 4, FurnitureType.SOFA, FurnitureCategory.GRIND, FurnitureDifficulty.EASY, null, false, true),
        Furniture(randomUUID(), 5, FurnitureType.SOFA, FurnitureCategory.GRIND, FurnitureDifficulty.EASY, null, false, true),
        Furniture(randomUUID(), 6, FurnitureType.SOFA, FurnitureCategory.GRIND, FurnitureDifficulty.EASY, null, false, true),
        Furniture(randomUUID(), 7, FurnitureType.SOFA, FurnitureCategory.GRIND, FurnitureDifficulty.EASY, null, false, true),
        Furniture(randomUUID(), 8, FurnitureType.SOFA, FurnitureCategory.GRIND, FurnitureDifficulty.EASY, null, false, true),

        Furniture(randomUUID(), 8, FurnitureType.ARMCHAIR, FurnitureCategory.SLIDE, FurnitureDifficulty.MIDDLE, null, true, true),
        Furniture(randomUUID(), 9, FurnitureType.ARMCHAIR, FurnitureCategory.SLIDE, FurnitureDifficulty.MIDDLE, null, true, true),
        Furniture(randomUUID(), 10, FurnitureType.ARMCHAIR, FurnitureCategory.SLIDE, FurnitureDifficulty.MIDDLE, null, true, true),
        Furniture(randomUUID(), 11, FurnitureType.ARMCHAIR, FurnitureCategory.SLIDE, FurnitureDifficulty.MIDDLE, null, false, true),
        Furniture(randomUUID(), 12, FurnitureType.ARMCHAIR, FurnitureCategory.SLIDE, FurnitureDifficulty.MIDDLE, null, false, true),
        Furniture(randomUUID(), 13, FurnitureType.ARMCHAIR, FurnitureCategory.SLIDE, FurnitureDifficulty.MIDDLE, null, false, true),

        Furniture(randomUUID(), 14, FurnitureType.BED, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, true, true),
        Furniture(randomUUID(), 15, FurnitureType.BED, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, true, true),
        Furniture(randomUUID(), 16, FurnitureType.BED, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, true, true),
        Furniture(randomUUID(), 17, FurnitureType.BED, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, false, true),
        Furniture(randomUUID(), 18, FurnitureType.BED, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, false, true),
        Furniture(randomUUID(), 19, FurnitureType.BED, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, false, true),
        Furniture(randomUUID(), 20, FurnitureType.BED, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, false, true),
        Furniture(randomUUID(), 21, FurnitureType.BED, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, false, true),
        Furniture(randomUUID(), 22, FurnitureType.BED, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, false, true)
    )

    private fun map(furnitureViewState: FurnitureViewState): Furniture {
        return Furniture(
            id = furnitureViewState.id,
            position = furnitureViewState.position,
            furnitureType = furnitureViewState.furnitureType,
            furnitureCategory = furnitureViewState.furnitureCategory,
            furnitureDifficulty = furnitureViewState.furnitureDifficulty,
            userCreateName = furnitureViewState.userCreatedName,
            isSelected = furnitureViewState.isSelected,
            isDefault = false
        )
    }

    private fun sortFurnitures(){
        furnitures.value = furnitures.value!!.sortedBy { it.furnitureCategory.sortWeight}
    }
}