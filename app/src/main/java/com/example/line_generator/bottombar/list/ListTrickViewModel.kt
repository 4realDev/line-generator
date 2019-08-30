package com.example.line_generator.bottombar.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.line_generator.trick.*

// numbPickerIndex = 0 -> position = 1
// ListTrickViewModel greift nur auf Repository zu, schreibt aber nie was rein

class ListTrickViewModel(application: Application) : AndroidViewModel(application), ListTrickContract.ViewModel {

    // Scoping auf activity nur sinnvoll, wenn Liste nach Fragment wechsel noch erhalten bleiben soll
    // Oder wenn Liste nicht verloren gehen darf, wenn Fragment destroyed wird
    // In unserem Fall soll jedes Mal neue Liste generiert werden (somit alte verworfen werden)
    // -> Scoping liegt auf Fragment (nicht auf Activity ("this" statt "activity"))
    // -> viewModel = ViewModelProviders.of(this).get(ListTrickViewModel::class.java)
    private val repository: TricksRepositoryImp
    private val trickSequenceGenerator = TrickSequenceGenerator(application)
    private val selectedTricks: LiveData<List<Trick>>
    private val shuffledSelectedTricks: LiveData<List<Trick>>
    private val selectedTricksViewState: LiveData<List<TrickViewState>>
    private val selectedTricksAccessData = MediatorLiveData<List<TrickViewState>>()

    init {
        val trickDao = TrickRoomDatabase.getDatabase(application, viewModelScope).trickDao()
        repository = TricksRepositoryImp(trickDao)
        selectedTricks = repository.getSelectedTricks()
        shuffledSelectedTricks = Transformations.map(selectedTricks, ::mapSelectedToShuffledList)
        selectedTricksViewState = Transformations.map(shuffledSelectedTricks, ::mapShuffledToViewState)

        selectedTricksAccessData.addSource(selectedTricksViewState) { value ->
            selectedTricksAccessData.value = selectedTricksViewState.value
        }
    }

    private fun mapItemToViewState(index: Int, trick: Trick): TrickViewState {
        return TrickViewState(
            id = trick.id,
            position = index + 1,
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

    private fun mapSelectedToShuffledList(list: List<Trick>) = trickSequenceGenerator.randomizeWithSettings(list)
    private fun mapShuffledToViewState(list: List<Trick>): List<TrickViewState> {
        return list
            .map { mapItemToViewState(list.indexOf(it), it) }
    }
    override fun getSelectedItems(): LiveData<List<TrickViewState>> = selectedTricksAccessData
    override fun areTricksSelected()= repository.getSelectedTricks().value!!.isNotEmpty()

    override fun changeTrickItemPosition(fromPosition: Int, toPosition: Int) {

        // Wenn fromPosition auf dem View über toPosition liegt (der Index kleiner ist)
        // Dann soll fromPosition - fromPosition bleiben (StartPosition)

        // Variablen dürfen nicht den selben namen haben, sonst switched es zurück

        val startPosition = if (fromPosition < toPosition) fromPosition else toPosition
        val endPosition = if (fromPosition < toPosition) toPosition else fromPosition


        var newSwapUnitList = selectedTricksAccessData.value.orEmpty().toMutableList()
        val removed = newSwapUnitList.removeAt(fromPosition)
        newSwapUnitList.add(toPosition, removed)

        // Überschreibe die Liste mit der neuen gemappten Liste
        // Am Anfang ist gemappte Liste leer
        // Befindet sich das Item in der gesuchten Position, kopiere dieses mit neuer Position
        // Falls nicht, übertrage das Item eins zu eins in die gemappte Liste
        // Die Position, darf aber nicht direkt verändert werden (VAR position -> trick.position = numbPickerIndex + 1)
        // Das würde zu einer Veränderung in allen Instancen führen (auch im Adapter)
        // Daten Properties sollen immer VAL (final sein) & so nur durch mapping "veränderbar"
        newSwapUnitList = newSwapUnitList.mapIndexed { index, trickViewState ->
            if (index in startPosition..endPosition) {
                trickViewState.copy(position = index + 1)
            } else trickViewState
        }.toMutableList()

        //selectedTricksViewState.value = newSwapUnitList
        selectedTricksAccessData.value = newSwapUnitList

        Log.d("DEBUGG", "fromViewModelPosition: $fromPosition toViewModelPosition: $toPosition")
    }

    override fun removeTrickItem(trickViewState: TrickViewState) {
        var newRemoveList = selectedTricksAccessData.value?.toMutableList()
        val removeIndex = trickViewState.position - 1
        newRemoveList?.removeAt(removeIndex)
        newRemoveList = newRemoveList?.mapIndexed { index, trickViewState ->
            if (index + 1 <= trickViewState.position) {
                trickViewState.copy(position = index + 1)
            } else trickViewState
        }?.toMutableList()
        //selectedTricksViewState.value = newRemoveUnit
        selectedTricksAccessData.value = newRemoveList

//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteTrickById(trickViewState.id)
//        }
    }
}