package com.example.furnitures.calculator.furniture

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import java.util.*

class FurnitureRepositoryImp : FurnitureRepository {

    private val furnitures = MutableLiveData<List<Furniture>>()
    private val selectedFurnitures = MutableLiveData<List<Furniture>>()

    override fun getFurnitures(): LiveData<List<Furniture>> = furnitures

    override fun getSelectedFurnitures(): LiveData<List<Furniture>> = selectedFurnitures

    override fun getFurnitureSync(id: String): Furniture? {
        lateinit var outputFurniture: Furniture
        furnitures.value?.forEach { furniture ->
            if(id == furniture.id) {
                outputFurniture = furniture
            }
        }
        return outputFurniture
    }

    override fun createInitialFurniture() = listOf(
        Furniture(randomUUID(), FurnitureType.BED, 0, 22.0, false, true, false),
        Furniture(randomUUID(), FurnitureType.ARMCHAIR, 0, 22.0, false, true, false),
        Furniture(randomUUID(), FurnitureType.SOFA, 0, 22.0, false, true, false)
    )

    // universally unique identifier class (UUID) generates a random 128-bit value
    // 44e128a5-ac7a-4c9a-be4c-224b6bf81b20
    private fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }
}