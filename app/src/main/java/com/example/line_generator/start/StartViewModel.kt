package com.example.line_generator.start

import android.app.Application
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import com.example.line_generator.util.SingleLiveEvent

class StartViewModel(application: Application) : AndroidViewModel(application), StartContract.ViewModel {

    override var isStartAnimationDone = false
    private val navigationEvent = SingleLiveEvent<Unit>()

    override fun getNavigationEvent() = navigationEvent

    override fun startNavigationEventTimer() {
        val handler = Handler()
        handler.postDelayed({
            navigationEvent.call()
        }, SPLASH_SCREEM_TIMEOUT_LONG)
    }

    companion object {
        const val SPLASH_SCREEM_TIMEOUT_LONG = 1500L
    }
}