package com.example.line_generator.userSelection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.line_generator.data.trick.LineGeneratorDatabase
import com.example.line_generator.data.user.User
import com.example.line_generator.data.user.UserRepositoryImp
import com.example.line_generator.extensions.randomUUID
import com.example.line_generator.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//TODO ANLEGEN EINER ACTIVITY FÜR DIE USER AUSWAHL
//ABSPEICHERN DER USER ID IN SHAREDPREFERENCES
//WENN ES BEREITS EINE ID GIBT, KEINE USER AUSWAHL AM ANFANG MEHR
//WEITERGEBEN DER USER ID AN DAS STARTVIEWMODEL
//ERST DANN ERSTELLUNG DER TRICKS GEBUNDEN AN USER

class UserViewModel(application: Application) : AndroidViewModel(application), UserContract.ViewModel {

    private val userDao = LineGeneratorDatabase.getDatabase(application).userDao()
    private val userDatabase = UserRepositoryImp(userDao)
    private val userService = UserService(application)

//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            userDao.createUser(User(randomUUID(), "User One"))
//            userDao.createUser(User(randomUUID(), "User Two"))
//            userDao.createUser(User(randomUUID(), "User Three"))
//        }
//    }

    private val startActivityNavigationEvent = SingleLiveEvent<Unit>()
    private val allUsers = userDatabase.getAllUsers()
//    private val allUserViewState = Transformations.map(allUsers, ::mapListToViewState)
//
//    private fun mapListToViewState(allUsersList: List<User>): List<User>{
//        val listOfNames = mutableListOf<String>()
//        allUsersList.forEach {
//            listOfNames.add(it.name)
//        }
//        return listOfNames
//        return allUsersList
//            .map{mapItemToViewState(it)}
//    }
//
//    private fun mapItemToViewState(user: User): User{
//        return UserViewState(
//            name = user.name
//        )
//    }

    override fun getStartActivityNavigationEvent() = startActivityNavigationEvent

    override fun getAllUsers() = allUsers

    override fun createUser(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = randomUUID()
            userDatabase.createUser(User(userId, name))
            userService.saveUserId(userId)
            userService.saveUserName(name)
            startActivityNavigationEvent.call()
        }
    }

    override fun onUserSelected(selectedUser: User) {
        userService.saveUserId(selectedUser.id)
        userService.saveUserName(selectedUser.name)
        startActivityNavigationEvent.call()
    }
}