package com.example.duiudattender.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.duiudattender.database.TeacherAdapter
import com.example.duiudattender.database.TeacherDataSource
import com.example.duiudattender.database.TeacherModel
import com.example.duiudattender.databinding.FragmentRemoveTeacherListBinding

class RemoveTeacherList : Fragment() {

    lateinit var binding: FragmentRemoveTeacherListBinding
    private lateinit var dataSource: TeacherDataSource
    private lateinit var teacherAdapter: TeacherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRemoveTeacherListBinding.inflate(layoutInflater, container, false)

        dataSource = TeacherDataSource(requireContext())
        dataSource.open()

        binding.teachersRcv.layoutManager =
            GridLayoutManager(requireContext(), 3) // Display 2 items in one row
        val teachers = dataSource.getAllTeachers()
        teacherAdapter = TeacherAdapter(
            requireContext(),
            "remove",
            requireParentFragment(),
            teachers as MutableList<TeacherModel>
        )
        binding.teachersRcv.adapter = teacherAdapter

        dataSource.close()

        return binding.root
    }

}