package com.example.furnitures.calculator.bottombar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BottomBarViewModel(application: Application) : AndroidViewModel(application), BottomBarContract.ViewModel {

    private var viewStateData = MutableLiveData<ViewState>()
    private var bottomBarNavigationEvent = MutableLiveData<BottomBarItem>()

    override fun getBottomBarNavigationEvent(): LiveData<BottomBarItem> = bottomBarNavigationEvent
    override fun getViewState(): LiveData<ViewState> = viewStateData

    init {
        if (viewStateData.value == null)
            viewStateData.value = ViewState.INITIAL_STATE

        bottomBarNavigationEvent.value = BottomBarItem.TrickSelection
    }

    override fun changeViewState() {
        when (viewStateData.value) {
            ViewState.INITIAL_STATE -> viewStateData.value = ViewState.CHANGED_STATE
            ViewState.CHANGED_STATE -> viewStateData.value = ViewState.INITIAL_STATE
        }
    }

    override fun onBottomBarItemClicked(item: BottomBarItem) {
        bottomBarNavigationEvent.value = item
    }
}