package com.example.furnitures.calculator.bottombar

import androidx.lifecycle.LiveData

// Interface benötigt .kt/.java Annotation
// Sonst kann Compiler Interface nicht lesen

interface BottomBarContract {

    interface ViewModel {

        //region Actions
        fun onBottomBarItemClicked(item: BottomBarItem)
        //endregion

        //region NavigationEvents
        fun getViewState(): LiveData<ViewState>

        fun getBottomBarNavigationEvent(): LiveData<BottomBarItem>
        //endregion NavigationEvents

        fun changeViewState()
    }

    interface Navigator {
        fun openBottomBarItem(item: BottomBarItem)
    }
}

enum class ViewState {
    INITIAL_STATE, CHANGED_STATE
}

sealed class BottomBarItem {
    object ListTrick : BottomBarItem()
    object TrickSelection : BottomBarItem()
}