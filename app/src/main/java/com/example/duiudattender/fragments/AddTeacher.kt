package com.example.duiudattender.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.duiudattender.R
import com.example.duiudattender.database.TeacherDataSource
import com.example.duiudattender.databinding.FragmentAddTeacherBinding

class AddTeacher : Fragment() {

    lateinit var binding: FragmentAddTeacherBinding
    private lateinit var dataSource: TeacherDataSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTeacherBinding.inflate(layoutInflater, container, false)
        dataSource = TeacherDataSource(requireContext())
        dataSource.open()

        binding.addTeacherBtn.setOnClickListener {

            val teacherName = binding.teacherNameEdt.text.toString()
            val passcode = binding.teacherPasscodeEdt.text.toString()
            dataSource.createTeacher(teacherName, passcode)

            dataSource.close()

            NavHostFragment.findNavController(this).navigate(R.id.action_addTeacher_to_adminHome2)

        }


        return binding.root
    }

}