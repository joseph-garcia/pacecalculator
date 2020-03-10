package com.josyf.improvementtracker.db

import android.net.Uri
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
data class ImageURI(
    val imageAddress : String
): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}