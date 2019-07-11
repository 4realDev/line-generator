package com.example.furnitures.bottombar

import androidx.lifecycle.LiveData

// Interface benötigt .kt/.java Annotation
// Sonst kann Compiler Interface nicht lesen

interface BottomBarContract {

    interface ViewModel {

        //region Actions
        fun onBottomBarItemClicked(item: BottomBarItem)
        //endregion

        //region Data
        var selectTrickInitialAnimation: Boolean
        // endregion

        // region NavigationEvents
        fun getBottomBarNavigationEvent(): LiveData<BottomBarItem>
        //endregion
    }

    interface Navigator {
        fun openBottomBarItem(item: BottomBarItem)
        fun onBackPressed()
        fun onExitClicked()
    }
}

sealed class BottomBarItem {
    object ListTrick : BottomBarItem()
    object SelectionTrick : BottomBarItem()
    object CreateTrick : BottomBarItem()
    object SettingsTrick: BottomBarItem()
}