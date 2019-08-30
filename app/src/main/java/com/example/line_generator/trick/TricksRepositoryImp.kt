package com.example.line_generator.trick

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

class TricksRepositoryImp(private val trickDao: TrickDao) : TrickRepository {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    private val allTricks: LiveData<List<Trick>> = trickDao.getAll()
    private val sortedTricks = Transformations.map(allTricks, ::sortTricks)
    private val selectedTricks = Transformations.map(allTricks, ::filterSelectedTricks)

    // Transformations.map wird erst Aufgerufen, wenn die LiveData Observed wird.
    // Mapping Funktion findet nur statt, wenn selectedTricks bereits observed wird!!!
    //private val selectedTricks = Transformations.map(tricks, ::filterSelect)

    private fun sortTricks(tricks: List<Trick>) = tricks.sortedBy { it.category }
    private fun filterSelectedTricks(tricks: List<Trick>) = tricks.filter { it.selected }

    // Collections sollten nie null sein
    override fun getSortedTricks(): LiveData<List<Trick>> = sortedTricks

    override fun getSelectedTricks(): LiveData<List<Trick>> = selectedTricks

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    // Ausschließende Funktion, die andere im Main Thread blocken würde
    // -> Suspend function 'insert' should be called only from a coroutine or another suspend function
    @Suppress
    @WorkerThread
    override suspend fun insert(trickViewState: TrickViewState) {
        val trick = map(trickViewState)
        trickDao.insert(trick)
    }

    @Suppress
    @WorkerThread
    override suspend fun updateTrickOnClick(clickedTrick: TrickViewState) {
//         Beim Click: nimm den Wert der Daten
//         falls Liste Leer ist, gib eine Leere Liste zurück (nicht null, sonst Crash)
//         Convertiere die Liste (ggf. leere Liste) zu einer MutableList (um remove und add anwenden zu können)
//         .let um die Werte zwischen zu  speichern
//         val clickedTrick -> geclicktes altes Element
//         finde das geclickte Element anhand seiner id
//         Auch wenn es nicht das selbe Element ist, sondern eine neue kopierte Referenz
//         ?: falls das Element nicht exisiert
//         finde den Index des geclickten Element und zwischenspeichere ihn
//         Kopiere das alte Element mit umgekehrten !selected Wert in die neue Element Instanz
//         Entferne das alte
//         Füge das neue Element (trickNowSelected) an die Stelle das alten (indexOfClicked)
//         setze die neue Liste ins LiveData
//
//        allTricks.value.orEmpty().toMutableList().let { trickList ->
//
//            val clickedTrick: Trick = trickList.find { it.id == clickedTrick.id }
//                ?: throw IllegalStateException("Unknown Trick clicked")
//            val indexOfClicked: Int = trickList.indexOf(clickedTrick)
//            val trickNowSelected: Trick = clickedTrick.copy(selected = !clickedTrick.selected)
//            trickList.removeAt(indexOfClicked)
//            trickList.add(indexOfClicked, trickNowSelected)
//            val mutableLiveData = MutableLiveData<List<Trick>>()
//            mutableLiveData.value = trickList
//            allTricks = mutableLiveData
//            //tricks.value = trickList
//        }
        trickDao.update(!clickedTrick.selected, clickedTrick.id)
    }

    @WorkerThread
    @Suppress
    override suspend fun updateAfterEdit(id: String, changedName: String, changedDirectionIn: DirectionIn, changedDirectionOut: DirectionOut, changedDifficulty: Difficulty) {
        trickDao.updateAfterEdit(id, changedName, changedDirectionIn, changedDirectionOut, changedDifficulty)
    }

    @WorkerThread
    @Suppress
    override suspend fun deleteTrickById(id: String) {
        trickDao.delete(id)
    }

    /* region old createInitialTricks Method */
    // randomUUID
    // universally unique identifier class (UUID) generates a random 128-bit value
    // 44e128a5-ac7a-4c9a-be4c-224b6bf81b20
//    override fun createInitialTricks() {
//        trickDao.insert(Trick(randomUUID(), 0, TrickType.AXLE_STALL, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.EASY, null, true, true))
//        trickDao.insert(Trick(randomUUID(), 1, TrickType.FIVE_O_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.MIDDLE, null, true, true))
//        trickDao.insert(Trick(randomUUID(), 2, TrickType.FEEBLE_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.MIDDLE, null, true, true))
//        trickDao.insert(Trick(randomUUID(), 3, TrickType.SMITH_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.MIDDLE, null, false, true))
//        trickDao.insert(Trick(randomUUID(), 4, TrickType.CROOKED_GRIND, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.GRIND, Difficulty.HARD, null, false, true))
//        trickDao.insert(Trick(randomUUID(), 5, TrickType.PIVOT, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.SAVE, null, false, true))
//        trickDao.insert(Trick(randomUUID(), 6, TrickType.NOSE_GRIND, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.GRIND, Difficulty.HARD, null, false, true))
//
//        trickDao.insert(Trick(randomUUID(), 8, TrickType.TAIL_STALL, DirectionIn.FAKIE, DirectionOut.TO_REGULAR, Category.SLIDE, Difficulty.SAVE, null, true, true))
//        trickDao.insert(Trick(randomUUID(), 9, TrickType.NOSE_STALL, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.SLIDE, Difficulty.MIDDLE, null, true, true))
//        trickDao.insert(Trick(randomUUID(), 10, TrickType.ROCK_TO_FAKIE, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.SLIDE, Difficulty.SAVE, null, true, true))
//        trickDao.insert(Trick(randomUUID(), 11, TrickType.ROCK_N_ROLL, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.SLIDE, Difficulty.EASY, null, false, true))
//        trickDao.insert(Trick(randomUUID(), 12, TrickType.HALFCAB_ROCK, DirectionIn.FAKIE, DirectionOut.TO_FAKIE, Category.SLIDE, Difficulty.EASY, null, false, true))
//        trickDao.insert(Trick(randomUUID(), 13, TrickType.FULLCAB_ROCK, DirectionIn.FAKIE, DirectionOut.TO_REGULAR, Category.SLIDE, Difficulty.MIDDLE, null, false, true))
//
//        trickDao.insert(Trick(randomUUID(), 14, TrickType.BONELESS, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.OTHER, Difficulty.MIDDLE, null, true, true))
//        trickDao.insert(Trick(randomUUID(), 15, TrickType.NO_COMPLY, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.OTHER, Difficulty.HARD, null, true, true))
//        trickDao.insert(Trick(randomUUID(), 16, TrickType.DISTASTER, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.OTHER, Difficulty.CRAZY, null, true, true))
//        trickDao.insert(Trick(randomUUID(), 17, TrickType.OLLIE, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.OTHER, Difficulty.CRAZY, null, false, true))
//    }
/* endregion */

    private fun map(trickViewState: TrickViewState): Trick {
        return Trick(
            id = trickViewState.id,
            position = trickViewState.position,
            trickType = trickViewState.trickType,
            directionIn = trickViewState.directionIn,
            directionOut = trickViewState.directionOut,
            category = trickViewState.category,
            difficulty = trickViewState.difficulty,
            userCreateName = trickViewState.userCreatedName,
            selected = trickViewState.selected,
            default = false
        )
    }
}