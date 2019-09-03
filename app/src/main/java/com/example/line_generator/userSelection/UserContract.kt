package com.example.line_generator.userSelection

import androidx.lifecycle.LiveData
import com.example.line_generator.data.user.User
import com.example.line_generator.util.SingleLiveEvent

interface UserContract {
    interface ViewModel {
        fun getStartActivityNavigationEvent(): SingleLiveEvent<Unit>
        fun getAllUsers(): LiveData<List<User>>
        fun createUser(name: String)
        fun onUserSelected(user: User)
    }

    interface Navigator {
        fun openStartActivity()
    }
}