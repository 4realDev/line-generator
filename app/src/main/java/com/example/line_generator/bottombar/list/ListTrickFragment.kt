package com.example.line_generator.bottombar.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.line_generator.R
import com.example.line_generator.bottombar.create.SettingsContract
import com.example.line_generator.bottombar.settings.SettingsViewModel
import com.example.line_generator.helper.ItemDecorationEqualSpacing
import com.example.line_generator.data.trick.TrickViewState
import kotlinx.android.synthetic.main.activity_bottom_bar.*


class ListTrickFragment : Fragment() {

    companion object {
        fun newInstance(): ListTrickFragment = ListTrickFragment()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var nothingSelectedLayout: View
    private lateinit var listViewModel: ListTrickContract.ViewModel
    private lateinit var settingsViewModel: SettingsContract.ViewModel

    private lateinit var listTrickItemMoveCallback: ListTrickItemMoveCallback
    private lateinit var itemTouchHelper: ItemTouchHelper

    private var isInitAnimationLoaded = false

    private val adapter = ListTrickAdapter(
        object : ListTrickAdapter.TrickModificationListener {
            override fun onItemRemove(trickItem: TrickViewState) {
                listViewModel.removeTrickItem(trickItem)
            }

            // wird durch onStartDrag aufgerufen
            override fun onItemMove(fromIndex: Int, toIndex: Int) {
                listViewModel.changeTrickItemPosition(fromIndex, toIndex)
            }
        },
        object : ListTrickAdapter.OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                itemTouchHelper.startDrag(viewHolder)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // LifeCycle muss this sein, damit die Liste jedesmal neu generiert werden kann
        // (ViewModel wird bei jedem Aufruf vom Fragments neu erstellt)
        listViewModel = ViewModelProviders.of(this).get(ListTrickViewModel::class.java)
        // LifeCycleOwner muss Activity sein, damit Settings bei Fragment Transaction nicht verloren gehen
        settingsViewModel = ViewModelProviders.of(activity!!).get(SettingsViewModel::class.java)

        listTrickItemMoveCallback = ListTrickItemMoveCallback(adapter)
        itemTouchHelper = ItemTouchHelper(listTrickItemMoveCallback)

        requireActivity().activity_bottom_bar_title.text = getString(R.string.toolbar_title_list)

        // Use ItemTouchHelper (convenience class / bequemlichkeits Klasse)
        // Class that make our RecyclerView swipable
        // Pass SimpleCallBack to create the two Methods
        // SimpleCallback wants two parameters -> drag and drop direction and swipe direction
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tricks_recycler, container, false)
        nothingSelectedLayout = view.findViewById(R.id.nothing_selected_layout)
        recyclerView = view.findViewById(R.id.fragment_tricks__recycler)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.toolbar_title_list)
        val spacing = resources.getDimensionPixelSize(R.dimen.fragment_list__recycler_spacing)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val spaceLinear = ItemDecorationEqualSpacing(spacing)
        recyclerView.addItemDecoration(spaceLinear)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Observed alle veränderungen der Liste und gibt sie an den Adapter weiter
        // submitList löst automatisch notifyDataSetChanged aus
        listViewModel.getSelectedItems().observe(viewLifecycleOwner, Observer { newData ->
            if (newData != null) {
                adapter.onTrickItemsUpdate(newData)
            }
            switchScreen()
            // Problem: jedes mal wenn LiveData sich ändert, wird animation getriggert
            // Abfangen durch Boolean um einmaliges Laden beim Erstellen des Fragments zu ermöglichen
            if (!isInitAnimationLoaded) {
                loadAnimation()
                isInitAnimationLoaded = true
            }
        })
    }

    private fun switchScreen() {
        if (listViewModel.areTricksSelected()) {
            nothingSelectedLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        } else {
            nothingSelectedLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }

    private fun loadAnimation() {
        val animFallDownRecycler = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        animFallDownRecycler.animation.startOffset = 0
        animFallDownRecycler.animation.duration = 300
        animFallDownRecycler.delay = 0.25f
        recyclerView.layoutAnimation = animFallDownRecycler
        recyclerView.startLayoutAnimation()

        val noTrickSelectedAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fade_in)
        noTrickSelectedAnimation.duration = 300
        nothingSelectedLayout.startAnimation(noTrickSelectedAnimation)
    }
}
