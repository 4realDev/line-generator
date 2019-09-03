package com.example.line_generator.bottombar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.line_generator.Navigator
import com.example.line_generator.R
import com.example.line_generator.userSelection.UserService
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView




class BottomBarActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, ConfirmExitDialogFragment.ExitDialogClickListener {

    companion object {
        fun newIntent(context: Context) = Intent(context, BottomBarActivity::class.java)
    }

    private lateinit var bottomAppBar: BottomNavigationView
    private lateinit var bottomBarViewModel: BottomBarViewModel
    private lateinit var navigator: BottomBarContract.Navigator
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var appBar: AppBarLayout
    private lateinit var user: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_bar)

        bottomAppBar = findViewById(R.id.bottom_app_bar)
        toolbar = findViewById(R.id.activity_bottom_bar_toolbar)
        appBar = findViewById(R.id.activity_bottom_bar_appbar_layout)
        user = findViewById(R.id.activity_bottom_bar_user)

        val userName = UserService(this).getUserName()
        user.text = userName

        bottomAppBar.setOnNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar)

        navigator = Navigator(this)
        bottomBarViewModel = ViewModelProviders.of(this).get(BottomBarViewModel::class.java)

        loadAnimation()

        // Item by default should be selection
        bottomAppBar.selectedItemId = R.id.menu_bottombar_selection
    }

    override fun onBackPressed() {
        openExitDialog()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_bottombar_selection -> navigator.openBottomBarItem(BottomBarItem.SelectionTrick)
            R.id.menu_bottombar_list -> navigator.openBottomBarItem(BottomBarItem.ListTrick)
            R.id.menu_bottombar_create -> navigator.openBottomBarItem(BottomBarItem.CreateTrick)
            R.id.menu_bottombar_settings -> navigator.openBottomBarItem(BottomBarItem.SettingsTrick)
        }
        return true
    }

    override fun onExitClicked() {
        navigator.onExitClicked()
    }

    private fun loadAnimation() {
        val animTtb = AnimationUtils.loadAnimation(this, R.anim.appbar_animation_top_to_bottom)
        val animBtt = AnimationUtils.loadAnimation(this, R.anim.bottombar_animation_bottom_to_top)
        appBar.startAnimation(animTtb)
        bottomAppBar.startAnimation(animBtt)
    }

    private fun openExitDialog() {
        navigator.onBackPressed()
    }
}