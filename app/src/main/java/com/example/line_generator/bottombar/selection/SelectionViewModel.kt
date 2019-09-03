package com.example.line_generator.bottombar.selection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.line_generator.data.trick.*
import com.example.line_generator.userSelection.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 *
 */

/**
 * Using LiveData and caching what tricks returns has several benefits:
 * - We can put an observer on the data (instead of polling for changes) and only update the
 *   the UI when the data actually changes.
 * - Repository is completely separated from the UI through the ViewModel.
 */

private const val START_HEADER_POSITION: Int = 0

// ID sollten immer eindeutig sein in ihrem Context
// Sehr unwahrscheinlich das in diesem Context in dieser Liste FIRST_HEADER_ID nochmal verwendet wird
// Selbst gewählte ID besser zum Debuggen und zum Verständnis des Entwicklers - d.h. kein randomUUID()
private const val FIRST_HEADER_ID: String = "FIRST_HEADER_ID"
private const val SLIDE_HEADER_ID: String = "SLIDE_HEADER_ID"
private const val OTHER_HEADER_ID: String = "OTHER_HEADER_ID"

class SelectionViewModel(application: Application) : AndroidViewModel(application), TrickContract.ViewModel {

    private val repository: TricksRepositoryImp

    private val sortedTricks: LiveData<List<Trick>>
    private val trickViewState: LiveData<List<RowViewState>>
    private val itemsWithHeader: LiveData<List<RowViewState>?>

    private var clickedItem: TrickViewState? = null
    private val userId = UserService(application).getUserId()

    init {
        val trickDao = LineGeneratorDatabase.getDatabase(application).trickDao()
        repository = TricksRepositoryImp(trickDao, userId)
        sortedTricks = repository.getSortedTricks()
        trickViewState = Transformations.map(sortedTricks, ::mapListToViewState)
        itemsWithHeader = Transformations.map(trickViewState, ::addHeaders)
    }

    private fun mapListToViewState(trickList: List<Trick>): List<RowViewState> {
        return trickList
            .map { mapItemToViewState(it) }
    }

    private fun mapItemToViewState(trick: Trick): TrickViewState {
        return TrickViewState(
            id = trick.id,
            userId = trick.userId,
            position = trick.position,
            trickType = trick.trickType,
            directionIn = trick.directionIn,
            directionOut = trick.directionOut,
            category = trick.category,
            difficulty = trick.difficulty,
            name = TrickTypeHelper.getString(trick.trickType),
            userCreatedName = trick.userCreateName,
            drawableResId = TrickTypeHelper.getDrawable(trick.trickType),
            selected = trick.selected
        )
    }

    private fun addHeaders(tricks: List<RowViewState>): List<RowViewState> {
        return if (tricks.isEmpty()) emptyList()
        else {
            checkListItemCategoriesAndAddHeaders(tricks)
        }
    }

    private fun checkListItemCategoriesAndAddHeaders(list: List<RowViewState>): List<RowViewState> {
        val listWithHeaders = list.toMutableList()
        var headerViewStateGrind: RowViewState? = null
        var headerViewStateSlide: RowViewState? = null
        var headerViewStateOther: RowViewState? = null
        var additionalHeaderItems = 0

        list.forEachIndexed { index, rowViewState ->
            // Gibt es Tricks mit dieser Kategory?
            // Wenn ja, existiert bereits ein Header von diese Kategory?
            // Wenn nein: erstelle Header für die Kategory
            if (rowViewState.category == Category.GRIND && headerViewStateGrind == null) {
                headerViewStateGrind = HeaderViewState(FIRST_HEADER_ID, index, Category.GRIND)
                headerViewStateGrind?.let { listWithHeaders.add(index, it) }
                additionalHeaderItems += 1
            }
            if (rowViewState.category == Category.SLIDE && headerViewStateSlide == null) {
                headerViewStateSlide = HeaderViewState(SLIDE_HEADER_ID, index + 1, Category.SLIDE)
                headerViewStateSlide?.let { listWithHeaders.add(index + additionalHeaderItems, it) }
                additionalHeaderItems += 1
            }
            if (rowViewState.category == Category.OTHER && headerViewStateOther == null) {
                headerViewStateOther = HeaderViewState(OTHER_HEADER_ID, index + 2, Category.OTHER)
                headerViewStateOther?.let { listWithHeaders.add(index + additionalHeaderItems, it) }
                additionalHeaderItems += 1
            }
        }
        return listWithHeaders
    }

    override fun getTrickViewStateWithHeader(): LiveData<List<RowViewState>?> = itemsWithHeader

    override fun onTrickClicked(trickViewState: TrickViewState) {
        clickedItem = trickViewState
        update(trickViewState)
    }

    override fun update(trickViewState: TrickViewState): Job {
        return viewModelScope.launch(Dispatchers.IO) { repository.updateTrickOnClick(trickViewState) }
    }

    override fun updateAfterEdit(id: String, changedName: String, changedDirectionIn: DirectionIn, changedDirectionOut: DirectionOut, changedDifficulty: Difficulty): Job {
        return viewModelScope.launch(Dispatchers.IO) { repository.updateAfterEdit(id, changedName, changedDirectionIn, changedDirectionOut, changedDifficulty) }
    }

    override fun delete(trickViewState: TrickViewState) {
//        val tricksListMinusPredeleted = itemsWithHeader.value
//        val predeletedPosition = tricksListMinusPredeleted.indexOf(trickViewStateate)
//        tricksListMinusPredeleted?.minusElement(trickViewStateate)
//        itemsWithHeader.value = tricksListMinusPredeleted
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTrickById(trickViewState.id)
        }
    }

    // Getter for a "Job" to insert the data in coroutine in a non-blocking way
    override fun insert(trickViewState: TrickViewState): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            repository.insert(trickViewState)
        }
    }

    override fun getClickedItem(): TrickViewState? = clickedItem

//    override fun isSectionEmpty(): Boolean = trickViewState.value.isNullOrEmpty()


    // Job stoppen, wenn ViewModel destroyed wird -> vermeidet memory leak vom ViewModel
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}