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
    private var dependingDirectionIn: DirectionIn = initialDirectionIn

    private val choosenMaxTricks = SettingsService.getMaxTricks(application)
    private val choosenDifficulty = SettingsService.getDifficulty(application)

    private val arrayItems = choosenDifficulty.weight

    private val difficultyArray: Array<FurnitureDifficulty> = enumValues()

    private val difficultyPercentageArray = FloatArray(arrayItems)
    private val numberOfTricksArray = IntArray(arrayItems)
    // Berechnung des Nenners im Bruch -> x/fraction
    private val fraction = (2.0f.pow(arrayItems - 1) - 1) * 2



    // Functionen ordnen nach Aufruf Hirachie
    // z.B. Public Methoden als Erste
    // wichtig für leserlichkeit
    // Function möglichst eine Aufgabe
    // Wenn Function zuviele Aufgaben erfüllt, Class erstellen

    fun randomizeWithSettings(list: List<Trick>): List<Trick> {

        for (index in 0 until arrayItems) {

            difficultyPercentageArray[index] = calculateDifficultyPercentage(index)


            // calculate the number of tricks for each difficulty (maxTricks * percentage)
            numberOfTricksArray[index] = (choosenMaxTricks * difficultyPercentageArray[index]).roundToInt()


            // get filteredList for each difficulty
            // shuffle filteredList, to select random set
            // if its null (first time), then set it to the default value "TO_FAKIE"
            // lastOrNull in combination with ?:, prevent NullPointException
//            val nextDirectionIn = newRandomizedList.lastOrNull()?.directionOut?.invert() ?: DirectionOut.TO_FAKIE
//            var previousDirectionOut = newRandomizedList.lastOrNull()?.directionOut/*?.invert()*/ ?: DirectionOut.TO_FAKIE
//            var filteredList = getTrickListSortedByOutComingSortedByDifficulty(list, difficultyArray[index], nextComing)
            val filteredList = getTrickListSortedByOutComingSortedByDifficulty(
                list,
                index,
                difficultyArray[index]
            )


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

            if (filteredList.isNotEmpty()) {
                newRandomizedList.addAll(filteredList)
            }
        }

        return newRandomizedList
    }

    private fun calculateDifficultyPercentage(index: Int): Float {
        val difficultyEqualsSave: Boolean = choosenDifficulty == FurnitureDifficulty.SAVE
        val difficultyIsLast: Boolean = index == difficultyPercentageArray.lastIndex

        // if choosenDifficulty is the easiest -> return all items (100%) of this difficulty
        // Assignment of when, with else case: makes sure that there is a value set
        return when {
            difficultyEqualsSave -> HUNDRET_PERCENT
            // for the last element -> sum all of the percentageArray
            difficultyIsLast -> difficultyPercentageArray.sum()
            // calculate the percentage for each difficulty before the last one -> ((2^index)/fraction)
            else -> ((2.0f.pow(index) / fraction))
        }
    }

    private fun getTrickListSortedByOutComingSortedByDifficulty(
        allTrick: List<Trick>,
        currentIndex: Int,
        difficulty: FurnitureDifficulty
    ): List<Trick> {

        val filteredTricks = mutableListOf<Trick>()
        for (trick in allTrick.shuffled()) {
            if (trick.furnitureDifficulty == difficulty && trick.directionIn == dependingDirectionIn) {
                filteredTricks.add(trick)

                dependingDirectionIn = when(trick.directionOut){
                    DirectionOut.TO_REGULAR -> DirectionIn.REGULAR
                    DirectionOut.TO_FAKIE -> DirectionIn.FAKIE
                }

                // !!! FEHLER !!!
                // Crash, falls -1
                numberOfTricksArray[currentIndex] = numberOfTricksArray[currentIndex] - 1
            }
        }
        return filteredTricks
    }
}