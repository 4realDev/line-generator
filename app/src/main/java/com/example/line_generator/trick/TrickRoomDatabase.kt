package com.example.line_generator.trick

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.line_generator.trick.converter.*
import kotlinx.coroutines.CoroutineScope

/**
 * This is the backend. The database
 * TypeConverters are used, to convert complex type's for the database
 * In this case: enum classes
 */

@Database(entities = [Trick::class], version = 1)
@TypeConverters(
    TrickTypeConverter::class,
    CategoryConverter::class,
    DirectionInConverter::class,
    DirectionOutConverter::class,
    DifficultyConverter::class
)
abstract class TrickRoomDatabase : RoomDatabase() {

    abstract fun trickDao(): TrickDao

    companion object {
        @Volatile
        private var INSTANCE: TrickRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TrickRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrickRoomDatabase::class.java,
                    "trick_database"
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