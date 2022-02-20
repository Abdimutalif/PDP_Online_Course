package com.projects.activities.dao

import androidx.room.*
import com.projects.activities.entities.Lesson
import io.reactivex.Flowable

@Dao
interface LessonDao {

    @Insert
    fun addLesson(lesson: Lesson)

    @Update
    fun editLesson(lesson: Lesson)

    @Delete
    fun deleteLesson(lesson: Lesson)

    @Query("select * from lesson where lesson.module_id = :module_id order by lesson.place")
    fun getLessonsByModuleID(module_id: Int): Flowable<List<Lesson>>

    @Query("select * from lesson where lesson.id = :lesson_id")
    fun getLessonByID(lesson_id: Int): Lesson

}