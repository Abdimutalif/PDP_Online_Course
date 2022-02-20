package com.projects.activities.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithModule(
    @Embedded val course: Course,
    @Relation(parentColumn = "id", entityColumn = "course_id", entity = Module::class)
    val modules: List<Module>
)