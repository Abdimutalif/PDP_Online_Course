package com.projects.activities.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.projects.activities.dao.CourseDao
import com.projects.activities.dao.LessonDao
import com.projects.activities.dao.ModuleDao
import com.projects.activities.entities.Course
import com.projects.activities.entities.Lesson
import com.projects.activities.entities.Module

@Database(
    entities = [Course::class, Module::class, Lesson::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun lessonDao(): LessonDao
    abstract fun moduleDao(): ModuleDao

    companion object {
        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
                    .allowMainThreadQueries()
                    .build()
            }
            return appDatabase!!
        }
    }
}
