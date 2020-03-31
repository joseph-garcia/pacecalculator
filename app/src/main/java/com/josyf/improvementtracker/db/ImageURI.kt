package com.josyf.improvementtracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ImageURI(
    var imageAddress : String
): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}