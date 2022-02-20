package com.projects.activities.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projects.activities.entities.Lesson
import com.projects.pdp2.databinding.ItemAddLessonBinding

class LessonAddAdapter(
    var imagePath: String,
    var onItemClick: OnItemClick
) :
    ListAdapter<Lesson, LessonAddAdapter.Vh>(MyDiffUtil()) {

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

    inner class Vh(var itemAddLessonBinding: ItemAddLessonBinding) :
        RecyclerView.ViewHolder(itemAddLessonBinding.root) {

        fun onBind(lesson: Lesson) {
            itemAddLessonBinding.apply {
                name.text = lesson.name
                place.text = lesson.place.toString() + "-dars"
                img.setImageURI(Uri.parse(imagePath))

                itemAddLessonBinding.edit.setOnClickListener {
                    onItemClick.onItemEdit(lesson)
                }
                itemAddLessonBinding.delete.setOnClickListener {
                    onItemClick.onItemDelete(lesson)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            ItemAddLessonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnItemClick {
        fun onItemEdit(lesson: Lesson)

        fun onItemDelete(lesson: Lesson)

    }

}