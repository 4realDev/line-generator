package com.example.line_generator.trick

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.line_generator.extensions.randomUUID
import com.example.line_generator.trick.converter.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        // TrickRoomDatabase gets a function as parameter
        // function onOpen has not parameters and returns Unit -> our Callback
        fun getDatabase(context: Context, scope: CoroutineScope, onOpen : () -> Unit): TrickRoomDatabase {
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
                    .addCallback(TrickDatabaseCallback(scope, onOpen))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        /*_________________________RESTART CODE__________________________*/

        private class TrickDatabaseCallback(
            private val scope: CoroutineScope,
            private val onOpen: () -> Unit
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.trickDao())
                        withContext(Dispatchers.Main){ onOpen() }
                    }
                }

            }
        }

        /**
         * Populate the database in a new coroutine.
         */
        fun populateDatabase(trickDao: TrickDao) {
            trickDao.insert(Trick(randomUUID(), 0, TrickType.AXLE_STALL, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.EASY, null, true, true))
            trickDao.insert(Trick(randomUUID(), 1, TrickType.FIVE_O_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.MIDDLE, null, true, true))
            trickDao.insert(Trick(randomUUID(), 2, TrickType.FEEBLE_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.MIDDLE, null, true, true))
            trickDao.insert(Trick(randomUUID(), 3, TrickType.SMITH_GRIND, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.MIDDLE, null, false, true))
            trickDao.insert(Trick(randomUUID(), 4, TrickType.CROOKED_GRIND, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.GRIND, Difficulty.HARD, null, false, true))
            trickDao.insert(Trick(randomUUID(), 5, TrickType.PIVOT, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.GRIND, Difficulty.SAVE, null, false, true))
            trickDao.insert(Trick(randomUUID(), 6, TrickType.NOSE_GRIND, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.GRIND, Difficulty.HARD, null, false, true))

            trickDao.insert(Trick(randomUUID(), 8, TrickType.TAIL_STALL, DirectionIn.FAKIE, DirectionOut.TO_REGULAR, Category.SLIDE, Difficulty.SAVE, null, true, true))
            trickDao.insert(Trick(randomUUID(), 9, TrickType.NOSE_STALL, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.SLIDE, Difficulty.MIDDLE, null, true, true))
            trickDao.insert(Trick(randomUUID(), 10, TrickType.ROCK_TO_FAKIE, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.SLIDE, Difficulty.SAVE, null, true, true))
            trickDao.insert(Trick(randomUUID(), 11, TrickType.ROCK_N_ROLL, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.SLIDE, Difficulty.EASY, null, false, true))
            trickDao.insert(Trick(randomUUID(), 12, TrickType.HALFCAB_ROCK, DirectionIn.FAKIE, DirectionOut.TO_FAKIE, Category.SLIDE, Difficulty.EASY, null, false, true))
            trickDao.insert(Trick(randomUUID(), 13, TrickType.FULLCAB_ROCK, DirectionIn.FAKIE, DirectionOut.TO_REGULAR, Category.SLIDE, Difficulty.MIDDLE, null, false, true))

            trickDao.insert(Trick(randomUUID(), 14, TrickType.BONELESS, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.OTHER, Difficulty.MIDDLE, null, true, true))
            trickDao.insert(Trick(randomUUID(), 15, TrickType.NO_COMPLY, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.OTHER, Difficulty.HARD, null, true, true))
            trickDao.insert(Trick(randomUUID(), 16, TrickType.DISTASTER, DirectionIn.REGULAR, DirectionOut.TO_REGULAR, Category.OTHER, Difficulty.CRAZY, null, true, true))
            trickDao.insert(Trick(randomUUID(), 17, TrickType.OLLIE, DirectionIn.REGULAR, DirectionOut.TO_FAKIE, Category.OTHER, Difficulty.CRAZY, null, false, true))
        }
    }
}