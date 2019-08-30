package com.example.line_generator.bottombar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BottomBarViewModel(application: Application) : AndroidViewModel(application), BottomBarContract.ViewModel {

    private var bottomBarNavigationEvent = MutableLiveData<BottomBarItem>()

    // Contract Property
    override var isSelectTrickInitialAnimationDone: Boolean = false

    override fun getBottomBarNavigationEvent(): LiveData<BottomBarItem> = bottomBarNavigationEvent

    override fun onBottomBarItemClicked(item: BottomBarItem) {
        bottomBarNavigationEvent.value = item
    }
}