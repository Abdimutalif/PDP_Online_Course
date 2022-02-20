package com.projects.activities.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.projects.activities.adapters.ModuleAddAdapter
import com.projects.activities.database.AppDatabase
import com.projects.activities.entities.Course
import com.projects.activities.entities.Module
import com.projects.pdp2.R
import com.projects.pdp2.databinding.FragmentAddModuleBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddModuleFragment : Fragment(R.layout.fragment_add_module) {
    private val binding by viewBinding(FragmentAddModuleBinding::bind)
    private lateinit var course: Course
    private lateinit var appDatabase: AppDatabase
    private lateinit var adapter: ModuleAddAdapter
    private lateinit var module: Module

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        val bundle = arguments
        val course_id = bundle?.getInt("id") as Int
        course = appDatabase.courseDao().getCourseByID(course_id)
        binding.name.text = course.name
        adapter = ModuleAddAdapter(course.imagePath, object : ModuleAddAdapter.OnItemClick {
            override fun onItemEdit(module: Module) {
                val bundle1 = Bundle()
                bundle1.putInt("id", module.id)
                findNavController().navigate(
                    R.id.action_addModuleFragment_to_editModuleFragment,
                    bundle1
                )
            }

            override fun onItemDelete(module: Module) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(
                    "Bu modul ichida darslar kiritilgan.\nDarslar bilan birgalikda o’chib ketishiga rozimisiz?"
                )
                builder.setPositiveButton("Ha")
                { _, _ ->
                    appDatabase.moduleDao().deleteModule(module)
                }
                builder.setNegativeButton("Yo'q") { _, _ ->

                }
                builder.show()
            }

            override fun onItem(module: Module) {
                val bundle1 = Bundle()
                bundle1.putInt("id", module.id)
                findNavController().navigate(
                    R.id.action_addModuleFragment_to_addLessonFragment,
                    bundle1
                )
            }
        })
        binding.rv.adapter = adapter
        appDatabase.moduleDao()
            .getAllModules(course_id)
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
//                val module1 = appDatabase.moduleDao().isAvailable(module.place)
//                if (module1.name != "" || module1.name.isNotEmpty()) {
//                    Toast.makeText(requireContext(), "Bunaqa o'rin mavjud!", Toast.LENGTH_SHORT)
//                        .show()
//                } else {
                appDatabase.moduleDao().addModule(module)
                binding.editCourseName.setText("")
                binding.editCoursePlace.setText((module.place + 1).toString())
                Toast.makeText(requireContext(), "Added!", Toast.LENGTH_SHORT).show()

            }
        }


    }


    private fun isValid(): Boolean {
        val name = binding.editCourseName.text.toString()
        val place = binding.editCoursePlace.text.toString()
        if (place == "") {
            Toast.makeText(requireContext(), "O'rin kiritilmagan", Toast.LENGTH_SHORT).show()
            return false
        }
        return if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Nom xato kiritilgan", Toast.LENGTH_SHORT).show()
            false
        } else {
            val placeNumber = place.toInt()
            module = Module(courseId = course.id, name = name, place = placeNumber)
            true
        }
    }
}