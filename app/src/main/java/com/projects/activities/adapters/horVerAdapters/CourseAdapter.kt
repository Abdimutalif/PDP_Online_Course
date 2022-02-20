package com.projects.activities.adapters.horVerAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projects.activities.entities.CourseWithModule
import com.projects.activities.entities.Module
import com.projects.pdp2.databinding.ItemCourseBinding

class CourseAdapter(
    var onAllItemClickListener: OnAllItemClickListener,
    var onModuleItemClickListener: OnModuleItemClickListener
) : ListAdapter<CourseWithModule, CourseAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<CourseWithModule>() {
        override fun areItemsTheSame(
            oldItem: CourseWithModule,
            newItem: CourseWithModule
        ): Boolean {
            return oldItem.course.id == newItem.course.id
        }

        override fun areContentsTheSame(
            oldItem: CourseWithModule,
            newItem: CourseWithModule
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class Vh(var itemCourseBinding: ItemCourseBinding) :
        RecyclerView.ViewHolder(itemCourseBinding.root) {

        fun onBind(courseWithModule: CourseWithModule) {
            itemCourseBinding.apply {
                nameTv.text = courseWithModule.course.name
            }

            itemCourseBinding.allTv.setOnClickListener {
                onAllItemClickListener.onAllItemClick(courseWithModule.course.id)
            }

            val moduleAdapter = ModuleAdapter(courseWithModule.modules, onModuleItemClickListener)
            itemCourseBinding.moduleRv.adapter = moduleAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnAllItemClickListener {
        fun onAllItemClick(course_id: Int)
    }

    interface OnModuleItemClickListener {
        fun onItemModuleClick(module: Module)
    }

}