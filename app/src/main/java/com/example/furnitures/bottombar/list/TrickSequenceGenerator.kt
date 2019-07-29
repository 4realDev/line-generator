package com.example.furnitures.bottombar.list

import android.app.Application
import com.example.furnitures.bottombar.settings.SettingsService
import com.example.furnitures.trick.DirectionIn
import com.example.furnitures.trick.DirectionOut
import com.example.furnitures.trick.FurnitureDifficulty
import com.example.furnitures.trick.Trick
import kotlin.math.pow
import kotlin.math.roundToInt

const val HUNDRET_PERCENT = 1.0f

class TrickSequenceGenerator(application: Application) {

    private var newRandomizedList = emptyList<Trick>().toMutableList()

    private val initialDirectionIn: DirectionIn = DirectionIn.REGULAR
    private var searchedDirectionIn: DirectionIn = initialDirectionIn

    private val chosenMaxTricks = SettingsService.getMaxTricks(application)
    private val chosenDifficulty = SettingsService.getDifficulty(application)

    private val maxArrayIndex: Int = FurnitureDifficulty.values().size
    private val numberOfChosenDifficulty: Int = chosenDifficulty.weight
    private val indexOfChosenDifficulty: Int = numberOfChosenDifficulty - 1

    private val difficultyPercentageArray = FloatArray(maxArrayIndex)
    private val numberOfTricksArray = IntArray(maxArrayIndex)
    // Berechnung des Nenners im Bruch -> x/fraction
    private val fraction = (2.0f.pow(indexOfChosenDifficulty) - 1) * 2


    // Functionen ordnen nach Aufruf Hirachie
    // z.B. Public Methoden als Erste
    // wichtig für leserlichkeit
    // Function möglichst eine Aufgabe
    // Wenn Function zuviele Aufgaben erfüllt, Class erstellen

    fun randomizeWithSettings(list: List<Trick>): List<Trick> {
        for (index in 0 until numberOfChosenDifficulty) {
            difficultyPercentageArray[index] = calculateDifficultyPercentage(index)
            // calculate the number of tricks for each difficulty (maxTricks * percentage)
            numberOfTricksArray[index] = (chosenMaxTricks * difficultyPercentageArray[index]).roundToInt()
            // region old code (Übertragen in Notizen)
            // get filteredList for each difficulty
            // shuffle filteredList, to select random set
            // if its null (first time), then set it to the default value "TO_FAKIE"
            // lastOrNull in combination with ?:, prevent NullPointException
//            val nextDirectionIn = newRandomizedList.lastOrNull()?.directionOut?.invert() ?: DirectionOut.TO_FAKIE
//            var previousDirectionOut = newRandomizedList.lastOrNull()?.directionOut/*?.invert()*/ ?: DirectionOut.TO_FAKIE
//            var filteredList = getTrickListSortedByOutComingSortedByDifficulty(list, difficultyArray[index], nextComing)


            // if difficulty exists
//            if (filteredList.isNotEmpty()) {
//                // existed elements < calculated, requested elements -> add all existed elements
//                // requested 5 - existed 3 -> add all 3
//                if (numberOfTricksArray[index] > filteredList.size)
//                    newRandomizedList.addAll(filteredList.subList(0, filteredList.size))
//                // existed elements > calculated, requested elements -> add the calculated number of elements
//                else
//                    newRandomizedList.addAll(filteredList.subList(0, numberOfTricksArray[index]))
//            }
            // endregion
        }

        fixRoundingMisstakes()


        val filteredList = getTrickListSortedByOutComingSortedByDifficulty(list)
        if (filteredList.isNotEmpty()) {
            newRandomizedList.addAll(filteredList)
        }

        return newRandomizedList
    }

    private fun fixRoundingMisstakes() {
        loop@ for (index in indexOfChosenDifficulty downTo 0) {
            if (numberOfTricksArray.sum() > chosenMaxTricks && numberOfTricksArray[index] != 0) {
                numberOfTricksArray[index] -= 1
            }
            else if(numberOfTricksArray.sum() < chosenMaxTricks){
                numberOfTricksArray[index] += 1
            } else break@loop
        }
    }

    private fun calculateDifficultyPercentage(index: Int): Float {
        val difficultyEqualsSave: Boolean = chosenDifficulty == FurnitureDifficulty.SAVE
        val difficultyIsLast: Boolean = index == indexOfChosenDifficulty

        // if chosenDifficulty is the easiest -> return all items (100%) of this difficulty
        // Assignment of when, with else case: makes sure that there is a value set
        return when {
            difficultyEqualsSave -> HUNDRET_PERCENT
            // for the last element -> sum all of the percentageArray
            difficultyIsLast -> difficultyPercentageArray.sum()
            // calculate the percentage for each difficulty before the last one -> ((2^index)/fraction)
            else -> ((2.0f.pow(index) / fraction))
        }
    }

    private fun getTrickListSortedByOutComingSortedByDifficulty(allTrick: List<Trick>): List<Trick> {

        val filteredTricks = mutableListOf<Trick>()
        for (trick in allTrick.shuffled()) {
            val difficultyIndex = trick.furnitureDifficulty.weight - 1
            if (numberOfTricksArray[difficultyIndex] > 0 && trick.directionIn == searchedDirectionIn) {
                filteredTricks.add(trick)

                searchedDirectionIn = when (trick.directionOut) {
                    DirectionOut.TO_REGULAR -> DirectionIn.REGULAR
                    DirectionOut.TO_FAKIE -> DirectionIn.FAKIE
                }

                numberOfTricksArray[difficultyIndex] = numberOfTricksArray[difficultyIndex] - 1
            }
        }
        return filteredTricks
    }
}