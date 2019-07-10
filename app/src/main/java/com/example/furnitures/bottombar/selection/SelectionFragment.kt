package com.example.furnitures.bottombar.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitures.R
import com.example.furnitures.bottombar.BottomBarContract
import com.example.furnitures.bottombar.BottomBarViewModel
import com.example.furnitures.calculator.extensions.pxFromDp
import com.example.furnitures.calculator.trick.FurnitureViewState

class SelectionFragment : Fragment(), SelectionAdapter.FurnitureClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: FurnitureContract.ViewModel
    private lateinit var activityViewModel: BottomBarContract.ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // wenn man this übergibt, übergibt man den Lifecycler des Fragments
        // ruft die Activity auch ViewModelProviders auf, so erstellt man zwei unterschiedliche Instanzen vom ViewModel
        // Damit beide die selbe Referenzieren, muss man bei beiden den selben Lifecylcer übergeben
        // -> Lifecycler von Acticity (da Activity länger lebt)
        viewModel = ViewModelProviders.of(activity!!).get(SelectionViewModel::class.java)
        activityViewModel = ViewModelProviders.of(activity!!).get(BottomBarViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.example.furnitures.R.layout.fragment_select_tricks, container, false)
        recyclerView = view.findViewById(com.example.furnitures.R.id.fragment_select_tricks__recycler)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(com.example.furnitures.R.string.toolbar_title_selection)
        val spanCount = resources.getInteger(com.example.furnitures.R.integer.fragment_selection__recycler_span_count)
        val spacing = resources.getDimensionPixelSize(com.example.furnitures.R.dimen.fragment_selection__recycler_spacing_grid)
        val adapter = SelectionAdapter(this)
        val manager = GridLayoutManager(activity, spanCount)

        // kommischerweise reverse?
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.isHeader(position)) spanCount
                else 1
            }
        }

        recyclerView.layoutManager = manager
        // Zusätzliche 25dp am Boden für e inheitliches Padding
        recyclerView.setPadding(0, 0, 0, pxFromDp(this.context!!, spacing.toFloat()).toInt())
        recyclerView.adapter = adapter
        viewModel.getFurnitureViewStateWithHeader().observe(viewLifecycleOwner, Observer { newData ->
            if (newData != null) adapter.submitList(newData)
        })

        loadAnimation()
    }

    override fun onFurnitureClicked(furniture: FurnitureViewState) {
        viewModel.onFurnitureClicked(furniture)
    }

    private fun loadAnimation() {
        val animFallDownRecycler = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        if (!activityViewModel.selectTrickInitialAnimation) {
            animFallDownRecycler.animation.startOffset = 700
            animFallDownRecycler.delay = 0.5f
            activityViewModel.selectTrickInitialAnimation = true
        } else {
            animFallDownRecycler.animation.startOffset = 0
            animFallDownRecycler.animation.duration = 300
            animFallDownRecycler.delay = 0.10f
        }
        recyclerView.layoutAnimation = animFallDownRecycler
        recyclerView.startLayoutAnimation()
    }

    companion object {
        fun newInstance(): SelectionFragment = SelectionFragment()
    }
}
