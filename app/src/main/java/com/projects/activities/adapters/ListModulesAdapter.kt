package com.projects.activities.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projects.activities.entities.Course
import com.projects.activities.entities.ModuleWithLesson
import com.projects.pdp2.databinding.ItemCourseSettingBinding
import com.projects.pdp2.databinding.ItemModuleListBinding

class ListModulesAdapter(
    var course_name: String,
    var imagePath: String,
    var onClickListener: OnItemClick
) :
    ListAdapter<ModuleWithLesson, ListModulesAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<ModuleWithLesson>() {
        override fun areItemsTheSame(
            oldItem: ModuleWithLesson,
            newItem: ModuleWithLesson
        ): Boolean {
            return oldItem.module.id == newItem.module.id
        }

        override fun areContentsTheSame(
            oldItem: ModuleWithLesson,
            newItem: ModuleWithLesson
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class Vh(var itemModuleListBinding: ItemModuleListBinding) :
        RecyclerView.ViewHolder(itemModuleListBinding.root) {

        fun onBind(moduleWithLesson: ModuleWithLesson) {
            itemModuleListBinding.apply {
                name.text = moduleWithLesson.module.name
                courseFade.text = course_name
                img.setImageURI(Uri.parse(imagePath))
                numberLessons.text = moduleWithLesson.lessons.size.toString()
                layout.setOnClickListener {
                    onClickListener.onItemClick(moduleWithLesson)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListModulesAdapter.Vh {
        return Vh(
            ItemModuleListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    interface OnItemClick {
        fun onItemClick(moduleWithLesson: ModuleWithLesson)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }
}