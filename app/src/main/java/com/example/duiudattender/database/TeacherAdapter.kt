package com.example.duiudattender.database

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.duiudattender.R

class TeacherAdapter(
    val requireContext: Context,
    val ch: String,
    val navHostFragment: Fragment,
    private val teachers: MutableList<TeacherModel>
) : RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    inner class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teacherName: Button = itemView.findViewById(R.id.teacherNameTextView)
    }

    fun addTeacher(teacher: TeacherModel) {
        teachers.add(teacher)
        notifyItemInserted(teachers.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.teacher_item, parent, false)
        return TeacherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacher = teachers[position]
        holder.teacherName.setText(teacher.name)

        val test = teacher.passcode
        val id = teacher.id

        holder.teacherName.setOnClickListener {

            if (ch == "home") {

                val bundle = Bundle()
                bundle.putString("check", "t")
                bundle.putString("nPass", test)
                bundle.putString("id", id.toString())
                NavHostFragment.findNavController(navHostFragment)
                    .navigate(R.id.action_homeFragment_to_passwordCheck, bundle)

            } else if (ch == "admin") {

                val bundle = Bundle()
                bundle.putString("id", id.toString())
                bundle.putString("name", teacher.name)

                NavHostFragment.findNavController(navHostFragment)
                    .navigate(R.id.action_adminHome2_to_teacherDetails, bundle)

            } else if (ch == "remove") {

                val bundle = Bundle()
                bundle.putString("id", id.toString())
                bundle.putString("name", teacher.name)

                NavHostFragment.findNavController(navHostFragment)
                    .navigate(R.id.action_removeTeacherList2_to_removeTeacher, bundle)

            }

        }

    }

    override fun getItemCount(): Int {
        return teachers.size
    }
}
