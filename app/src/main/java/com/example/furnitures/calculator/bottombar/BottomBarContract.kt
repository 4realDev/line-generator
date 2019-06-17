package com.example.furnitures.calculator.bottombar

import androidx.lifecycle.LiveData

// Interface benötigt .kt/.java Annotation
// Sonst kann Compiler Interface nicht lesen

interface BottomBarContract {

    interface ViewModel {

        //region Actions
        fun onBottomBarItemClicked(item: BottomBarItem)
        //endregion

        fun getBottomBarNavigationEvent(): LiveData<BottomBarItem>
        //endregion NavigationEvents
    }

    interface Navigator {
        fun openBottomBarItem(item: BottomBarItem)
    }
}

sealed class BottomBarItem {
    object ListTrick : BottomBarItem()
    object SelectionTrick : BottomBarItem()
    object CreateTrick : BottomBarItem()
}