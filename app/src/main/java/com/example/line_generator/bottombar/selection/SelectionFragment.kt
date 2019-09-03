package com.example.line_generator.bottombar.selection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.line_generator.R
import com.example.line_generator.bottombar.BottomBarContract
import com.example.line_generator.bottombar.BottomBarViewModel
import com.example.line_generator.bottombar.selection.trick_settings.TrickSettingsBottomSheetDialog
import com.example.line_generator.data.trick.*
import com.example.line_generator.extensions.pxFromDp
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_bottom_bar.*
import timber.log.Timber

class SelectionFragment : Fragment(), SelectionAdapter.TrickClickListener, TrickSettingsBottomSheetDialog.ClickListener {

    companion object {
        fun newInstance(): SelectionFragment = SelectionFragment()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: TrickContract.ViewModel
    private lateinit var activityViewModel: BottomBarContract.ViewModel
    private lateinit var emptySelectionLayout: View

    private var animationOnInitFlag: Boolean = true

    // Snackbar needs CoordinatorLayout
    private lateinit var snackBar: Snackbar

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
        val view = inflater.inflate(R.layout.fragment_tricks_recycler, container, false)
        recyclerView = view.findViewById(R.id.fragment_tricks__recycler)
        emptySelectionLayout = view.findViewById(R.id.nothing_selected_layout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().activity_bottom_bar_title.text = getString(R.string.toolbar_title_selection)
        val spanCount = resources.getInteger(com.example.line_generator.R.integer.fragment_selection__recycler_span_count)
        val spacing = resources.getDimensionPixelSize(com.example.line_generator.R.dimen.fragment_selection__recycler_spacing_grid)
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
        // Zusätzliche 25dp am Boden für einheitliches Padding
        recyclerView.setPadding(0, 0, 0, pxFromDp(this.context!!, spacing.toFloat()).toInt())
        recyclerView.adapter = adapter
        viewModel.getTrickViewStateWithHeader().observe(viewLifecycleOwner, Observer { newData ->
            if (newData != null) {
                switchScreen(newData)
                adapter.submitList(newData)
                animateRecyclerOnInit()
            }
        })
    }

    private fun animateRecyclerOnInit() {
        if (animationOnInitFlag) {
            loadAnimation()
            animationOnInitFlag = false
        }
    }

    override fun onTrickClicked(trickViewState: TrickViewState) {
        viewModel.onTrickClicked(trickViewState)
    }

    override fun onTrickLongPressed(trickViewState: TrickViewState) {
        showDeleteDialog(trickViewState)
    }

    override fun onDeleteClicked(item: TrickViewState) {
        viewModel.delete(item)
        if (item.userCreatedName != null) {
            snackBar = Snackbar
                .make(view!!.findViewById(R.id.fragment_tricks__recycler_container), "${item.userCreatedName} deleted!", Snackbar.LENGTH_LONG)
        } else if (item.name != null) {
            snackBar = Snackbar
                .make(view!!.findViewById(R.id.fragment_tricks__recycler_container), "${getString(TrickTypeHelper.getString(item.trickType)!!)} deleted!", Snackbar.LENGTH_LONG)
        }
        snackBar.setAction("UNDO", object : View.OnClickListener {
            override fun onClick(v: View?) {
                viewModel.insert(item)
            }
        })
        snackBar.show()
    }

    override fun onSaveClicked(id: String, name: String, difficulty: Difficulty, directionIn: DirectionIn, directionOut: DirectionOut) {
        viewModel.updateAfterEdit(id, name, directionIn, directionOut, difficulty)
    }

    private fun showDeleteDialog(item: TrickViewState) {
        val deleteBottomSheetDialog = TrickSettingsBottomSheetDialog.newInstance()
        val tag = SelectionFragment::class.java.name
        val args = Bundle()
        args.putSerializable("longPressedTrick", item)
        deleteBottomSheetDialog.arguments = args
        deleteBottomSheetDialog.show(childFragmentManager, tag)
    }

    private fun switchScreen(newData: List<RowViewState>) {
        if(newData.isEmpty()){
            emptySelectionLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptySelectionLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
//        if (viewModel.isSectionEmpty()) {
//            emptySelectionLayout.visibility = View.VISIBLE
//            recyclerView.visibility = View.GONE
//        } else {
//            emptySelectionLayout.visibility = View.GONE
//            recyclerView.visibility = View.VISIBLE
//        }
    }

    private fun loadAnimation() {
        val animFallDownRecycler = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        if (!activityViewModel.isSelectTrickInitialAnimationDone) {
            animFallDownRecycler.animation.startOffset = 700
            animFallDownRecycler.delay = 0.50f
            activityViewModel.isSelectTrickInitialAnimationDone = true
        } else {
            animFallDownRecycler.animation.startOffset = 0
            animFallDownRecycler.delay = 0.10f
        }
        recyclerView.layoutAnimation = animFallDownRecycler
        recyclerView.startLayoutAnimation()
        Timber.e("loadAnimation() Called")
        Log.d("DEBUGG", "loadAnimation() Called")

        val emptySelectionAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fade_in)
        emptySelectionAnimation.duration = 300
        emptySelectionLayout.startAnimation(emptySelectionAnimation)
    }
}