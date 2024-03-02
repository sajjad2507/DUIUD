package com.example.duiudattender.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.example.duiudattender.R
import com.example.duiudattender.database.TeacherDataSource
import com.example.duiudattender.databinding.FragmentRemoveTeacherBinding

class RemoveTeacher : Fragment() {

    lateinit var binding: FragmentRemoveTeacherBinding
    private lateinit var dataSource: TeacherDataSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRemoveTeacherBinding.inflate(layoutInflater, container, false)

        val id = arguments?.getString("id")!!.toLong()
        val name = arguments?.getString("name")
        val adminPass = "0000"

        binding.removeTeacherBtn.setOnClickListener {

            dataSource = TeacherDataSource(requireContext())
            dataSource.open()

            if (binding.teacherNameEdt.text.toString() == name && binding.adminPasscodeEdt.text.toString() == adminPass) {

                try {

                    dataSource.deleteTeacher(id)

                    NavHostFragment.findNavController(this).navigate(R.id.action_removeTeacher_to_adminHome2)

                    dataSource.close()

                } catch (e: Exception) {

                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()

                }


            }

        }

        return binding.root
    }
}