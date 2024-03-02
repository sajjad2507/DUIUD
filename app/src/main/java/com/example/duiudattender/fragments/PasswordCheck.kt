package com.example.duiudattender.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.example.duiudattender.R
import com.example.duiudattender.databinding.FragmentPasswordCheckBinding

class PasswordCheck : Fragment() {

    lateinit var binding: FragmentPasswordCheckBinding
    val enteredPassword = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPasswordCheckBinding.inflate(layoutInflater, container, false)
        val navHost = NavHostFragment.findNavController(this)
        val nTitle = arguments?.getString("nPass").toString()

        binding.zeroBtn.setOnClickListener {
            appendDigit("0")
        }

        binding.oneBtn.setOnClickListener {
            appendDigit("1")
        }

        binding.twoBtn.setOnClickListener {
            appendDigit("2")
        }

        binding.threeBtn.setOnClickListener {
            appendDigit("3")
        }

        binding.fourBtn.setOnClickListener {
            appendDigit("4")
        }

        binding.fiveBtn.setOnClickListener {
            appendDigit("5")
        }

        binding.sixBtn.setOnClickListener {
            appendDigit("6")
        }

        binding.sevenBtn.setOnClickListener {
            appendDigit("7")
        }

        binding.eightBtn.setOnClickListener {
            appendDigit("8")
        }

        binding.nineBtn.setOnClickListener {
            appendDigit("9")
        }

        binding.backBtn.setOnClickListener {

            navHost.navigate(R.id.action_passwordCheck_to_homeFragment)

        }

        return binding.root
    }

    fun appendDigit(digit: String) {
        if (enteredPassword.length < 4) {
            enteredPassword.append(digit)

            if (enteredPassword.length == 1) {
                binding.dotImg.setImageResource(R.drawable.onepass)
            } else if (enteredPassword.length == 2) {
                binding.dotImg.setImageResource(R.drawable.twopass)
            } else if (enteredPassword.length == 3) {
                binding.dotImg.setImageResource(R.drawable.threepass)
            } else if (enteredPassword.length == 4) {
                binding.dotImg.setImageResource(R.drawable.fourpass)
            }

            if (enteredPassword.length == 4) {
                checkPassword()
            }
        }
    }

    fun checkPassword() {
        val enteredPasswordStr = enteredPassword.toString()
        val nTitle = arguments?.getString("nPass").toString()
        val navHost = NavHostFragment.findNavController(this)

        if (enteredPasswordStr == nTitle) {
            val check = arguments?.getString("check").toString()

            if (check == "t") {

                binding.dotImg.setImageResource(R.drawable.alldot)

                val id = arguments?.getString("id").toString()
                val bundle = Bundle()
                bundle.putString("id", id)

                navHost.navigate(R.id.action_passwordCheck_to_teacherAttendance, bundle)

                enteredPassword.clear()

            } else if (check == "a") {

                navHost.navigate(R.id.action_passwordCheck_to_adminHome2)

            }

        } else {

            binding.dotImg.setImageResource(R.drawable.alldot)
            Toast.makeText(
                requireContext(),
                "Incorrect password, please try again",
                Toast.LENGTH_SHORT
            ).show()
            enteredPassword.clear()
        }
    }

}