package com.example.line_generator.start

import android.app.Application
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.line_generator.data.trick.*
import com.example.line_generator.extensions.randomUUID
import com.example.line_generator.userSelection.UserService
import com.example.line_generator.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartViewModel(application: Application) : AndroidViewModel(application), StartContract.ViewModel {

    companion object {
        const val SPLASH_SCREEM_TIMEOUT_LONG = 1500L
    }

    val trickDao = LineGeneratorDatabase.getDatabase(application).trickDao()
    val userId = UserService(application).getUserId()
    override var isStartAnimationDone = false
    private val navigationEvent = SingleLiveEvent<Unit>()


    init {
        // populateDatabase must be called only once
        // after the call isInitialized is set to true and saved in Preferences
        val isInitialized = StartService().getIsInitialized(application)
        if (!isInitialized) {
            StartService().saveIsInitialized(application, true)
            viewModelScope.launch(Dispatchers.IO) {
                populateDatabase(trickDao)
                delay(SPLASH_SCREEM_TIMEOUT_LONG)
                // postValue -> postToMainThread
                navigationEvent.postValue(Unit)
            }
        } else {
            val handler = Handler()
            handler.postDelayed({
                navigationEvent.call()
            }, SPLASH_SCREEM_TIMEOUT_LONG)
        }
    }

    override fun getNavigationEvent() = navigationEvent

    /**
     * Populate the database only on the first initialisation in a new coroutine.
     */
    // randomUUID
    // universally unique identifier class (UUID) generates a random 128-bit value
    // 44e128a5-ac7a-4c9a-be4c-224b6bf81b20
    fun populateDatabase(trickDao: TrickDao) {
        trickDao.insert(Trick(randomUUID(), userId, 0, TrickType.AXLE_STALL, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.EASY, null, selected = true, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 1, TrickType.FIVE_O_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.MIDDLE, null, selected = true, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 2, TrickType.FEEBLE_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.MIDDLE, null, selected = true, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 3, TrickType.SMITH_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.MIDDLE, null, selected = false, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 4, TrickType.CROOKED_GRIND, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.GRIND, Difficulty.HARD, null, selected = false, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 5, TrickType.PIVOT, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.SAVE, null, selected = false, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 6, TrickType.NOSE_GRIND, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.GRIND, Difficulty.HARD, null, selected = false, default = true))

        trickDao.insert(Trick(randomUUID(), userId, 8, TrickType.TAIL_STALL, DirectionIn.FAKIE, DirectionOut.TO_REGULAR, Category.SLIDE, Difficulty.SAVE, null, selected = true, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 9, TrickType.NOSE_STALL, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.SLIDE, Difficulty.MIDDLE, null, selected = true, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 10, TrickType.ROCK_TO_FAKIE, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.SLIDE, Difficulty.SAVE, null, selected = true, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 11, TrickType.ROCK_N_ROLL, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.SLIDE, Difficulty.EASY, null, selected = false, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 12, TrickType.HALFCAB_ROCK, DirectionIn.FAKIE, DirectionOut.TO_FAKIE, Category.SLIDE, Difficulty.EASY, null, selected = false, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 13, TrickType.FULLCAB_ROCK, DirectionIn.FAKIE, DirectionOut.TO_REGULAR, Category.SLIDE, Difficulty.MIDDLE, null, selected = false, default = true))

        trickDao.insert(Trick(randomUUID(), userId, 14, TrickType.BONELESS, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.OTHER, Difficulty.MIDDLE, null, selected = true, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 15, TrickType.NO_COMPLY, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.OTHER, Difficulty.HARD, null, selected = true, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 16, TrickType.DISTASTER, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.OTHER, Difficulty.CRAZY, null, selected = true, default = true))
        trickDao.insert(Trick(randomUUID(), userId, 17, TrickType.OLLIE, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.OTHER, Difficulty.CRAZY, null, selected = false, default = true))
    }
}