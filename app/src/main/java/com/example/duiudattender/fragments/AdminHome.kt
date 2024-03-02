package com.example.duiudattender.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.duiudattender.R
import com.example.duiudattender.database.TeacherAdapter
import com.example.duiudattender.database.TeacherDataSource
import com.example.duiudattender.database.TeacherModel
import com.example.duiudattender.databinding.FragmentAdminHomeBinding

class AdminHome : Fragment() {

    lateinit var binding: FragmentAdminHomeBinding
    private lateinit var dataSource: TeacherDataSource
    private lateinit var teacherAdapter: TeacherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAdminHomeBinding.inflate(layoutInflater, container, false)

        dataSource = TeacherDataSource(requireContext())
        dataSource.open()

        val navHost = NavHostFragment.findNavController(this)

        binding.teachersRcv.layoutManager =
            GridLayoutManager(requireContext(), 3)
        val teachers = dataSource.getAllTeachers()
        teacherAdapter = TeacherAdapter(
            requireContext(),
            "admin",
            requireParentFragment(),
            teachers as MutableList<TeacherModel>
        )
        binding.teachersRcv.adapter = teacherAdapter

        binding.addBtn.setOnClickListener {

            navHost.navigate(R.id.action_adminHome2_to_addTeacher)

        }

        binding.removeBtn.setOnClickListener {

            navHost.navigate(R.id.action_adminHome2_to_removeTeacherList22)

        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    navHost.navigate(R.id.action_adminHome2_to_homeFragment)

                }
            })

        return binding.root
    }
}