package com.projects.activities.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.projects.activities.adapters.ListLessonsAdapter
import com.projects.activities.database.AppDatabase
import com.projects.activities.entities.Lesson
import com.projects.activities.entities.Module
import com.projects.pdp2.R
import com.projects.pdp2.databinding.FragmentListLessonsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListLessonsFragment : Fragment(R.layout.fragment_list_lessons) {

    private val binding by viewBinding(FragmentListLessonsBinding::bind)
    private lateinit var appDatabase: AppDatabase
    private lateinit var listLessonsAdapter: ListLessonsAdapter
    private lateinit var module: Module

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        appDatabase = AppDatabase.getInstance(requireContext())
        val module_id = bundle?.getInt("id") as Int
        module = appDatabase.moduleDao().getModuleById(module_id)
        binding.name.text = module.name
        listLessonsAdapter = ListLessonsAdapter(object : ListLessonsAdapter.OnItemClick {
            override fun onItemClick(lesson: Lesson) {
                val bundle1 = Bundle()
                bundle1.putInt("id", lesson.id)
                findNavController().navigate(
                    R.id.action_listLessonsFragment_to_lessonFragment,
                    bundle1
                )
            }
        })
        binding.rv.adapter = listLessonsAdapter
        appDatabase.lessonDao()
            .getLessonsByModuleID(module_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listLessonsAdapter.submitList(it)
            }, {
                Log.d(ContentValues.TAG, "onCreate: $it")
            }) {
                Log.d(ContentValues.TAG, "onCreate: complate")
            }
    }
}