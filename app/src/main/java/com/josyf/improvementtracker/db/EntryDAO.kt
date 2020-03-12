package com.josyf.improvementtracker.db

import androidx.room.*

@Dao
interface EntryDAO{
    @Insert
    suspend fun addEntry(entry: Entry)

    @Insert
    suspend fun addMultipleEntries(vararg entry: Entry)

    @Query("SELECT * FROM entry ORDER BY id DESC")
    suspend fun getAllEntries() : List<Entry>


    @Delete
    suspend fun deleteEntry(entry: Entry)

    @Update
    suspend fun updateEntry(entry: Entry)

    //get length
}