package com.example.furnitures.calculator.bottombar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.furnitures.R
import com.example.furnitures.calculator.bottombar.selection.SelectionFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BottomBarActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var bottomBarViewModel: BottomBarViewModel
    private lateinit var navigator: BottomBarContract.Navigator
    private var isClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trick_contrainer)

        fab = findViewById(R.id.fab)
        bottomAppBar = findViewById(R.id.bottom_app_bar)

        navigator = BottomBarNavigator(this)
        bottomBarViewModel = ViewModelProviders.of(this).get(BottomBarViewModel::class.java)

        setSupportActionBar(bottomAppBar)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_trick_container__frame_layout, SelectionFragment.newInstance(), SelectionFragment::class.java.name)
                .commit()
        }

        val addVisibilityChanged: FloatingActionButton.OnVisibilityChangedListener = object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
                bottomBarViewModel.changeViewState()
            }
        }

        fab.setOnClickListener {
            isClicked = true
            fab.hide(addVisibilityChanged)
            invalidateOptionsMenu()
        }

        bottomBarViewModel.getViewState().observe(this, Observer { newViewState ->

            if (newViewState == ViewState.CHANGED_STATE && isClicked) {
                //bottomBarViewModel.onBottomBarItemClicked(BottomBarItem.ListTrick)
                navigator.openBottomBarItem(BottomBarItem.ListTrick)
                isClicked = false
            }

            if (newViewState == ViewState.INITIAL_STATE && isClicked) {
                supportFragmentManager.popBackStack()
                isClicked = false
            }

            switchFabAlignment(newViewState!!)
            removeBottomNavigationIcon(newViewState)
            replaceFabMenu(newViewState)
            setImageDrawable(newViewState)
            fab.show()
        })

//        bottomBarViewModel.getBottomBarNavigationEvent().observe(this, Observer { newItem ->
//            navigator.openBottomBarItem(newItem)
//        })
    }

    // Inflaten des ersten Menu's (mit Search Icon)
    // app:menu="@menu/menu_tricks_first_bottom_bar" nicht möglich
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_tricks_first_bottom_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomBarDialogSheet()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return true
    }

    private fun removeBottomNavigationIcon(newViewState: ViewState) {
        if (newViewState == ViewState.CHANGED_STATE)
            bottomAppBar.navigationIcon = null
        else bottomAppBar.navigationIcon = getDrawable(R.drawable.baseline_menu_white_24)
    }

    private fun switchFabAlignment(newViewState: ViewState) {
        if (newViewState == ViewState.CHANGED_STATE)
            bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        else bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
    }

    private fun replaceFabMenu(newViewState: ViewState) {
        if (newViewState == ViewState.CHANGED_STATE)
            bottomAppBar.replaceMenu(R.menu.menu_tricks_secondary_bottom_bar)
        else bottomAppBar.replaceMenu(R.menu.menu_tricks_first_bottom_bar)
    }

    private fun setImageDrawable(newViewState: ViewState) {
        if (newViewState == ViewState.CHANGED_STATE)
            fab.setImageDrawable(getDrawable(R.drawable.baseline_reply_white_24))
        else fab.setImageDrawable(getDrawable(R.drawable.ic_dice_24dp))
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, BottomBarActivity::class.java)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        bottomBarViewModel.changeViewState()
        isClicked = false
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, BottomBarActivity::class.java)
    }
}