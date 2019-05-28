package com.example.furnitures.calculator.container

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.NavigationView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.furnitures.R
import com.example.furnitures.calculator.container.selection.FurnitureContract
import com.example.furnitures.calculator.container.selection.SelectionViewModel
import com.example.furnitures.calculator.container.selection.FurtnitureCategory

class TrickBottomSheet : BottomSheetDialogFragment() {

    private lateinit var navigationView: NavigationView
    private lateinit var viewmodel: FurnitureContract.ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trick_navigation_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigationView = view!!.findViewById(R.id.fragment_trick__navigation_view)
        viewmodel = ViewModelProviders.of(activity!!).get(SelectionViewModel::class.java)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Bottom Navigation Drawer menu item clicks
            when (menuItem.itemId) {
                R.id.nav1 -> viewmodel.onFilterFurnitures(FurtnitureCategory.ROOF)
                R.id.nav2 -> viewmodel.onFilterFurnitures(FurtnitureCategory.GARDEN)
                R.id.nav3 -> viewmodel.onFilterFurnitures(FurtnitureCategory.HOUSE)
                R.id.nav4 -> viewmodel.onFilterFurnitures(FurtnitureCategory.UNDEFINED)
            }
            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            true
        }
    }
}
