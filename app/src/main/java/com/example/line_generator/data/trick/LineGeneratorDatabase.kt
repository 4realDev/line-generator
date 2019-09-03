package com.example.line_generator.data.trick

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.line_generator.data.trick.converter.*
import com.example.line_generator.data.user.User
import com.example.line_generator.data.user.UserDao

/**
 * This is the backend. The database
 * TypeConverters are used, to convert complex type's for the database
 * In this case: enum classes
 */

@Database(entities = [Trick::class, User::class], version = 1)
@TypeConverters(
    TrickTypeConverter::class,
    CategoryConverter::class,
    DirectionInConverter::class,
    DirectionOutConverter::class,
    DifficultyConverter::class
)
abstract class LineGeneratorDatabase : RoomDatabase() {

    abstract fun trickDao(): TrickDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: LineGeneratorDatabase? = null

        fun getDatabase(context: Context): LineGeneratorDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LineGeneratorDatabase::class.java,
                    "line_generator_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}