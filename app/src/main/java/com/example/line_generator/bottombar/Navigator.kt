package com.example.line_generator.bottombar

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.example.line_generator.R
import com.example.line_generator.bottombar.create.CreateFragment
import com.example.line_generator.bottombar.list.ListTrickFragment
import com.example.line_generator.bottombar.selection.SelectionFragment
import com.example.line_generator.bottombar.settings.SettingsFragment
import com.example.line_generator.start.StartContract
import timber.log.Timber


class Navigator(activity: FragmentActivity) : BottomBarContract.Navigator, StartContract.Navigator {

    private val activity = activity

    override fun openBottomBarItem(item: BottomBarItem) {
        when (item) {
            BottomBarItem.SelectionTrick -> openSelection()
            BottomBarItem.ListTrick -> openList()
            BottomBarItem.CreateTrick -> openCreate()
            BottomBarItem.SettingsTrick -> openSettings()
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

    override fun openBottomBarActivity() {
        activity.startActivity(BottomBarActivity.newIntent(activity))
        activity.finish()
        Timber.e("finish() StartActivity")
        Log.d("DEBUGG", "finish() StartActivity")
    }

    fun openSelection() {
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
            .commit()
    }

    fun openCreate() {
        val tag = CreateFragment::class.java.name
        activity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_trick_container__frame_layout, CreateFragment.newInstance(), tag)
            .commit()
    }

    fun openSettings() {
        val tag = BottomBarItem.SettingsTrick::class.java.name
        activity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_trick_container__frame_layout, SettingsFragment.newInstance(), tag)
            .commit()
    }
}