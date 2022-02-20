package com.projects.activities.dao

import androidx.room.*
import com.projects.activities.entities.Course
import com.projects.activities.entities.CourseWithModule
import io.reactivex.Flowable

@Dao
interface CourseDao {

    @Insert
    fun addCourse(course: Course)

    @Query("select * from course")
    fun getAllCourses(): Flowable<List<Course>>

    @Update
    fun editCourse(course: Course)

    @Delete
    fun deleteCourse(course: Course)

    @Transaction
    @Query("select * from course")
    fun getModuleWithCourse(): Flowable<List<CourseWithModule>>

    @Query("select * from course where course.id = :course_id")
    fun getCourseByID(course_id: Int): Course

    @Query("select * from course where course.id = :course_id")
    fun getCourseWithModulesById(course_id: Int): Flowable<List<CourseWithModule>>


}