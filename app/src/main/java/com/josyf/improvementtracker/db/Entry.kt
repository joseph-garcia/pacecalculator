package com.josyf.improvementtracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//This is a data class.
//This is also our entity, denoted by @Entity

// An entity is a set of related fields. For each entity, a table is
// created within the associated Database object to hold the items.

//In other words, here is where we define the schema for our entries.

//Reference this entity class through the entities array in the DB class.

@Entity
data class Entry(
    val timeString: String,
    val distanceString: String,
    val paceString: String,
    val dateString: String,
    val adjustedTime: String,
    val adjustedTimeInSeconds: Int,
    var timeDifference: String,
    val image: String
):Serializable{
    // Serializing encodes the data in a way that we can store it and deserialize it later
    // Let's make an id property and let it be our primarykey. Autogenerate lets SQLite generate the unique id
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}