package com.example.furnitures.calculator.bottombar.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitures.calculator.extensions.pxFromDp
import com.example.furnitures.calculator.trick.FurnitureViewState




class SelectionFragment : Fragment(), SelectionAdapter.FurnitureClickListener {

    private lateinit var recyclerView: RecyclerView
    //private FurnitureContract.Navigator navigator;
    private lateinit var viewModel: FurnitureContract.ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AppAnalyzer.logEvent(AppAnalyzer.EVENT_SHOW_TRUCK_CALCULATOR_ITEMS);
        //setHasOptionsMenu(true);
        //navigator = new CalculatorNavigator(this);

        // wenn man this übergibt, übergibt man den Lifecycler des Fragments
        // ruft die Activity auch ViewModelProviders auf, so erstellt man zwei unterschiedliche Instanzen vom ViewModel
        // Damit beide die selbe Referenzieren, muss man bei beiden den selben Lifecylcer übergeben
        // -> Lifecycler von Acticity (da Activity länger lebt)
        viewModel = ViewModelProviders.of(activity!!).get(SelectionViewModel::class.java)
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
                val viewType = adapter.getItemViewType(position)
                if(adapter.isHeader(position)) return spanCount
                else return 1
            }
        }

        recyclerView.layoutManager = manager
        // Zusätzliche 25dp am Boden für einheitliches Padding
        recyclerView.setPadding(0, 0, 0, pxFromDp(this.context!!, 25f).toInt())
        recyclerView.adapter = adapter
        viewModel.getFurnitureViewStateWithHeader().observe(viewLifecycleOwner, Observer { newData ->
            if (newData != null) adapter.submitList(newData)
        })

        //viewModel.getUpNavigationEvent().observe(this, nothing -> navigator.onNavigateUpFromFurniture());
    }

    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
    //    switch (item.getItemId()) {
    //        case android.R.id.home:
    //            viewModel.onUpClicked();
    //            return true;
    //        default:
    //            return super.onOptionsItemSelected(item);
    //    }
    //}

    //region ListTrickAdapter.FurnitureClickListener
    override fun onFurnitureClicked(furniture: FurnitureViewState) {
        viewModel.onFurnitureClicked(furniture)
    }

    companion object {
        fun newInstance(): SelectionFragment = SelectionFragment()
    }
}
