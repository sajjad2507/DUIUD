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
import com.example.duiudattender.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var dataSource: TeacherDataSource
    private lateinit var teacherAdapter: TeacherAdapter
    private var backPressedTime: Long = 0
    private val doubleBackToExitPressedMessage = "Press back again to exit"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        dataSource = TeacherDataSource(requireContext())
        dataSource.open()

        binding.teachersRcv.layoutManager =
            GridLayoutManager(requireContext(), 3)

        val teachers = dataSource.getAllTeachers()
        teacherAdapter = TeacherAdapter(
            requireContext(),
            "home",
            requireParentFragment(),
            teachers as MutableList<TeacherModel>
        )
        binding.teachersRcv.adapter = teacherAdapter

        binding.adminBtn.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("nPass", "0000")
            bundle.putString("check", "a")

            NavHostFragment.findNavController(this)
                .navigate(R.id.action_homeFragment_to_passwordCheck, bundle)

        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (backPressedTime + 2000 > System.currentTimeMillis()) {
                        requireActivity().finish()
                        return
                    } else {
                        Toast.makeText(
                            requireContext(),
                            doubleBackToExitPressedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    backPressedTime = System.currentTimeMillis()
                }
            })


        return binding.root
    }

    override fun onResume() {
        dataSource.open()
        super.onResume()
    }

    override fun onPause() {
        dataSource.close()
        super.onPause()
    }

}