package com.projects.activities.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projects.activities.entities.Module
import com.projects.pdp2.databinding.ItemAddModuleBinding

class ModuleAddAdapter(
    var imagePath: String,
    var onItemClick: OnItemClick
) :
    ListAdapter<Module, ModuleAddAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Module>() {
        override fun areItemsTheSame(
            oldItem: Module,
            newItem: Module
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Module,
            newItem: Module
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class Vh(var itemAddModuleBinding: ItemAddModuleBinding) :
        RecyclerView.ViewHolder(itemAddModuleBinding.root) {

        fun onBind(module: Module) {
            itemAddModuleBinding.apply {
                name.text = module.name
                img.setImageURI(Uri.parse(imagePath))
                itemAddModuleBinding.edit.setOnClickListener {
                    onItemClick.onItemEdit(module)
                }
                numberLessons.text = module.place.toString()
                itemAddModuleBinding.delete.setOnClickListener {
                    onItemClick.onItemDelete(module)
                }

                itemAddModuleBinding.layout.setOnClickListener {
                    onItemClick.onItem(module)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            ItemAddModuleBinding.inflate(
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
        fun onItemEdit(module: Module)

        fun onItemDelete(module: Module)

        fun onItem(module: Module)
    }

}