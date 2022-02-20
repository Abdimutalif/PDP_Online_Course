package com.projects.activities.dao

import androidx.room.*
import com.projects.activities.entities.ModuleWithLesson
import com.projects.activities.entities.Module
import io.reactivex.Flowable

@Dao
interface ModuleDao {
    @Insert
    fun addModule(module: Module)

    @Update
    fun editModule(module: Module)

    @Delete
    fun deleteModule(module: Module)

    @Query("select * from module where module.course_id = :course_id order by place")
    fun getAllModules(course_id: Int): Flowable<List<Module>>

    @Transaction
    @Query("select * from module where module.course_id = :course_id")
    fun getAllLessonWithModuleByCourseID(course_id: Int): Flowable<List<ModuleWithLesson>>

    @Query("select * from module where module.place = :placeNumber")
    fun isAvailable(placeNumber: Int): Module

    @Query("select * from module where module.id = :module_id")
    fun getModuleById(module_id: Int): Module


}