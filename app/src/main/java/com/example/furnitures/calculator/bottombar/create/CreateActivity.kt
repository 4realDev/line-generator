package com.example.furnitures.calculator.bottombar.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.furnitures.R
import com.example.furnitures.calculator.trick.FurnitureCategory
import com.google.android.material.textfield.TextInputEditText
import info.hoang8f.android.segmented.SegmentedGroup

class CreateActivity : AppCompatActivity() {

    private lateinit var trickName: TextInputEditText
    private lateinit var viewModel: CreateViewModel
    private var name: String? = null
    private var category: FurnitureCategory = FurnitureCategory.SLIDE
    private lateinit var createBtn: Button
    private lateinit var categoryGroup: SegmentedGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.furnitures.R.layout.fragment_create_trick)

        trickName = findViewById(R.id.fragment_create__trick_name_input)
        createBtn = findViewById(R.id.activity_create_trick__create_button)
        categoryGroup = findViewById(R.id.activity_create_trick__category_group)
        viewModel = ViewModelProviders.of(this).get(CreateViewModel::class.java)

        trickName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                name = s.toString()

                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        createBtn.setOnClickListener {
            validateInput()
            true
        }

        categoryGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.activity_create_trick__category_slides -> category = FurnitureCategory.SLIDE
                R.id.activity_create_trick__category_grindes -> category = FurnitureCategory.GRIND
                R.id.activity_create_trick__category_others -> category = FurnitureCategory.OTHER
            }
        }
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

    companion object {

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, CreateActivity::class.java)
            return intent
        }
    }
}




