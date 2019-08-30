package com.example.line_generator.bottombar.list

import android.app.Application
import com.example.line_generator.bottombar.settings.SettingsService
import com.example.line_generator.trick.Difficulty
import com.example.line_generator.trick.Difficulty.*
import com.example.line_generator.trick.DirectionIn
import com.example.line_generator.trick.DirectionIn.FAKIE
import com.example.line_generator.trick.DirectionIn.REGULAR
import com.example.line_generator.trick.DirectionOut
import com.example.line_generator.trick.DirectionOut.TO_FAKIE
import com.example.line_generator.trick.DirectionOut.TO_REGULAR
import com.example.line_generator.trick.Trick
import kotlin.math.roundToInt

class TrickSequenceGenerator(application: Application) {

    private val initialDirectionIn: DirectionIn = REGULAR
    private var searchedDirectionIn: DirectionIn = initialDirectionIn

    private val chosenMaxTricks = SettingsService.getMaxTricks(application)
    private val chosenDifficulty = SettingsService.getDifficulty(application)
    private val chosenTricksPercentage = mapToPercentage(chosenDifficulty)

    private val numberOfDifficulties: Int = values().size
    private val difficultyBasedNumberOfTricks = IntArray(numberOfDifficulties)

    fun mapToPercentage(choosenDifficulty: Difficulty): FloatArray {
        return when (choosenDifficulty) {
            SAVE -> floatArrayOf(1f, 0f, 0f, 0f, 0f)
            EASY -> floatArrayOf(0.5f, 0.5f, 0f, 0f, 0f)
            MIDDLE -> floatArrayOf(0.25f, 0.25f, 0.5f, 0f, 0f)
            HARD -> floatArrayOf(0.15f, 0.15f, 0.2f, 0.5f, 0f)
            CRAZY -> floatArrayOf(0.05f, 0.05f, 0.15f, 0.25f, 0.5f)
        }
    }

    fun randomizeWithSettings(list: List<Trick>): List<Trick> {
        caculateNumberOfTricks()
        fixUpRoundingMistakes()
        return getTrickListSortedByOutComingSortedByDifficulty(list)
    }

    private fun caculateNumberOfTricks() {
        for (index in 0 until numberOfDifficulties) {
            difficultyBasedNumberOfTricks[index] = (chosenMaxTricks * chosenTricksPercentage[index]).roundToInt()

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
            //                if (difficultyBasedNumberOfTricks[index] > filteredList.size)
            //                    newRandomizedList.addAll(filteredList.subList(0, filteredList.size))
            //                // existed elements > calculated, requested elements -> add the calculated number of elements
            //                else
            //                    newRandomizedList.addAll(filteredList.subList(0, difficultyBasedNumberOfTricks[index]))
            //            }
            // endregion
        }
    }

    private fun fixUpRoundingMistakes() {
        loop@ for (index in 0 until numberOfDifficulties) {
            if (difficultyBasedNumberOfTricks.sum() > chosenMaxTricks) {
                difficultyBasedNumberOfTricks[index] -= 1
            } else break@loop
        }
    }

    private fun getTrickListSortedByOutComingSortedByDifficulty(allTrick: List<Trick>): MutableList<Trick> {

        val sortedList = mutableListOf<Trick>()
        for (trick in allTrick.shuffled()) {
            val currentDifficulty = trick.difficulty.weight - 1
            val hasSearchedDirection = trick.directionIn == searchedDirectionIn
            val hasTrickWithSearchedDifficulty = difficultyBasedNumberOfTricks[currentDifficulty] > 0

            if (hasTrickWithSearchedDifficulty && hasSearchedDirection) {
                sortedList.add(trick)
                switchSearchedDirectionIn(trick.directionOut)
                difficultyBasedNumberOfTricks[currentDifficulty] -= 1
            }
        }
        return sortedList
    }

    private fun switchSearchedDirectionIn(directionOut: DirectionOut) {
        when (directionOut) {
            TO_REGULAR -> searchedDirectionIn = REGULAR
            TO_FAKIE -> searchedDirectionIn = FAKIE
        }
    }
}


// Functionen ordnen nach Aufruf Hirachie
// z.B. Public Methoden als Erste
// wichtig für leserlichkeit
// Function möglichst eine Aufgabe
// Wenn Function zuviele Aufgaben erfüllt, Class erstellen