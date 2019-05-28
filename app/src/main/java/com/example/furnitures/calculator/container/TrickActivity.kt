package com.example.furnitures.calculator.container

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.bottomappbar.BottomAppBar
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.furnitures.R
import com.example.furnitures.calculator.container.list.ListTrickFragment
import com.example.furnitures.calculator.container.selection.SelectionFragment
import com.example.furnitures.calculator.container.selection.TrickViewModel
import com.example.furnitures.calculator.container.selection.ViewState

class TrickActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var trickViewModel: TrickViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trick_contrainer)

        fab = findViewById(R.id.fab)
        bottomAppBar = findViewById(R.id.bottom_app_bar)
        trickViewModel = ViewModelProviders.of(this).get(TrickViewModel::class.java)

        // lässt sich scheinbar nicht mit replaceMenu kombinieren
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
                trickViewModel.changeViewState()
            }
        }

        fab.setOnClickListener {
            fab.hide(addVisibilityChanged)
            invalidateOptionsMenu()

            if (savedInstanceState == null) {
                val tag = ListTrickFragment::class.java.name
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.activity_trick_container__frame_layout, ListTrickFragment.newInstance(), tag)
                    .addToBackStack(tag)
                    .commit()
            }
        }

        trickViewModel.getViewState().observe(this, Observer { newViewState ->
            switchFabAlignment(newViewState!!)
            removeBottomNavigationIcon(newViewState)
            replaceFabMenu(newViewState)
            setImageDrawable(newViewState)
            fab.show()
        })
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
                val bottomNavDrawerFragment = TrickBottomSheet()
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
        fun newIntent(context: Context): Intent = Intent(context, TrickActivity::class.java)
    }
}