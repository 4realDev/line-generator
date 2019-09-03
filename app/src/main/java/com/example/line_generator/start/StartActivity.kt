package com.example.line_generator.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.line_generator.Navigator


class StartActivity : AppCompatActivity() {

    companion object{
        fun newIntent(context: Context) = Intent(context, StartActivity::class.java)
    }

    private lateinit var viewModel: StartContract.ViewModel
    private val navigator = Navigator(this)
    private lateinit var lineLogo: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.line_generator.R.layout.activity_start)
        lineLogo = findViewById(com.example.line_generator.R.id.activity_start_line_logo)


        viewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)

        initObservers()

        if(!viewModel.isStartAnimationDone) {
            viewModel.isStartAnimationDone = true
        }
    }



    override fun onStart() {
        super.onStart()
        AnimationUtils.loadAnimation(this, com.example.line_generator.R.anim.scale_line_logo).run {
            lineLogo.startAnimation(this)
        }
    }

    private fun initObservers(){
        viewModel.getNavigationEvent().observe(this, Observer {
            navigator.openBottomBarActivity()
        })
    }
}
