package com.josyf.improvementtracker.db

import androidx.room.*

@Dao
interface ImageURI_DAO{
    @Insert
    suspend fun addEntry(imageURI: ImageURI)

    @Query("SELECT * FROM ImageURI ORDER BY id DESC")
    suspend fun getAllEntries() : List<ImageURI>

    @Insert
    suspend fun addMultipleEntries(vararg imageURI: ImageURI)

    @Delete
    suspend fun deleteEntry(imageURI: ImageURI)

    @Update
    suspend fun updateEntry(imageURI: ImageURI)

    //get length
}