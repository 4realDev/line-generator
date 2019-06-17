package com.example.furnitures.calculator.bottombar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.furnitures.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class BottomBarActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomAppBar: BottomNavigationView
    private lateinit var bottomBarViewModel: BottomBarViewModel
    private lateinit var navigator: BottomBarContract.Navigator
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.furnitures.R.layout.activity_bottom_bar)

        bottomAppBar = findViewById(com.example.furnitures.R.id.bottom_app_bar)
        toolbar = findViewById(R.id.activity_bottom_bar_appbar)

        bottomAppBar.setOnNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar)

        navigator = BottomBarNavigator(this)
        bottomBarViewModel = ViewModelProviders.of(this).get(BottomBarViewModel::class.java)

        navigator.openBottomBarItem(BottomBarItem.SelectionTrick)

        // Item by default should be selection
        bottomAppBar.selectedItemId = R.id.menu_bottombar_selection
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_bottombar_settings -> {
                val bottomNavDrawerFragment = BottomBarDialogSheet()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
            R.id.menu_bottombar_selection -> navigator.openBottomBarItem(BottomBarItem.SelectionTrick)
            R.id.menu_bottombar_list -> navigator.openBottomBarItem(BottomBarItem.ListTrick)
            R.id.menu_bottombar_create -> navigator.openBottomBarItem(BottomBarItem.CreateTrick)
        }
        return true
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, BottomBarActivity::class.java)
    }
}