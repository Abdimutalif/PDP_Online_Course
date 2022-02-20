package com.projects.activities.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ModuleWithLesson(
    @Embedded val module: Module,
    @Relation(parentColumn = "id", entityColumn = "module_id", entity = Lesson::class)
    val lessons: List<Lesson>
)
