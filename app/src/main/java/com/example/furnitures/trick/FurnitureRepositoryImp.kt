package com.example.furnitures.trick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.furnitures.extensions.randomUUID

class FurnitureRepositoryImp : FurnitureRepository {

    // Transformations.map wird erst Aufgerufen, wenn die LiveData Observed wird.

    private val furnitures = MutableLiveData<List<Trick>>()
    // Mapping Funktion findet nur statt, wenn selectedFurnitures bereits observed wird!!!
    //private val selectedFurnitures = Transformations.map(furnitures, ::filterSelect)

    init {
        furnitures.value = createInitialFurnitures()
    }

    // sortierung vorher behebt fehler beim header einfügen
    override fun getSortedFurnitures(): LiveData<List<Trick>>{
        sortFurnitures()
        return furnitures
    }

    //override fun getSelectedFurnitures(): LiveData<List<Trick>> = selectedFurnitures

    override fun getSelectedFurnituresList(): List<Trick> {
        // Collections sollten nie null sein
        return furnitures.value.orEmpty().filter { it.isSelected }
    }

    override fun getFurnitureById(id: String): Trick? {
        lateinit var outputTrick: Trick
        furnitures.value?.forEach { furniture ->
            if (id == furniture.id) {
                outputTrick = furniture
            }
        }
        return outputTrick
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

            val clickedTrick: Trick = furnituresList.find { it.id == clickedFurniture.id }
                ?: throw IllegalStateException("Unknown Trick clicked")
            val indexOfClicked: Int = furnituresList.indexOf(clickedTrick)
            val trickNowSelected: Trick = clickedTrick.copy(isSelected = !clickedTrick.isSelected)
            furnituresList.removeAt(indexOfClicked)
            furnituresList.add(indexOfClicked, trickNowSelected)
            furnitures.value = furnituresList
        }
    }

    // randomUUID
    // universally unique identifier class (UUID) generates a random 128-bit value
    // 44e128a5-ac7a-4c9a-be4c-224b6bf81b20
    override fun createInitialFurnitures() = listOf(
        Trick(randomUUID(), 0, FurnitureType.AXLE_STALL, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, FurnitureCategory.GRIND, FurnitureDifficulty.EASY, null, true, true),
        Trick(randomUUID(), 1, FurnitureType.FIVE_O_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, FurnitureCategory.GRIND, FurnitureDifficulty.MIDDLE, null, true, true),
        Trick(randomUUID(), 2, FurnitureType.FEEBLE_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, FurnitureCategory.GRIND, FurnitureDifficulty.MIDDLE, null, true, true),
        Trick(randomUUID(), 3, FurnitureType.SMITH_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, FurnitureCategory.GRIND, FurnitureDifficulty.MIDDLE, null, false, true),
        Trick(randomUUID(), 4, FurnitureType.CROOKED_GRIND, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, FurnitureCategory.GRIND, FurnitureDifficulty.HARD, null, false, true),
        Trick(randomUUID(), 5, FurnitureType.PIVOT, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, FurnitureCategory.GRIND, FurnitureDifficulty.SAVE, null, false, true),
        Trick(randomUUID(), 6, FurnitureType.NOSE_GRIND, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, FurnitureCategory.GRIND, FurnitureDifficulty.HARD, null, false, true),

        Trick(randomUUID(), 8, FurnitureType.TAIL_STALL, DirectionIn.FAKIE, DirectionOut.TO_REGULAR, FurnitureCategory.SLIDE, FurnitureDifficulty.SAVE, null, true, true),
        Trick(randomUUID(), 9, FurnitureType.NOSE_STALL, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, FurnitureCategory.SLIDE, FurnitureDifficulty.MIDDLE, null, true, true),
        Trick(randomUUID(), 10, FurnitureType.ROCK_TO_FAKIE, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, FurnitureCategory.SLIDE, FurnitureDifficulty.SAVE, null, true, true),
        Trick(randomUUID(), 11, FurnitureType.ROCK_N_ROLL, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, FurnitureCategory.SLIDE, FurnitureDifficulty.EASY, null, false, true),
        Trick(randomUUID(), 12, FurnitureType.HALFCAB_ROCK, DirectionIn.FAKIE, DirectionOut.TO_FAKIE, FurnitureCategory.SLIDE, FurnitureDifficulty.EASY, null, false, true),
        Trick(randomUUID(), 13, FurnitureType.FULLCAB_ROCK, DirectionIn.FAKIE, DirectionOut.TO_REGULAR, FurnitureCategory.SLIDE, FurnitureDifficulty.MIDDLE, null, false, true),

        Trick(randomUUID(), 14, FurnitureType.BONELESS, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, FurnitureCategory.OTHER, FurnitureDifficulty.MIDDLE, null, true, true),
        Trick(randomUUID(), 15, FurnitureType.NO_COMPLY, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, FurnitureCategory.OTHER, FurnitureDifficulty.HARD, null, true, true),
        Trick(randomUUID(), 16, FurnitureType.DISTASTER, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, true, true),
        Trick(randomUUID(), 17, FurnitureType.OLLIE, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, FurnitureCategory.OTHER, FurnitureDifficulty.CRAZY, null, false, true)
    )

    private fun map(furnitureViewState: FurnitureViewState): Trick {
        return Trick(
            id = furnitureViewState.id,
            position = furnitureViewState.position,
            furnitureType = furnitureViewState.furnitureType,
            directionIn = furnitureViewState.directionIn,
            directionOut = furnitureViewState.directionOut,
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