package com.projects.activities.adapters.horVerAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projects.activities.entities.Module
import com.projects.pdp2.databinding.ItemModuleBinding

class ModuleAdapter(
    var list: List<Module>,
    var onModuleItemClickListener: CourseAdapter.OnModuleItemClickListener
) : RecyclerView.Adapter<ModuleAdapter.Vh>() {

    inner class Vh(var moduleBinding: ItemModuleBinding) :
        RecyclerView.ViewHolder(moduleBinding.root) {

        fun onBind(module: Module) {
            moduleBinding.moduleName.text = module.name

            moduleBinding.moduleName.setOnClickListener {
                onModuleItemClickListener.onItemModuleClick(module)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemModuleBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
