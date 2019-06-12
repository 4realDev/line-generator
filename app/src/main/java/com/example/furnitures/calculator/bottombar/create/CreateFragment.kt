package com.example.furnitures.calculator.bottombar.create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.furnitures.calculator.trick.FurnitureCategory
import com.google.android.material.textfield.TextInputEditText
import info.hoang8f.android.segmented.SegmentedGroup


class CreateFragment : Fragment() {

    private lateinit var trickName: TextInputEditText
    private lateinit var viewModel: CreateViewModel
    private var name: String? = null
    private var category: FurnitureCategory = FurnitureCategory.SLIDE
    private lateinit var createBtn: Button
    private lateinit var categoryGroup: SegmentedGroup
    //private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.example.furnitures.R.layout.fragment_create_trick, container, false)
        trickName = view.findViewById(com.example.furnitures.R.id.fragment_create__trick_name_input)
        createBtn = view.findViewById(com.example.furnitures.R.id.activity_create_trick__create_button)
        categoryGroup = view.findViewById(com.example.furnitures.R.id.activity_create_trick__category_group)
        //toolbar = view.findViewById(com.example.furnitures.R.id.activity_bottom_bar_appbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //toolbar.title = "Feedback"
        activity?.title = "SOMETHING"
        setupListeners()
    }

    private fun validateInput(): Boolean {
        return if (name == null) {
            trickName.error = "Field can't be empty"
            false
        } else {
            trickName.error = null
            viewModel.createTrick(name!!, category)
            true
        }
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
                com.example.furnitures.R.id.activity_create_trick__category_slides -> category = FurnitureCategory.SLIDE
                com.example.furnitures.R.id.activity_create_trick__category_grindes -> category = FurnitureCategory.GRIND
                com.example.furnitures.R.id.activity_create_trick__category_others -> category = FurnitureCategory.OTHER
            }
        }
    }

    companion object {
        fun newInstance() = CreateFragment()
    }
}




