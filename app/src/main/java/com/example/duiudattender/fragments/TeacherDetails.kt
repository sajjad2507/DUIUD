package com.example.duiudattender.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.duiudattender.database.DateRangeHelper
import com.example.duiudattender.database.TeacherAttendanceModel
import com.example.duiudattender.database.TeacherDataSource
import com.example.duiudattender.databinding.FragmentTeacherDetailsBinding
import java.time.LocalDate
import android.app.AlertDialog
import java.util.*

class TeacherDetails : Fragment() {

    lateinit var binding: FragmentTeacherDetailsBinding
    private lateinit var dataSource: TeacherDataSource
    private lateinit var dateRangeHelper: DateRangeHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTeacherDetailsBinding.inflate(layoutInflater, container, false)

        val id = arguments?.getString("id")!!.toLong()
        val name = arguments?.getString("name")

        binding.teacherTxt.text = name

        // Initialize the DateRangeHelper
        dateRangeHelper = DateRangeHelper()

        val (previousSunday, previousSaturday) = dateRangeHelper.getCurrentWeekDateRange()

        val teacherId = id

        setRecords(teacherId, previousSunday, previousSaturday)

        binding.previousBtn.setOnClickListener {

            clearRecords()

            dateRangeHelper.getPreviousWeekDateRange()
            val (previousSunday, previousSaturday) = dateRangeHelper.getCurrentWeekDateRange()

            setRecords(teacherId, previousSunday, previousSaturday)

        }

        binding.nextBtn.setOnClickListener {

            clearRecords()

            dateRangeHelper.getNextWeekDateRange()
            val (previousSunday, previousSaturday) = dateRangeHelper.getCurrentWeekDateRange()

            setRecords(teacherId, previousSunday, previousSaturday)

        }

        binding.drSunN.setOnClickListener {

            val text = binding.sunN.text.toString()
            showCustomDialog(text)

        }

        binding.drMonN.setOnClickListener {

            val text = binding.monN.text.toString()
            showCustomDialog(text)

        }

        binding.drTueN.setOnClickListener {

            val text = binding.tueN.text.toString()
            showCustomDialog(text)

        }

        binding.drWedN.setOnClickListener {

            val text = binding.wedN.text.toString()
            showCustomDialog(text)

        }

        binding.drThuN.setOnClickListener {

            val text = binding.thuN.text.toString()
            showCustomDialog(text)

        }

        binding.drFriN.setOnClickListener {

            val text = binding.friN.text.toString()
            showCustomDialog(text)

        }

        binding.drSatN.setOnClickListener {

            val text = binding.satN.text.toString()
            showCustomDialog(text)

        }

