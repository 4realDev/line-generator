package com.example.furnitures.bottombar.create

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.furnitures.R
import com.example.furnitures.bottombar.settings.SettingsService
import com.example.furnitures.trick.FurnitureDifficulty
import com.example.furnitures.trick.FurnitureDifficultyHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_trick.fragment_create_trick
import kotlinx.android.synthetic.main.fragment_create_trick.fragment_create_trick__create_button
import kotlinx.android.synthetic.main.fragment_create_trick.fragment_create_trick__image
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var categoryImage: ImageView
    private lateinit var numberPickerMaxTricks: NumberPicker
    private lateinit var numberPickerDifficulty: NumberPicker
    private val difficultyList: Array<String> = arrayOf(
        FurnitureDifficulty.JOKE.name.toLowerCase().capitalize(),
        FurnitureDifficulty.EASY.name.toLowerCase().capitalize(),
        FurnitureDifficulty.MIDDLE.name.toLowerCase().capitalize(),
        FurnitureDifficulty.HARD.name.toLowerCase().capitalize(),
        FurnitureDifficulty.CRAZY.name.toLowerCase().capitalize()
    )
    private lateinit var createBtn: FrameLayout
    private lateinit var createBtnText: TextView
    private lateinit var createBtnProgressBar: ProgressBar
    private lateinit var createBtnCheck: ImageView

    private lateinit var snackBar: Snackbar
    private lateinit var snackBarView: View
    private lateinit var snackBarTextView: TextView

    private var createBtnCollapsedWidth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        createBtn = view.findViewById(R.id.fragment_create_trick__create_button)
        createBtnText = view.findViewById(R.id.fragment_create_trick__create_button_text)
        createBtnProgressBar = view.findViewById(R.id.fragment_create_trick__create_button_progress_bar)
        createBtnCheck = view.findViewById(R.id.fragment_create_trick__create_button_check)
        categoryImage = view.findViewById(R.id.fragment_create_trick__image)
        numberPickerMaxTricks = view.findViewById(R.id.fragment_settings_trick__number_picker_max_tricks)
        numberPickerDifficulty = view.findViewById(R.id.fragment_settings_trick__number_picker_difficulty)

        snackBar = Snackbar.make(view.findViewById(R.id.fragment_create_trick__coordinatorLayout), "Settings succefully saved", Snackbar.LENGTH_SHORT)
        snackBarView = snackBar.view
        snackBarTextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
        snackBarTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
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
        createBtnCollapsedWidth = resources.getDimension(R.dimen.fragment_create_trick__collapsed_create_button).toInt()

        numberPickerMaxTricks.minValue = 1
        numberPickerMaxTricks.maxValue = 69
        numberPickerMaxTricks.value = SettingsService.getMaxTricks(context!!)
        numberPickerMaxTricks.wrapSelectorWheel = true

        val defaultDifficulty = SettingsService.getDifficulty(context!!).weight - 1
        numberPickerDifficulty.maxValue = difficultyList.size - 1
        numberPickerDifficulty.value = defaultDifficulty
        numberPickerDifficulty.wrapSelectorWheel = true
        numberPickerDifficulty.displayedValues = difficultyList

//        class StringFormatter : NumberPicker.Formatter {
//            override fun format(value: Int): String {
//                // catch 0 and set default value
//                return when (value) {
//                    0 -> SettingsService.getDifficulty(context!!)
//                    else -> throw Throwable("Unknown value: $value")
//                }
//            }
//        }
//
//        numberPickerDifficulty.setFormatter(StringFormatter())
    }

    private fun setupListeners() {
        createBtn.setOnClickListener {
            loadButtonToggle()
            true
        }

        numberPickerMaxTricks.setOnValueChangedListener { _, _, newVal ->
            viewModel.setMaxTricks(newVal)
        }

        numberPickerDifficulty.setOnValueChangedListener { _, _, newVal ->
            viewModel.setDifficulty(FurnitureDifficultyHelper.getFurnitureDifficulty(difficultyList[newVal]))
        }
    }

    private fun loadButtonToggle() {
        animateButtonWidth(createBtnCollapsedWidth)
        fadeOutTextAndSetProgressDialog()
        fadeOutProgressDialogAndSetCheck()
        fadeOutCheckAndSetText()
    }

    private fun animateButtonWidth(width: Int) {
        val anim: ValueAnimator = ValueAnimator.ofInt(createBtn.measuredWidth, width)
        anim.addUpdateListener {
            var value = it.animatedValue as Int
            var layoutParams: ViewGroup.LayoutParams = createBtn.layoutParams
            layoutParams.width = value
            createBtn.requestLayout()
        }
        createBtn.setBackgroundResource(R.drawable.background_create_button_shape)
        anim.duration = 250
        anim.start()

    }

    private fun fadeOutTextAndSetProgressDialog() {
        createBtnText.animate().alpha(0f).setDuration(150).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                createBtnProgressBar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
                createBtnProgressBar.animate().alpha(1f).setDuration(200).start()
                // Remove AnimatorListener, so ProgressBar would be toggeled again, when the createBtnText shows second time
                createBtnText.animate().setListener(null)
            }
        }).start()
        createBtn.requestLayout()
    }

    private fun fadeOutProgressDialogAndSetCheck() {
        val handler = Handler()
        handler.postDelayed({
            createBtnProgressBar.animate().alpha(0f).setDuration(200).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    createBtnCheck.animate().alpha(1f).setDuration(200).start()
                    createBtnProgressBar.animate().setListener(null)
                }
            }).start()
            createBtn.requestLayout()
        }, 1000)
    }

    private fun fadeOutCheckAndSetText() {
        val handler = Handler()
        handler.postDelayed({
            createBtnCheck.animate().alpha(0f).setDuration(250).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    createBtnText.animate().alpha(1f).setDuration(250).start()
                    createBtn.setBackgroundResource(R.drawable.background_create_button_shape2)
                    createBtnCheck.animate().setListener(null)
                }
            }).start()
            snackBar.show()
            animateButtonWidth(fragment_settings_trick__number_picker_max_tricks.width * 2)
            createBtn.requestLayout()
            createBtn.isEnabled = true
        }, 1750)
    }

    private fun loadLayoutAnimation() {
        val animFadeInFragment = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fade_in)
        val animFadeInImage = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_scale)
        val animFadeInCreateBtn = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fade_in)
        animFadeInFragment.startOffset = 0
        animFadeInFragment.duration = 1000
        animFadeInCreateBtn.startOffset = 600
        animFadeInImage.startOffset = 1000
        fragment_create_trick.startAnimation(animFadeInFragment)
        fragment_create_trick__image.startAnimation(animFadeInImage)
        fragment_create_trick__create_button.startAnimation(animFadeInCreateBtn)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}




