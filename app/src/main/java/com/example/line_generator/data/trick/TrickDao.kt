package com.example.line_generator.data.trick

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
interface TrickDao {

    @Query ("SELECT * from trick_data WHERE user_id = :userId")
    fun getAll(userId: String): LiveData<List<Trick>>

    @Query ("UPDATE trick_data SET selected = :selected WHERE id = :id")
    fun update(selected: Boolean, id: String)

    @Query ("UPDATE trick_data SET user_created_name = :changedName, direction_in = :changedDirectionIn, direction_out = :changedDirectionOut, difficulty = :changedDifficulty WHERE id = :id")
    fun updateAfterEdit(id: String, changedName: String, changedDirectionIn: DirectionIn, changedDirectionOut: DirectionOut, changedDifficulty: Difficulty)

    @Query ("DELETE FROM trick_data WHERE id = :id")
    fun delete(id: String)

    @Insert (onConflict = REPLACE)
    fun insert(trickData: Trick)

    @Query ("DELETE from trick_data")
    fun deleteAll()

}