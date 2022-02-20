package com.projects.activities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projects.activities.entities.Lesson
import com.projects.pdp2.databinding.ItemLessonListBinding

class ListLessonsAdapter(var onClickListener: OnItemClick) :
    ListAdapter<Lesson, ListLessonsAdapter.Vh>(MyDiffUtil()) {
    class MyDiffUtil : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(
            oldItem: Lesson,
            newItem: Lesson
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Lesson,
            newItem: Lesson
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class Vh(var itemLessonListBinding: ItemLessonListBinding) :
        RecyclerView.ViewHolder(itemLessonListBinding.root) {

        fun onBind(lesson: Lesson) {
            itemLessonListBinding.apply {
                placeNumber.text = lesson.place.toString()
                layout.setOnClickListener {
                    onClickListener.onItemClick(lesson)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListLessonsAdapter.Vh {
        return Vh(
            ItemLessonListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    interface OnItemClick {
        fun onItemClick(lesson: Lesson)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }
}