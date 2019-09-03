package com.example.line_generator.data.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

// must perforn Queries in the worker thread, otherwise application will crash
/**
 * DAO maps Java method call to SQL query
 *
 * Using complex data types (z.B. Enums) -> you need Type Converters
 */

@Dao
interface UserDao {

    @Query ("SELECT * FROM user_data")
    fun getAllUsers(): LiveData<List<User>>

    @Query ("SELECT * FROM user_data WHERE id = :userId")
    fun getUser(userId: String): LiveData<User>

    @Query ("DELETE FROM user_data WHERE id = :userId")
    fun deleteUser(userId: String)

    @Insert (onConflict = REPLACE)
    fun createUser(userData: User)

    @Query ("DELETE from user_data")
    fun deleteAllUser()

}