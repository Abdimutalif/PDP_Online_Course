package com.projects.activities.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Module::class,
            parentColumns = ["id"],
            childColumns = ["module_id"],
            onDelete = CASCADE
        )]
)
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var info: String,
    var place: Int,
    @ColumnInfo(name = "module_id")
    var moduleId: Int
) : Serializable
