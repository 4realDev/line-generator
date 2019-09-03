package com.example.line_generator.data.user

import androidx.lifecycle.LiveData

interface UserRepository {

    fun getAllUsers(): LiveData<List<User>>

    fun getUser(userId: String): LiveData<User>

    suspend fun deleteUser(id: String)

    suspend fun createUser(user: User)

    suspend fun deleteAllUser()
}