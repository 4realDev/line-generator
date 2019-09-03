package com.example.line_generator.data.user

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class UserRepositoryImp(private val userDao: UserDao) : UserRepository {

    private val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    override fun getAllUsers(): LiveData<List<User>> = allUsers

    override fun getUser(userId: String): LiveData<User> = userDao.getUser(userId)

    @Suppress
    @WorkerThread
    override suspend fun createUser(user: User) {
        userDao.createUser(user)
    }

    @Suppress
    @WorkerThread
    override suspend fun deleteUser(id: String) {
        userDao.deleteUser(id)
    }

    @Suppress
    @WorkerThread
    override suspend fun deleteAllUser() {
        userDao.deleteAllUser()
    }
}