        return binding.root
    }

    private fun clearRecords() {

        binding.sunI.text = ""
        binding.sunO.text = ""
        binding.sunN.text = ""

        binding.monI.text = ""
        binding.monO.text = ""
        binding.monN.text = ""

        binding.tueI.text = ""
        binding.tueO.text = ""
        binding.tueN.text = ""

        binding.wedI.text = ""
        binding.wedO.text = ""
        binding.wedN.text = ""

        binding.thuI.text = ""
        binding.thuO.text = ""
        binding.thuN.text = ""

        binding.friI.text = ""
        binding.friO.text = ""
        binding.friN.text = ""

        binding.satI.text = ""
        binding.satO.text = ""
        binding.satN.text = ""

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setRecords(teacherId: Long, previousSunday: String, previousSaturday: String) {

        val dataSource = TeacherDataSource(requireContext())
        dataSource.open()

        val records = dataSource.getAttendanceRecordsForTeacherAndDateRange(
            teacherId,
            previousSunday,
            previousSaturday
        )
        dataSource.close()

        val allRecordsText = buildString {
            append("Attendance Records for Week:\n\n${records.size}")

            var indexMorning = 0
            var indexEvening = 0

            val recordsMorning: List<TeacherAttendanceModel> =
                records.filter { it.date.contains("M") }

            val finalMorning = setFinalRecords(previousSunday, "M", recordsMorning)


            val recordsEvening: List<TeacherAttendanceModel> =
                records.filter { it.date.contains("E") }

            val finalEvening = setFinalRecords(previousSunday, "E", recordsEvening)

            for (record in finalMorning) {

                if (indexMorning == 0) {

                    binding.sunI.text = record.signInTime
                    binding.sunO.text = record.signOutTime
                    binding.sunN.text = record.note

                } else if (indexMorning == 1) {

                    binding.monI.text = record.signInTime
                    binding.monO.text = record.signOutTime
                    binding.monN.text = record.note

                } else if (indexMorning == 2) {

                    binding.tueI.text = record.signInTime
                    binding.tueO.text = record.signOutTime
                    binding.tueN.text = record.note

                } else if (indexMorning == 3) {

                    binding.wedI.text = record.signInTime
                    binding.wedO.text = record.signOutTime
                    binding.wedN.text = record.note

                } else if (indexMorning == 4) {

                    binding.thuI.text = record.signInTime
                    binding.thuO.text = record.signOutTime
                    binding.thuN.text = record.note

                } else if (indexMorning == 5) {

                    binding.friI.text = record.signInTime
                    binding.friO.text = record.signOutTime
                    binding.friN.text = record.note

                } else if (indexMorning == 6) {

                    binding.satI.text = record.signInTime
                    binding.satO.text = record.signOutTime
                    binding.satN.text = record.note

                }

                indexMorning++


            }

            for (recordE in finalEvening) {

                if (indexEvening == 0) {

                    binding.sunIE.text = recordE.signInTime
                    binding.sunOE.text = recordE.signOutTime

                    val text = "Morning Note: " + binding.sunN.text.toString() +"\n Evening Note: "+ recordE.note

                    binding.sunN.text = text

                } else if (indexEvening == 1) {

                    binding.monIE.text = recordE.signInTime
                    binding.monOE.text = recordE.signOutTime

                    val text = "Morning Note: " + binding.monN.text.toString() +"\n Evening Note: "+ recordE.note

                    binding.monN.text = text

                } else if (indexEvening == 2) {

                    binding.tueIE.text = recordE.signInTime
                    binding.tueOE.text = recordE.signOutTime

                    val text = "Morning Note: " + binding.tueN.text.toString() +"\n Evening Note: "+ recordE.note

                    binding.tueN.text = text

                } else if (indexEvening == 3) {

                    binding.wedIE.text = recordE.signInTime
                    binding.wedOE.text = recordE.signOutTime

                    val text = "Morning Note: " + binding.wedN.text.toString() +"\n Evening Note: "+ recordE.note

                    binding.wedN.text = text

                } else if (indexEvening == 4) {

                    binding.thuIE.text = recordE.signInTime
                    binding.thuOE.text = recordE.signOutTime

                    val text = "Morning Note: " + binding.thuN.text.toString() +"\n Evening Note: "+ recordE.note

                    binding.thuN.text = text


                } else if (indexEvening == 5) {

                    binding.friIE.text = recordE.signInTime
                    binding.friOE.text = recordE.signOutTime

                    val text = "Morning Note: " + binding.friN.text.toString() +"\n Evening Note: "+ recordE.note

                    binding.friN.text = text

                } else if (indexEvening == 6) {

                    binding.satIE.text = recordE.signInTime
                    binding.satOE.text = recordE.signOutTime

                    val text = "Morning Note: " + binding.satN.text.toString() +"\n Evening Note: "+ recordE.note

                    binding.satN.text = text

                }

                indexEvening++

            }

        }

        dataSource.close()

    }

    private fun setFinalRecords(
        previousSunday: String,
        ch: String,
        recordsMorning: List<TeacherAttendanceModel>
    ): List<TeacherAttendanceModel> {
        val result = mutableListOf<TeacherAttendanceModel>()

        if (ch == "M") {

            // Iterate over each day from previousSunday to previousSaturday
            for (i in 0 until 7) {
                val currentDate = LocalDate.parse(previousSunday).plusDays(i.toLong()).toString()

                // Check if recordsMorning contains the current date
                val attendanceData = recordsMorning.find { it.date == currentDate + "M" }

                // If attendanceData is found, add it to the result list; otherwise, create a default entry with '-' values
                result.add(attendanceData ?: createDefaultAttendanceModel(currentDate))
            }

        } else if (ch == "E")

        // Iterate over each day from previousSunday to previousSaturday
            for (i in 0 until 7) {
                val currentDate = LocalDate.parse(previousSunday).plusDays(i.toLong()).toString()

                // Check if recordsMorning contains the current date
                val attendanceData = recordsMorning.find { it.date == currentDate + "E" }

                // If attendanceData is found, add it to the result list; otherwise, create a default entry with '-' values
                result.add(attendanceData ?: createDefaultAttendanceModel(currentDate))
            }


        return result
    }

    private fun createDefaultAttendanceModel(date: String): TeacherAttendanceModel {
        // Create a default TeacherAttendanceModel with '-' values
        return TeacherAttendanceModel(
            attendanceId = 0,  // Replace with an appropriate default value
            teacherId = 0,  // Replace with an appropriate default value
            signInTime = "-",
            signOutTime = "-",
            note = "-",
            date = date
        )
    }

    fun showCustomDialog(text: String) {
        val builder = AlertDialog.Builder(context)

        // Set the dialog title and message
        builder.setTitle("Dialog Title")
        builder.setMessage(text!!)

        // Set up the Cancel button
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        // Set up the OK button
        builder.setPositiveButton("OK") { dialog, _ ->
            // Handle the OK button click
            // Add your logic here
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }


//    fun calculateTimeDuration(startTime: String, endTime: String): String {
//        val timeFormat = SimpleDateFormat("hh:mm a")
//
//        val startDate = timeFormat.parse(startTime)
//        val endDate = timeFormat.parse(endTime)
//
//        if (startDate != null && endDate != null) {
//            val durationMillis = endDate.time - startDate.time
//
//            val durationHour = durationMillis / (60 * 60 * 1000)
//            val durationMinute = (durationMillis / (60 * 1000)) % 60
//
//            return String.format("%02d:%02d", durationHour, durationMinute)
//        } else {
//            return "Error!"
//        }
//    }
}