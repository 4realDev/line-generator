package com.example.furnitures.bottombar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.furnitures.R.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView


class BottomBarActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, ConfirmExitDialogFragment.ExitDialogClickListener {

    private lateinit var bottomAppBar: BottomNavigationView
    private lateinit var bottomBarViewModel: BottomBarViewModel
    private lateinit var navigator: BottomBarContract.Navigator
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var appBar: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_bottom_bar)

        bottomAppBar = findViewById(id.bottom_app_bar)
        toolbar = findViewById(id.activity_bottom_bar_toolbar)
        appBar = findViewById(id.activity_bottom_bar_appbar_layout)

        bottomAppBar.setOnNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar)

        navigator = BottomBarNavigator(this)
        bottomBarViewModel = ViewModelProviders.of(this).get(BottomBarViewModel::class.java)

        // Item by default should be selection
        bottomAppBar.selectedItemId = id.menu_bottombar_selection

        loadAnimation()
    }

    override fun onBackPressed() {
        openExitDialog()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            id.menu_bottombar_selection -> navigator.openBottomBarItem(BottomBarItem.SelectionTrick)
            id.menu_bottombar_list -> navigator.openBottomBarItem(BottomBarItem.ListTrick)
            id.menu_bottombar_create -> navigator.openBottomBarItem(BottomBarItem.CreateTrick)
            id.menu_bottombar_settings -> navigator.openBottomBarItem(BottomBarItem.SettingsTrick)
        }
        return true
    }

    override fun onExitClicked() {
        navigator.onExitClicked()
    }

    private fun loadAnimation() {
        val animTtb = AnimationUtils.loadAnimation(this, anim.appbar_animation_top_to_bottom)
        val animBtt = AnimationUtils.loadAnimation(this, anim.bottombar_animation_bottom_to_top)
        appBar.startAnimation(animTtb)
        bottomAppBar.startAnimation(animBtt)
    }

    private fun openExitDialog(){
        navigator.onBackPressed()
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, BottomBarActivity::class.java)
    }
}