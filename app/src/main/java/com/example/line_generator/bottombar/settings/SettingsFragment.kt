package com.example.line_generator.bottombar.settings

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.line_generator.R
import com.example.line_generator.helper.TrickDifficultyHelper
import com.example.line_generator.data.trick.Difficulty
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_bottom_bar.*
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment(), OnChartValueSelectedListener {

    override fun onNothingSelected() {}

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (h != null) {
            val entryIndex = pieDataSet.getEntryIndex(e)
            val highlightedLabel = pieDataSet.getEntryForIndex(entryIndex).label
            val highlightedValue = pieDataSet.getEntryForIndex(entryIndex).value

            snackBar = Snackbar.make(view!!.findViewById(R.id.fragment_settings_trick__coordinatorLayout), "$highlightedLabel: $highlightedValue %", Snackbar.LENGTH_SHORT)
            snackBarView = snackBar.view
            snackBarTextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
            snackBarTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            snackBar.show()
        }
    }

    private lateinit var numberPickerDifficulty: NumberPicker
    private lateinit var difficulty: Difficulty
    private val difficultyList: Array<String> = arrayOf(
        Difficulty.SAVE.name.toLowerCase().capitalize(),
        Difficulty.EASY.name.toLowerCase().capitalize(),
        Difficulty.MIDDLE.name.toLowerCase().capitalize(),
        Difficulty.HARD.name.toLowerCase().capitalize(),
        Difficulty.CRAZY.name.toLowerCase().capitalize()
    )

    private var pieEntries = ArrayList<PieEntry>()
    private lateinit var pieChart: PieChart
    private lateinit var pieDataSet: PieDataSet
    private lateinit var pieData: PieData

    private lateinit var numberPickerMaxTricks: NumberPicker
    private lateinit var viewModel: SettingsViewModel

    private var bgColors = ArrayList<Int>(5)
    private var difficultyPercentageArray = FloatArray(5)

    private lateinit var snackBar: Snackbar
    private lateinit var snackBarView: View
    private lateinit var snackBarTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

    }

    private fun setupChartColors() {
        bgColors = arrayListOf(
            getColor(activity!!.applicationContext, R.color.save),
            getColor(activity!!.applicationContext, R.color.easy),
            getColor(activity!!.applicationContext, R.color.middle),
            getColor(activity!!.applicationContext, R.color.hard),
            getColor(activity!!.applicationContext, R.color.crazy)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        numberPickerMaxTricks = view.findViewById(R.id.fragment_settings_trick__number_picker_max_tricks)
        pieChart = view.findViewById(R.id.fragment_settings_trick_pie_chart)
        numberPickerDifficulty = view.findViewById(R.id.fragment_settings_trick__number_picker_difficulty)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().activity_bottom_bar_title.text = getString(R.string.toolbar_title_settings)
        pieChart.setOnChartValueSelectedListener(this)
        setupDefaultValues()
        initObservers()
        initListeners()
        loadLayoutAnimation()
    }

    private fun setupPieChart() {
        getEntries()
        setupChartColors()
        setupPieLegend()
        setupPieData()
        setupPieView()
    }

    private fun setupDefaultValues() {
        getNumberOfTricks()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        numberPickerMaxTricks.setOnValueChangedListener { _, _, newVal ->
            viewModel.onMaxTricksSelected(newVal)
        }


        numberPickerDifficulty.setOnValueChangedListener { _, _, newVal ->
            //            viewModel.onDifficultySelected(TrickDifficultyHelper.getDifficulty(difficultyList[newVal]))
            viewModel.onDifficultySelected(TrickDifficultyHelper.getFurnitureDifficulty(difficultyList[newVal]))
        }

        // tell the View, that we dont handle the event
        // view give event further on the ArcProgressStackView.java class
        // this class handles the remaining logic in her onTouchEvent


        // Logging Langsam nicht in onDraw
        // onDraw läuft in MainThread
        // 16ms für 60fps Refresh Rate für Flüssige Bewegung
        // Log.d("DEBUG", "onTouchEvent ${models[0].progress}")
    }

    private fun initObservers() {
        viewModel.getDifficulty().observe(this, Observer { newDifficulty ->
            difficulty = newDifficulty
            setupDifficulty()
        })
        viewModel.getDifficultyPercentage().observe(this, Observer { newDifficultyPercentage ->
            difficultyPercentageArray = newDifficultyPercentage
            setupPieChart()
        })
    }


    private fun setupPieView() {
        pieChart.data = pieData
        pieChart.isDrawHoleEnabled = false
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.description.isEnabled = false
    }

    private fun setupPieData() {
        pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.colors = bgColors
        // number color
        pieDataSet.valueTextColor = Color.TRANSPARENT
        pieData = PieData(pieDataSet)
        pieChart.invalidate() // refresh
    }

    private fun setupPieLegend() {
        val l = pieChart.legend
        l.form = Legend.LegendForm.SQUARE
        l.formSize = 10f
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.textColor = Color.WHITE
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.textSize = 12f
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
    }

    private fun setupDifficulty() {
        val difficultyIndex = difficulty.weight - 1
        numberPickerDifficulty.maxValue = difficultyList.size - 1
        numberPickerDifficulty.value = difficultyIndex
        numberPickerDifficulty.wrapSelectorWheel = true
        numberPickerDifficulty.displayedValues = difficultyList
    }

    private fun getNumberOfTricks() {
        numberPickerMaxTricks.minValue = 1
        numberPickerMaxTricks.maxValue = 20
        numberPickerMaxTricks.value = SettingsService.getMaxTricks(context!!)
        numberPickerMaxTricks.wrapSelectorWheel = true
    }

    private fun getEntries() {
        pieEntries.removeAll(pieEntries)
        pieEntries.add(PieEntry(difficultyPercentageArray[0], "Save"))
        pieEntries.add(PieEntry(difficultyPercentageArray[1], "Easy"))
        pieEntries.add(PieEntry(difficultyPercentageArray[2], "Middle"))
        pieEntries.add(PieEntry(difficultyPercentageArray[3], "Hard"))
        pieEntries.add(PieEntry(difficultyPercentageArray[4], "Crazy"))
        pieChart.animateY(1500, Easing.EaseInOutExpo)
    }

    private fun loadLayoutAnimation() {
        val animFadeInFragment = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fade_in)
        val animFadeInImage = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_scale)
        animFadeInFragment.startOffset = 0
        animFadeInFragment.duration = 600
        animFadeInImage.startOffset = 400
        fragment_settings_trick__coordinatorLayout.startAnimation(animFadeInFragment)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}




