package com.example.duiudattender.database

data class TeacherAttendanceModel(
    val attendanceId: Long,
    val teacherId: Long,
    val signInTime: String,
    val signOutTime: String,
    val note: String,
    val date: String
)

