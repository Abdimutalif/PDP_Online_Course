package com.projects.activities.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.projects.activities.database.AppDatabase
import com.projects.activities.entities.Lesson
import com.projects.pdp2.R
import com.projects.pdp2.databinding.FragmentLessonBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class LessonFragment : Fragment(R.layout.fragment_lesson) {
    private val binding by viewBinding(FragmentLessonBinding::bind)
    private lateinit var lesson: Lesson
    private lateinit var appDatabase: AppDatabase
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        appDatabase = AppDatabase.getInstance(requireContext())
        val lesson_id = bundle?.getInt("id") as Int
        lesson = appDatabase.lessonDao().getLessonByID(lesson_id)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.about.text = lesson.info

        binding.placeNumber.text = lesson.place.toString() + "-dars"
        binding.name.text = lesson.name
    }
}