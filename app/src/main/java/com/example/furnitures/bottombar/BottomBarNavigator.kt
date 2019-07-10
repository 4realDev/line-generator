package com.example.furnitures.bottombar

import androidx.fragment.app.FragmentActivity
import com.example.furnitures.bottombar.create.CreateFragment
import com.example.furnitures.bottombar.list.ListTrickFragment
import com.example.furnitures.bottombar.selection.SelectionFragment



class BottomBarNavigator(activity: FragmentActivity) : BottomBarContract.Navigator {

    private val activity = activity

    override fun openBottomBarItem(item: BottomBarItem) {
        when (item) {
            BottomBarItem.SelectionTrick -> openSelection()
            BottomBarItem.ListTrick -> openList()
            BottomBarItem.CreateTrick -> openCreate()
        }
    }

    override fun onBackPressed() {
        val tag = ConfirmExitDialogFragment::class.java.name
        ConfirmExitDialogFragment.newInstance().show(activity.supportFragmentManager, tag)
    }

    override fun onExitClicked() {
        android.os.Process.killProcess(android.os.Process.myPid())
        activity.finish()
    }

    fun openSelection() {
        val tag = SelectionFragment::class.java.name
        activity.supportFragmentManager
            .beginTransaction()
            .replace(com.example.furnitures.R.id.activity_trick_container__frame_layout, SelectionFragment.newInstance(), tag)
            .commit()
    }

    fun openList() {
        val tag = ListTrickFragment::class.java.name
        activity.supportFragmentManager
            .beginTransaction()
            .replace(com.example.furnitures.R.id.activity_trick_container__frame_layout, ListTrickFragment.newInstance(), tag)
            .commit()
    }

    fun openCreate() {
        val tag = CreateFragment::class.java.name
        activity.supportFragmentManager
            .beginTransaction()
            .replace(com.example.furnitures.R.id.activity_trick_container__frame_layout, CreateFragment.newInstance(), tag)
            .commit()
    }
}