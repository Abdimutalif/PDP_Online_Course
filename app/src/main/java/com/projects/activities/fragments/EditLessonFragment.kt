package com.projects.activities.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.projects.activities.database.AppDatabase
import com.projects.activities.entities.Lesson
import com.projects.pdp2.R
import com.projects.pdp2.databinding.FragmentEditLessonBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class EditLessonFragment : Fragment(R.layout.fragment_edit_lesson) {
    private val binding by viewBinding(FragmentEditLessonBinding::bind)
    private lateinit var appDatabase: AppDatabase
    private lateinit var lesson: Lesson

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        val bundle = arguments
        val lesson_id = bundle?.getInt("id") as Int
        lesson = appDatabase.lessonDao().getLessonByID(lesson_id)
        binding.name.text = lesson.place.toString() + "-dars"
        binding.editLessonAbout.setText(lesson.info)
        binding.editLessonName.setText(lesson.name)
        binding.editLessonPlace.setText(lesson.place.toString())
        binding.addBtn.setOnClickListener {
            if (isValid()) {
                appDatabase.lessonDao().editLesson(lesson)
                Toast.makeText(requireContext(), "Edited!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun isValid(): Boolean {
        val name = binding.editLessonName.text.toString().trim()
        val about = binding.editLessonAbout.text.toString().trim()
        val place = binding.editLessonPlace.text.toString().trim()
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Nom kiritilmagan!", Toast.LENGTH_SHORT).show()
            return false
        } else if (about.isEmpty()) {
            Toast.makeText(requireContext(), "About kiritilmagan!", Toast.LENGTH_SHORT).show()
            return false
        } else if (place.isEmpty()) {
            Toast.makeText(requireContext(), "O'rin kiritilmagan!", Toast.LENGTH_SHORT).show()
            return false
        } else {
            lesson.name = name
            lesson.info = about
            lesson.place = place.toInt()
            return true
        }
    }
}