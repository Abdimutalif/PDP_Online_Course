package com.projects.activities.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Course(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var imagePath: String
) : Serializable
