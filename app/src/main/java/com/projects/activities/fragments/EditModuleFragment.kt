package com.projects.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.projects.activities.database.AppDatabase
import com.projects.activities.entities.Module
import com.projects.pdp2.R
import com.projects.pdp2.databinding.FragmentEditModuleBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class EditModuleFragment : Fragment(R.layout.fragment_edit_module) {
    private val binding by viewBinding(FragmentEditModuleBinding::bind)
    private lateinit var appDatabase: AppDatabase
    private lateinit var module: Module
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val module_id = bundle?.getInt("id") as Int
        appDatabase = AppDatabase.getInstance(requireContext())
        module = appDatabase.moduleDao().getModuleById(module_id)
        binding.name.text = module.name
        binding.editCourseName.setText(module.name)
        binding.editCoursePlace.setText(module.place.toString())
        binding.editBtn.setOnClickListener {
            if (isValid()) {
//                val module1 = appDatabase.moduleDao().isAvailable(module.place)
//                if (module1.name != "" || module1.name.isNotEmpty()) {
//                    Toast.makeText(requireContext(), "Bunaqa o'rin mavjud!", Toast.LENGTH_SHORT)
//                        .show()
//                } else {
                appDatabase.moduleDao().editModule(module)
                binding.editCourseName.setText("")
                binding.editCoursePlace.setText("")
                Toast.makeText(requireContext(), "Edited!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()

            }
        }

    }

    private fun isValid(): Boolean {
        val name = binding.editCourseName.text.toString()
        val place = binding.editCoursePlace.text.toString()
        if (place == "") {
            Toast.makeText(requireContext(), "O'rin kiritilmagan", Toast.LENGTH_SHORT).show()
            return false
        }
        return if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Nom xato kiritilgan", Toast.LENGTH_SHORT).show()
            false
        } else {
            val placeNumber = place.toInt()
            module.place = placeNumber
            module.name = name
            true
        }
    }
}