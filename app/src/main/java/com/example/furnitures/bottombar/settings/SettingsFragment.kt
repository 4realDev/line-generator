package com.example.furnitures.bottombar.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.furnitures.R
import com.example.furnitures.bottombar.settings.SettingsService
import com.example.furnitures.trick.FurnitureDifficulty
import com.example.furnitures.trick.FurnitureDifficultyHelper
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var categoryImage: ImageView
    private lateinit var numberPickerMaxTricks: NumberPicker
    private lateinit var numberPickerDifficulty: NumberPicker
    private val difficultyList: Array<String> = arrayOf(
        FurnitureDifficulty.SAVE.name.toLowerCase().capitalize(),
        FurnitureDifficulty.EASY.name.toLowerCase().capitalize(),
        FurnitureDifficulty.MIDDLE.name.toLowerCase().capitalize(),
        FurnitureDifficulty.HARD.name.toLowerCase().capitalize(),
        FurnitureDifficulty.CRAZY.name.toLowerCase().capitalize()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        categoryImage = view.findViewById(R.id.fragment_settings_trick__image)
        numberPickerMaxTricks = view.findViewById(R.id.fragment_settings_trick__number_picker_max_tricks)
        numberPickerDifficulty = view.findViewById(R.id.fragment_settings_trick__number_picker_difficulty)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.toolbar_title_settings)
        setupDefaultValues()
        setupListeners()
        loadLayoutAnimation()
    }

    private fun setupDefaultValues() {
        numberPickerMaxTricks.minValue = 1
        numberPickerMaxTricks.maxValue = 20
        numberPickerMaxTricks.value = SettingsService.getMaxTricks(context!!)
        numberPickerMaxTricks.wrapSelectorWheel = true

        val defaultDifficulty = SettingsService.getDifficulty(context!!).weight - 1
        numberPickerDifficulty.maxValue = difficultyList.size - 1
        numberPickerDifficulty.value = defaultDifficulty
        numberPickerDifficulty.wrapSelectorWheel = true
        numberPickerDifficulty.displayedValues = difficultyList
    }

    private fun setupListeners() {


        numberPickerMaxTricks.setOnValueChangedListener { _, _, newVal ->
            viewModel.setMaxTricks(newVal)
        }

        numberPickerDifficulty.setOnValueChangedListener { _, _, newVal ->
            viewModel.setDifficulty(FurnitureDifficultyHelper.getFurnitureDifficulty(difficultyList[newVal]))
        }
    }

    private fun loadLayoutAnimation() {
        val animFadeInFragment = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fade_in)
        val animFadeInImage = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_scale)
        animFadeInFragment.startOffset = 0
        animFadeInFragment.duration = 600
        animFadeInImage.startOffset = 400
        fragment_settings_trick.startAnimation(animFadeInFragment)
        fragment_settings_trick__image.startAnimation(animFadeInImage)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}




