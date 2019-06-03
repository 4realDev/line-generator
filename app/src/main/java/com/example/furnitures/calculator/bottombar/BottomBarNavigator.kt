package com.example.furnitures.calculator.bottombar

import androidx.fragment.app.FragmentActivity
import com.example.furnitures.R
import com.example.furnitures.calculator.bottombar.list.ListTrickFragment
import com.example.furnitures.calculator.bottombar.selection.SelectionFragment

class BottomBarNavigator(activity: FragmentActivity): BottomBarContract.Navigator {

    private val activity = activity

    override fun openBottomBarItem(item: BottomBarItem) {
        when(item){
            BottomBarItem.TrickSelection -> openSelection()
            BottomBarItem.ListTrick -> openList()
        }
    }

    fun openSelection(){
        val tag = SelectionFragment::class.java.name
        activity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_trick_container__frame_layout, SelectionFragment.newInstance(), tag)
            .commit()
    }

    fun openList() {
        val tag = ListTrickFragment::class.java.name
        activity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_trick_container__frame_layout, ListTrickFragment.newInstance(), tag)
            .addToBackStack(tag)
            .commit()
    }
}