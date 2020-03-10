package com.josyf.improvementtracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Here is where we initialize the DB class and reference the entity class in an array
@Database(
    entities = [ImageURI::class],
    version = 1
)
abstract class ImageURIDatabase : RoomDatabase(){
    abstract fun ImageDAO() : ImageURI_DAO

    companion object {
        @Volatile private var instance : ImageURIDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ImageURIDatabase::class.java,
            "imagedatabase"
        ).build()
    }
}