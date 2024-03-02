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
import com.example.duiudattender.databinding.FragmentTeacherAttendanceBinding
import java.text.SimpleDateFormat
import java.util.*

class TeacherAttendance : Fragment() {

    lateinit var binding: FragmentTeacherAttendanceBinding
    var ch = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTeacherAttendanceBinding.inflate(layoutInflater, container, false)

        val id = arguments?.getString("id")!!.toLong()

        // Get the current system time
        val currentTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = sdf.format(currentTime)
        var formattedTimeOut = ""

        // Set the formatted time to 9:00 PM if the current time is later than 9:00 AM
        val calendar = Calendar.getInstance()
        calendar.time = sdf.parse(formattedTime)
        val am9 = Calendar.getInstance()
        am9.set(Calendar.HOUR_OF_DAY, 9)
        am9.set(Calendar.MINUTE, 0)
        am9.set(Calendar.SECOND, 0)
        if (calendar.time.after(am9.time)) {
            // The current time is later than 9:00 AM, so set it to 9:00 PM
            calendar.set(Calendar.HOUR_OF_DAY, 21)
            calendar.set(Calendar.MINUTE, 0)
            formattedTimeOut = sdf.format(calendar.time)
        }

        val currentDate = Date()
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = sdfDate.format(currentDate)

        binding.signInBtn.setOnClickListener {

            try {
                val dataSource = TeacherDataSource(requireContext())
                dataSource.open()

                var signInTime = ""
                var note = ""

                signInTime = formattedTime // Use the current time if it's before 9:00 AM
                note = binding.edtNotes.text.toString()

                // Determine the date suffix based on the time
                val dateSuffix = when {
                    formattedTime >= "09:00 AM" && formattedTime <= "12:30 PM" -> "M"
                    formattedTime > "12:30 PM" || formattedTime < "09:00 AM" -> "E"
                    else -> ""
                }

                val date = "$formattedDate$dateSuffix"
                val signOutTime = ""

                dataSource.createAttendanceForTeacher(id, signInTime, signOutTime, note, date)

                Toast.makeText(requireContext(), date, Toast.LENGTH_SHORT).show()

                dataSource.close()

                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_teacherAttendance_to_homeFragment)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error while inserting data", Toast.LENGTH_SHORT).show()
            }


        }

        binding.signOutBtn.setOnClickListener {

            try {

                val dataSource = TeacherDataSource(requireContext())

                dataSource.open()


                var signOutTime = formattedTime


                val dateSuffix = when {
                    formattedTime >= "09:00 AM" && formattedTime <= "12:30 PM" -> "M"
                    formattedTime > "12:30 PM" || formattedTime < "09:00 AM" -> "E"
                    else -> ""
                }
//                // Append 'M' or 'E' to the date based on the time
//                val dateSuffix = if (formattedTimeOut <= "09:00 AM" || formattedTimeOut >= "12:31 PM") "M" else "E"
                val date = "$formattedDate$dateSuffix"

                dataSource.updateAttendanceForTeacherAndDate(id, date, signOutTime)

                dataSource.close()

                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_teacherAttendance_to_homeFragment)

            } catch (e: Exception) {

                Toast.makeText(requireContext(), "Error while inserting data", Toast.LENGTH_SHORT)
                    .show()

            }

        }

        binding.addNote.setOnClickListener {

                binding.noteSection.visibility = View.VISIBLE
                binding.signInBtn.visibility = View.GONE
                binding.signOutBtn.visibility = View.GONE
                binding.addNote.visibility = View.GONE


        }

        binding.addNoteBtn.setOnClickListener {

            try {
                val dataSource = TeacherDataSource(requireContext())
                dataSource.open()

                val dateSuffix = when {
                    formattedTime >= "09:00 AM" && formattedTime <= "12:30 PM" -> "M"
                    formattedTime > "12:30 PM" || formattedTime < "09:00 AM" -> "E"
                    else -> ""
                }
//                // Append 'M' or 'E' to the date based on the time
//                val dateSuffix = if (formattedTimeOut <= "09:00 AM" || formattedTimeOut >= "12:31 PM") "M" else "E"
                val date = "$formattedDate$dateSuffix"

                dataSource.updateNoteForTeacherAndDate(id, date, binding.edtNotes.text.toString())

            } catch (e: Exception) {

                Toast.makeText(requireContext(), "Error while inserting data", Toast.LENGTH_SHORT)
                    .show()

            }

            binding.noteSection.visibility = View.GONE
            binding.signInBtn.visibility = View.VISIBLE
            binding.signOutBtn.visibility = View.VISIBLE
            binding.addNote.visibility = View.VISIBLE

        }

        return binding.root
    }
}