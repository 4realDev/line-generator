package com.example.furnitures.bottombar.create

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.furnitures.R
import com.example.furnitures.calculator.trick.FurnitureCategory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import info.hoang8f.android.segmented.SegmentedGroup
import kotlinx.android.synthetic.main.fragment_create_trick.*


class CreateFragment : Fragment() {

    private lateinit var viewModel: CreateViewModel
    private lateinit var categoryImage: ImageView
    private lateinit var trickName: TextInputEditText

    private lateinit var createBtn: FrameLayout
    private lateinit var createBtnText: TextView
    private lateinit var createBtnProgressBar: ProgressBar
    private lateinit var createBtnCheck: ImageView

    private lateinit var categoryGroup: SegmentedGroup
    private lateinit var category: FurnitureCategory

    private lateinit var snackBar: Snackbar
    private lateinit var snackBarView: View
    private lateinit var snackBarTextView: TextView

    private var name: String? = null
    private var createBtnCollapsedWidth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_trick, container, false)
        trickName = view!!.run { findViewById(R.id.fragment_create__trick_name_input) }
        createBtn = view.findViewById(R.id.fragment_create_trick__create_button)
        createBtnText = view.findViewById(R.id.fragment_create_trick__create_button_text)
        createBtnProgressBar = view.findViewById(R.id.fragment_create_trick__create_button_progress_bar)
        createBtnCheck = view.findViewById(R.id.fragment_create_trick__create_button_check)
        categoryImage = view.findViewById(R.id.fragment_create_trick__image)
        categoryGroup = view.findViewById(R.id.fragment_create_trick__category_group)

        snackBar = Snackbar.make(view.findViewById(R.id.fragment_create_trick__coordinatorLayout), "", Snackbar.LENGTH_SHORT)
        snackBarView = snackBar.view
        snackBarTextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
        snackBarTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.toolbar_title_create)
        setupDefaultValues()
        setupListeners()
        loadLayoutAnimation()
    }

    private fun setupDefaultValues() {
        categoryImage.setImageResource(R.drawable.ic_letter_g)
        category = FurnitureCategory.GRIND
        createBtnCollapsedWidth = resources.getDimension(R.dimen.fragment_create_trick__collapsed_create_button).toInt()
    }

    private fun setupListeners() {

        trickName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                name = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        createBtn.setOnClickListener {
            validateInput()
            true
        }

        categoryGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.fragment_create_trick__category_grindes -> {
                    category = FurnitureCategory.GRIND
                    loadCategoryAnimation()
                    categoryImage.setImageResource(R.drawable.ic_letter_g)
                }
                R.id.fragment_create_trick__category_slides -> {
                    category = FurnitureCategory.SLIDE
                    loadCategoryAnimation()
                    categoryImage.setImageResource(R.drawable.ic_letter_s)
                }
                R.id.fragment_create_trick__category_others -> {
                    category = FurnitureCategory.OTHER
                    loadCategoryAnimation()
                    categoryImage.setImageResource(R.drawable.ic_letter_o)
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        return if (name.isNullOrEmpty()) {
            trickName.error = "Field can't be empty"
            false
        } else {
            createBtn.isEnabled = false
            trickName.error = null
            viewModel.createTrick(name!!, category)
            snackBar.setText("$name successfully added")
            loadButtonToggle()
            true
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
        createBtnText.animate().alpha(0f).setDuration(250).setListener(object : AnimatorListenerAdapter() {
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
            animateButtonWidth(trickName.width)
            createBtn.requestLayout()
            createBtn.isEnabled = true
        }, 1750)
    }

    fun loadCategoryAnimation() {
        val animTtb = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_scale)
        fragment_create_trick__image.startAnimation(animTtb)
    }

    private fun loadLayoutAnimation() {
        val animFallDownRecycler = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        animFallDownRecycler.animation.startOffset = 0
        animFallDownRecycler.animation.duration = 300
        animFallDownRecycler.delay = 0.25f
        fragment_create_trick.layoutAnimation = animFallDownRecycler
        fragment_create_trick.startLayoutAnimation()
    }

    companion object {
        fun newInstance() = CreateFragment()
    }
}




