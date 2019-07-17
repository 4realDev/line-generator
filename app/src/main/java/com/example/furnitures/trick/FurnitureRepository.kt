package com.example.furnitures.trick

import androidx.lifecycle.LiveData

interface FurnitureRepository {

    /* region data */
    fun getSortedFurnitures(): LiveData<List<Furniture>>

    fun getSelectedFurnituresList(): List<Furniture>

    fun getFurnitureById(id: String): Furniture?

    fun createFurniture(furnitureViewState: FurnitureViewState)

    fun deleteFurnitureById(id: String)

    fun updateFurnitureOnClick(clickedFurniture: FurnitureViewState)

    fun createInitialFurnitures(): List<Furniture>
    /* endregion */

    // warum getter im Repository/ Contract Interface, anstatt LiveData zu übergeben und diese mit Property zu überschreiben?
    // Der Rückgabe Wert ist weniger Flexibel. Will man einen anderen Typen zurückgeben als das LiveData hat, so kann man es im getter Interface anpassen
    // beim Property get() muss man die get() Methode überschreiben
}