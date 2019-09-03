package com.example.line_generator.userSelection

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.line_generator.Navigator
import com.example.line_generator.R
import com.google.android.material.textfield.TextInputEditText

class UserActivity : AppCompatActivity() {

    private lateinit var userName: TextInputEditText
    private lateinit var name: String
    private lateinit var createUserButton: Button

    private lateinit var spinner: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private var isSpinnerInitialized = false

    private lateinit var viewModel: UserContract.ViewModel
    private val navigator = Navigator(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

//        val userService = UserService(this)
//        val hasUserId = userService.getUserId() != ""
//        if(hasUserId) {
//            navigator.openStartActivity()
//        }

        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        userName = findViewById(R.id.activity_user_name_input_field)
        createUserButton = findViewById(R.id.activity_user_create_user_button)
        spinner = findViewById(R.id.activity_user_spinner)

        initListener()
        initOberservers()
    }

    private fun initListener(){

        spinner.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(isSpinnerInitialized) {
                    viewModel.onUserSelected(position)
                }
                isSpinnerInitialized = true
            }
        }

        userName.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                name = s.toString()
            }
        })

        createUserButton.setOnClickListener {
            viewModel.createUser(name)
        }
    }

    private fun initOberservers() {
        viewModel.getAllUsers().observe(this, Observer { newListOfUsers ->

            var userNameList = ArrayList<String>(newListOfUsers.size)

            if (newListOfUsers != null) {
                for (index in 0 until newListOfUsers.size) {
                    userNameList.add(newListOfUsers[index].name)
                }
            }

            spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userNameList)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = spinnerAdapter
        })

        viewModel.getStartActivityNavigationEvent().observe(this, Observer{ it ->
            navigator.openStartActivity()
        })
    }
}
