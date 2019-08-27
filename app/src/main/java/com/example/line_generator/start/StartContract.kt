package com.example.line_generator.start

import com.example.line_generator.util.SingleLiveEvent

interface StartContract {
    interface ViewModel{
        var isStartAnimationDone: Boolean
        fun startNavigationEventTimer()
        fun getNavigationEvent(): SingleLiveEvent<Unit>
    }
    interface Navigator{
        fun openBottomBarActivity()
    }
}