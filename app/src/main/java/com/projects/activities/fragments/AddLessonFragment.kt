package com.projects.activities.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.projects.activities.adapters.LessonAddAdapter
import com.projects.activities.database.AppDatabase
import com.projects.activities.entities.Lesson
import com.projects.activities.entities.Module
import com.projects.pdp2.R
import com.projects.pdp2.databinding.FragmentAddLessonBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddLessonFragment : Fragment(R.layout.fragment_add_lesson) {

    private val binding by viewBinding(FragmentAddLessonBinding::bind)
    private lateinit var appDatabase: AppDatabase
    private lateinit var adapter: LessonAddAdapter
    private lateinit var lesson: Lesson
    private lateinit var module: Module

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        val bundle = arguments
        val module_id = bundle?.getInt("id") as Int
        module = appDatabase.moduleDao().getModuleById(module_id)
        binding.name.text = module.name
        val course_id = module.courseId
        val course = appDatabase.courseDao().getCourseByID(course_id)
        adapter = LessonAddAdapter(course.imagePath, object : LessonAddAdapter.OnItemClick {
            override fun onItemEdit(lesson: Lesson) {
                val bundle1 = Bundle()
                bundle1.putInt("id", lesson.id)
                findNavController().navigate(
                    R.id.action_addLessonFragment_to_editLessonFragment,
                    bundle1
                )
            }

            override fun onItemDelete(lesson: Lesson) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(
                    "Dars o’chishiga rozimisiz?"
                )
                builder.setPositiveButton("Ha")
                { _, _ ->
                    appDatabase.lessonDao().deleteLesson(lesson)
                }
                builder.setNegativeButton("Yo'q") { _, _ ->

                }
                builder.show()
            }
        })

        binding.rv.adapter = adapter
        appDatabase.lessonDao()
            .getLessonsByModuleID(module_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter.submitList(it)
            }, {
                Log.d(ContentValues.TAG, "onCreate: $it")
            }) {
                Log.d(ContentValues.TAG, "onCreate: complate")
            }

        binding.addBtn.setOnClickListener {
            if (isValid()) {
                appDatabase.lessonDao().addLesson(lesson)
                Toast.makeText(requireContext(), "Added!", Toast.LENGTH_SHORT).show()
                binding.editLessonAbout.setText("")
                binding.editLessonPlace.setText("")
                binding.editLessonName.setText("")
            }
        }

    }

    private fun isValid(): Boolean {
        val name = binding.editLessonName.text.toString()
        val place = binding.editLessonPlace.text.toString()
        val about = binding.editLessonAbout.text.toString()
        if (place.isEmpty()) {
            Toast.makeText(requireContext(), "O'rin kiritilmagan", Toast.LENGTH_SHORT).show()
            return false
        } else
            if (about.isEmpty()) {
                Toast.makeText(requireContext(), "About kiritilmagan", Toast.LENGTH_SHORT).show()
                return false
            }
        return if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Nom xato kiritilgan", Toast.LENGTH_SHORT).show()
            false
        } else {
            lesson = Lesson(name = name, place = place.toInt(), info = about, moduleId = module.id)
            true
        }
    }

}