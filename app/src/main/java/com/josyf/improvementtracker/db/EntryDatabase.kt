package com.josyf.improvementtracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//Here is where we initialize the DB class and reference the entity class in an array
@Database(
    entities = [Entry::class],
    version = 1
)

// make it abstract since this is not able to be initialized
abstract class EntryDatabase : RoomDatabase() {
    abstract fun entryDao() : EntryDAO


    companion object {
        //create an instance of our DB and volatize it to guarantee the instance is coming from main thread and not cache
        @Volatile
        private var instance: RoomDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            EntryDatabase::class.java,
            "entrydatabase"
        ).build()

    }
}