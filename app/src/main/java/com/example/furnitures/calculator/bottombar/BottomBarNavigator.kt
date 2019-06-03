package com.example.furnitures.calculator.bottombar

import androidx.fragment.app.FragmentActivity
import com.example.furnitures.R
import com.example.furnitures.calculator.bottombar.list.ListTrickFragment
import com.example.furnitures.calculator.bottombar.selection.BottomBarContract
import com.example.furnitures.calculator.bottombar.selection.BottomBarItem
import com.example.furnitures.calculator.bottombar.selection.SelectionFragment

class BottomBarNavigator: FragmentActivity(), BottomBarContract.Navigator {

    override fun openBottomBarItem(item: BottomBarItem) {
        when(item){
            BottomBarItem.TrickSelection -> openSelection()
            BottomBarItem.TrickList -> openList()
        }
    }

    fun openSelection(){
        val tag = ListTrickFragment::class.java.name
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_trick_container__frame_layout, ListTrickFragment.newInstance(), tag)
            .addToBackStack(tag)
    }

    fun openList() {
        val tag = SelectionFragment::class.java.name
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_trick_container__frame_layout, SelectionFragment.newInstance(), tag)
            .addToBackStack(tag)
    }
}