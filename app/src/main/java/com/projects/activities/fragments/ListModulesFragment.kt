package com.projects.activities.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.projects.activities.adapters.ListModulesAdapter
import com.projects.activities.database.AppDatabase
import com.projects.activities.entities.ModuleWithLesson
import com.projects.pdp2.R
import com.projects.pdp2.databinding.FragmentListModulesBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListModulesFragment : Fragment(R.layout.fragment_list_modules) {
    private val binding by viewBinding(FragmentListModulesBinding::bind)
    private lateinit var appDatabase: AppDatabase
    private lateinit var listModulesAdapter: ListModulesAdapter

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        val course_id = arguments?.getInt("id") as Int
        val course = appDatabase.courseDao().getCourseByID(course_id)
        listModulesAdapter =
            ListModulesAdapter(
                course.name,
                course.imagePath,
                object : ListModulesAdapter.OnItemClick {
                    override fun onItemClick(moduleWithLesson: ModuleWithLesson) {
                        val bundle = Bundle()
                        bundle.putInt("id", moduleWithLesson.module.id)
                        findNavController().navigate(
                            R.id.action_listModulesFragment_to_listLessonsFragment,
                            bundle
                        )
                    }
                })

        binding.rv.adapter = listModulesAdapter
        appDatabase.moduleDao()
            .getAllLessonWithModuleByCourseID(course_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listModulesAdapter.submitList(it)
            }, {
                Log.d(ContentValues.TAG, "onCreate: $it")
            }) {
                Log.d(ContentValues.TAG, "onCreate: complate")
            }
    }
}