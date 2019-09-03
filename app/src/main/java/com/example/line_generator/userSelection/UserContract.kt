package com.example.line_generator.userSelection

import androidx.lifecycle.LiveData
import com.example.line_generator.data.user.UserViewState
import com.example.line_generator.util.SingleLiveEvent

interface UserContract {
    interface ViewModel {
        fun getStartActivityNavigationEvent(): SingleLiveEvent<Unit>
        fun getAllUsers(): LiveData<List<UserViewState>>
        fun createUser(name: String)
        fun onUserSelected(position: Int)
    }

    interface Navigator {
        fun openStartActivity()
    }
}