package com.projects.activities.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projects.activities.entities.Course
import com.projects.pdp2.databinding.ItemCourseSettingBinding

class CourseSetAdapter(var onClickListener: OnItemClick) :
    ListAdapter<Course, CourseSetAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(
            oldItem: Course,
            newItem: Course
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Course,
            newItem: Course
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class Vh(var itemCourseSettingBinding: ItemCourseSettingBinding) :
        RecyclerView.ViewHolder(itemCourseSettingBinding.root) {

        fun onBind(course: Course) {
            itemCourseSettingBinding.apply {
                name.text = course.name
                img.setImageURI(Uri.parse(course.imagePath))
            }

            itemCourseSettingBinding.edit.setOnClickListener {
                onClickListener.onItemEditClick(course)
            }

            itemCourseSettingBinding.delete.setOnClickListener {
                onClickListener.onItemDeleteClick(course)
            }

            itemCourseSettingBinding.layout.setOnClickListener {
                onClickListener.onItemClick(course)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseSetAdapter.Vh {
        return Vh(
            ItemCourseSettingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    interface OnItemClick {
        fun onItemEditClick(course: Course)
        fun onItemDeleteClick(course: Course)
        fun onItemClick(course: Course)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

}