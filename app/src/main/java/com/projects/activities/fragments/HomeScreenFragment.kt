package com.projects.activities.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.projects.activities.adapters.horVerAdapters.CourseAdapter
import com.projects.activities.database.AppDatabase
import com.projects.activities.entities.Module
import com.projects.pdp2.R
import com.projects.pdp2.databinding.FragmentHomeScreenBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private val binding by viewBinding(FragmentHomeScreenBinding::bind)
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var appDatabase: AppDatabase

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        courseAdapter = CourseAdapter(object : CourseAdapter.OnAllItemClickListener {
            override fun onAllItemClick(course_id: Int) {
                val bundle = Bundle()
                bundle.putInt("id", course_id)
                findNavController().navigate(
                    R.id.action_homeScreenFragment_to_listModulesFragment,
                    bundle
                )
            }
        }, object : CourseAdapter.OnModuleItemClickListener {
            override fun onItemModuleClick(module: Module) {
                val bundle = Bundle()
                bundle.putInt("id", module.id)
                findNavController().navigate(
                    R.id.action_homeScreenFragment_to_listLessonsFragment,
                    bundle
                )
            }
        })

        binding.floatingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_settingsFragment)
        }

        binding.rv.adapter = courseAdapter
        appDatabase.courseDao()
            .getModuleWithCourse()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                courseAdapter.submitList(it)
            }, {
                Log.d(TAG, "onCreate: $it")
            }) {
                Log.d(TAG, "onCreate: complate")
            }
    }

}