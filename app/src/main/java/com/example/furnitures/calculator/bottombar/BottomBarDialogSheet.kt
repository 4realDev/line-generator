package com.example.furnitures.calculator.bottombar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.furnitures.R
import com.example.furnitures.calculator.bottombar.selection.FurnitureContract
import com.example.furnitures.calculator.bottombar.selection.SelectionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

class BottomBarDialogSheet : BottomSheetDialogFragment() {

    private lateinit var navigationView: NavigationView
    private lateinit var viewmodel: FurnitureContract.ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trick_navigation_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigationView = view!!.findViewById(R.id.fragment_trick__navigation_view)
        viewmodel = ViewModelProviders.of(activity!!).get(SelectionViewModel::class.java)

//        navigationView.setNavigationItemSelectedListener { menuItem ->
//            // Bottom Navigation Drawer menu item clicks
//            when (menuItem.itemId) {
//                R.id.nav1 -> viewmodel.onFilterFurnitures(FurnitureCategory.SLIDE)
//                R.id.nav2 -> viewmodel.onFilterFurnitures(FurnitureCategory.GRIND)
//                R.id.nav3 -> viewmodel.onFilterFurnitures(FurnitureCategory.OTHER)
//                R.id.nav4 -> viewmodel.onFilterFurnitures(FurnitureCategory.UNDEFINED)
//            }
//            // Add code here to update the UI based on the item selected
//            // For example, swap UI fragments here
//            true
//        }
    }
}
