package com.projects.activities.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.projects.activities.database.AppDatabase
import com.projects.activities.entities.Course
import com.projects.pdp2.R
import com.projects.pdp2.databinding.FragmentEditCourseBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.io.File
import java.io.FileOutputStream

class EditCourseFragment : Fragment(R.layout.fragment_edit_course) {
    private lateinit var fileAbsolutePath: String
    private val binding by viewBinding(FragmentEditCourseBinding::bind)
    private lateinit var appDatabase: AppDatabase
    private lateinit var course: Course
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val id = bundle?.getInt("id") as Int
        appDatabase = AppDatabase.getInstance(requireContext())
        course = appDatabase.courseDao().getCourseByID(id)
        fileAbsolutePath = course.imagePath

        binding.apply {
            placeholder.setImageURI(Uri.parse(course.imagePath))
            editCourseName.setText(course.name)
        }

        binding.placeholder.setOnClickListener {
            requestPermission()
        }

        binding.editBtn.setOnClickListener {
            if (isValid()) {
                appDatabase.courseDao().editCourse(course)
                Toast.makeText(requireContext(), "O'zgartirildi!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }

        }
    }

    private fun isValid(): Boolean {
        val name = binding.editCourseName.text.toString().trim()
        return if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Nom xato kiritilgan!", Toast.LENGTH_SHORT).show()
            false
        } else {
            course.name = name
            course.imagePath = fileAbsolutePath
            true
        }
    }

    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.placeholder.setImageURI(it)
        val openInputStream = activity?.contentResolver?.openInputStream(it)
        val m = System.currentTimeMillis()
        val file = File(activity?.filesDir, "$m.jpg")
        val fileOutputStream = FileOutputStream(file)
        openInputStream?.copyTo(fileOutputStream)
        openInputStream?.close()
        fileOutputStream.close()
        fileAbsolutePath = file.absolutePath
        Toast.makeText(requireContext(), file.absolutePath, Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission() {
        Dexter.withContext(requireContext())
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    getImageContent.launch("image/*")
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri =
                            Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } else {
                        response.requestedPermission
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: com.karumi.dexter.listener.PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Gallery permission!")
                    builder.setMessage("Galleriyadan foydalanish uchun ruxsat bering!")
                    builder.setPositiveButton("Ruxsat so'rash!")
                    { _, _ ->
                        p1?.continuePermissionRequest()
                    }
                    builder.setNegativeButton("Ruxsat so'ramaslik!") { _, _ -> p1?.cancelPermissionRequest() }
                    builder.show()

                }

            }).check()
    }
}