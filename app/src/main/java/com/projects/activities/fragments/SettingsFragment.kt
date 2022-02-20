package com.projects.activities.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.projects.activities.adapters.CourseSetAdapter
import com.projects.activities.database.AppDatabase
import com.projects.activities.entities.Course
import com.projects.pdp2.R
import com.projects.pdp2.databinding.FragmentSettingsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var fileAbsolutePath: String = ""
    private lateinit var course: Course
    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private lateinit var appDatabase: AppDatabase
    private lateinit var adapter: CourseSetAdapter

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        binding.placeholder.setOnClickListener {
            requestPermission()
        }
        adapter = CourseSetAdapter(object : CourseSetAdapter.OnItemClick {
            override fun onItemEditClick(course: Course) {
                val bundle = Bundle()
                bundle.putInt("id", course.id)
                findNavController().navigate(
                    R.id.action_settingsFragment_to_editCourseFragment,
                    bundle
                )
            }

            override fun onItemDeleteClick(course: Course) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(
                    "Bu kurs ichida modullar kiritilgan. \nModullar bilan birgalikda o’chib ketishiga rozimisiz?"
                )
                builder.setPositiveButton("Ha")
                { _, _ ->
                    appDatabase.courseDao().deleteCourse(course)
                }
                builder.setNegativeButton("Yo'q") { _, _ ->

                }
                builder.show()
            }

            override fun onItemClick(course: Course) {
                val bundle = Bundle()
                bundle.putInt("id", course.id)
                findNavController().navigate(
                    R.id.action_settingsFragment_to_addModuleFragment,
                    bundle
                )
            }
        })
        binding.rv.adapter = adapter
        appDatabase.courseDao()
            .getAllCourses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter.submitList(it)
            }, {
                Log.d(ContentValues.TAG, "onCreate: $it")
            }) {
                Log.d(ContentValues.TAG, "onCreate: complate")
            }

        binding.floatingBtn.setOnClickListener {
            if (isValid()) {
                appDatabase.courseDao().addCourse(course)
                Toast.makeText(requireContext(), "Added!", Toast.LENGTH_SHORT).show()
                binding.placeholder.setImageResource(R.drawable.placeholder)
                binding.editCourseName.setText("")
            }
        }

    }

    private fun isValid(): Boolean {
        val name = binding.editCourseName.text.toString().trim()
        return when {
            fileAbsolutePath == "" -> {
                Toast.makeText(requireContext(), "Rasm kiritilmagan!", Toast.LENGTH_SHORT).show()
                false
            }
            (name == "") || (name.isEmpty()) -> {
                Toast.makeText(requireContext(), "Nom xato kiritilgan!", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                course = Course(name = name, imagePath = fileAbsolutePath)
                true
            }
        }
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

    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            binding.placeholder.setImageURI(it)
            val openInputStream = activity?.contentResolver?.openInputStream(it)
            val m = System.currentTimeMillis()
            val file = File(activity?.filesDir, "$m.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()
            fileAbsolutePath = file.absolutePath
        }
    }

}