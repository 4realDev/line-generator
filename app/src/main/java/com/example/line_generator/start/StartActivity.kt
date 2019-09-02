package com.example.line_generator.start

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.line_generator.R
import com.example.line_generator.bottombar.Navigator

class StartActivity : AppCompatActivity() {

    private lateinit var viewModel: StartContract.ViewModel
    private val navigator = Navigator(this)
    private lateinit var lineLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        lineLogo = findViewById(R.id.activity_start_line_logo)

        viewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)

        if(!viewModel.isStartAnimationDone) {
//            viewModel.startNavigationEventTimer()
            viewModel.isStartAnimationDone = true
        }

        viewModel.getNavigationEvent().observe(this, Observer {
            navigator.openBottomBarActivity()
        })
    }

    override fun onStart() {
        super.onStart()
        AnimationUtils.loadAnimation(this, R.anim.scale_line_logo).run {
            lineLogo.startAnimation(this)
        }
    }
}
