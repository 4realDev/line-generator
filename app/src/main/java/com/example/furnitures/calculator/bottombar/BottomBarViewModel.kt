package com.example.furnitures.calculator.bottombar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BottomBarViewModel(application: Application) : AndroidViewModel(application), BottomBarContract.ViewModel {

    private var bottomBarNavigationEvent = MutableLiveData<BottomBarItem>()

    override fun getBottomBarNavigationEvent(): LiveData<BottomBarItem> = bottomBarNavigationEvent

    override fun onBottomBarItemClicked(item: BottomBarItem) {
        bottomBarNavigationEvent.value = item
    }
}