package com.example.duiudattender.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class TeacherDataSource(context: Context) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)
    private var database: SQLiteDatabase? = null

    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        database?.close()
    }

    fun createTeacher(name: String, passcode: String): TeacherModel {
        if (database == null) {
            open()
        }

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NAME, name)
            put(DatabaseHelper.COLUMN_PASSCODE, passcode)
        }

        val insertId = database?.insert(DatabaseHelper.TABLE_TEACHERS, null, values)

        val cursor = database?.query(
            DatabaseHelper.TABLE_TEACHERS,
            null,
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(insertId.toString()),
            null, null, null
        )

        cursor?.moveToFirst()
        val newTeacher = cursorToTeacher(cursor)
        cursor?.close()
        return newTeacher
    }

    fun deleteTeacher(teacherId: Long) {
        if (database == null) {
            open()
        }

        database?.delete(
            DatabaseHelper.TABLE_TEACHERS,
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(teacherId.toString())
        )
    }

    fun getAllTeachers(): List<TeacherModel> {
        if (database == null) {
            open()
        }

        val teachers = mutableListOf<TeacherModel>()
        val cursor = database?.query(
            DatabaseHelper.TABLE_TEACHERS,
            null, null, null, null, null, null
        )

        cursor?.moveToFirst()

        while (!cursor?.isAfterLast!!) {
            val teacher = cursorToTeacher(cursor)
            teachers.add(teacher)
            cursor.moveToNext()
        }
        cursor?.close()
        return teachers
    }

    fun createAttendance(
        teacherId: Long,
        signInTime: String,
        signOutTime: String,
        note: String,
        date: String
    ): Long {
        if (database == null) {
            open()
        }

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TEACHER_ID, teacherId)
            put(DatabaseHelper.COLUMN_SIGN_IN_TIME, signInTime)
            put(DatabaseHelper.COLUMN_SIGN_OUT_TIME, signOutTime)
            put(DatabaseHelper.COLUMN_NOTE, note)
            put(DatabaseHelper.COLUMN_DATE, date)
        }

        return database?.insert(DatabaseHelper.TABLE_ATTENDANCE, null, values) ?: -1
    }

    fun getTeacherById(teacherId: Double): TeacherModel? {
        val cursor = database?.query(
            DatabaseHelper.TABLE_TEACHERS,
            null,
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(teacherId.toString()),
            null, null, null
        )

        cursor?.moveToFirst()

        if (cursor != null && cursor.moveToFirst()) {
            val teacher = cursorToTeacher(cursor)
            cursor.close()
            return teacher
        }

        return null // Return null if the teacher with the specified ID is not found
    }

    fun createAttendanceForTeacher(
        teacherId: Long,
        signInTime: String,
        signOutTime: String,
        note: String,
        date: String
    ): Long {
        if (database == null) {
            open()
        }

        // Check if an attendance record already exists for the specified teacher and date
        val existingRecord = getAttendanceRecordForTeacherAndDate(teacherId, date)

        if (existingRecord != null) {
            // Data already exists, show a message or handle as needed
            return -1 // Return a special value (-1) to indicate that data already exists
        } else {
            val values = ContentValues().apply {
                put(DatabaseHelper.COLUMN_TEACHER_ID, teacherId)
                put(DatabaseHelper.COLUMN_SIGN_IN_TIME, signInTime)
                put(DatabaseHelper.COLUMN_SIGN_OUT_TIME, signOutTime)
                put(DatabaseHelper.COLUMN_NOTE, note)
                put(DatabaseHelper.COLUMN_DATE, date)
            }

            return database?.insert(DatabaseHelper.TABLE_ATTENDANCE, null, values) ?: -1
        }
    }

    fun updateAttendanceForTeacherAndDate(
        teacherId: Long,
        date: String,
        signOutTime: String
    ): Int {
        if (database == null) {
            open()
        }

        // Check if an attendance record already exists for the specified teacher and date
        val existingRecord = getAttendanceRecordForTeacherAndDate(teacherId, date)

        if (existingRecord != null) {
            if (existingRecord.signOutTime.isNotEmpty()) {
                // Existing record already has a non-empty sign-out time, so don't allow further updates
                return -1
            } else {
                // Update the sign-out time for the existing record
                val values = ContentValues().apply {
                    put(DatabaseHelper.COLUMN_SIGN_OUT_TIME, signOutTime)
                }

                return database?.update(
                    DatabaseHelper.TABLE_ATTENDANCE,
                    values,
                    "${DatabaseHelper.COLUMN_TEACHER_ID} = ? AND ${DatabaseHelper.COLUMN_DATE} = ?",
                    arrayOf(teacherId.toString(), date)
                ) ?: -1
            }
        } else {
            // If the record doesn't exist, you can handle it here, such as showing a message
            return -1
        }
    }

    fun updateNoteForTeacherAndDate(
        teacherId: Long,
        date: String,
        newNote: String
    ): Int {
        if (database == null) {
            open()
        }

        // Check if an attendance record already exists for the specified teacher and date
        val existingRecord = getAttendanceRecordForTeacherAndDate(teacherId, date)

        if (existingRecord != null) {
            // Update the note for the existing record
            val values = ContentValues().apply {
                put(DatabaseHelper.COLUMN_NOTE, newNote)
            }

            return database?.update(
                DatabaseHelper.TABLE_ATTENDANCE,
                values,
                "${DatabaseHelper.COLUMN_TEACHER_ID} = ? AND ${DatabaseHelper.COLUMN_DATE} = ?",
                arrayOf(teacherId.toString(), date)
            ) ?: -1
        } else {
            // If the record doesn't exist, you can handle it here, such as showing a message
            return -1
        }
    }


    fun getAttendanceRecordForTeacherAndDate(
        teacherId: Long,
        date: String
    ): TeacherAttendanceModel? {
        if (database == null) {
            open()
        }

        val cursor = database?.query(
            DatabaseHelper.TABLE_ATTENDANCE,
            null,
            "${DatabaseHelper.COLUMN_TEACHER_ID} = ? AND ${DatabaseHelper.COLUMN_DATE} = ?",
            arrayOf(teacherId.toString(), date),
            null, null, null
        )

        cursor?.moveToFirst()

        if (cursor != null && cursor.moveToFirst()) {
            val record = cursorToAttendance(cursor)
            cursor.close()
            return record
        }

        return null // Return null if no matching attendance record is found
    }

    fun getAttendanceRecordsForTeacherAndDateRange(
        teacherId: Long,
        startDate: String,
        endDate: String
    ): List<TeacherAttendanceModel> {
        val records = mutableListOf<TeacherAttendanceModel>()

        val cursor = database?.query(
            DatabaseHelper.TABLE_ATTENDANCE,
            null,
            "${DatabaseHelper.COLUMN_TEACHER_ID} = ? AND ${DatabaseHelper.COLUMN_DATE} BETWEEN ? AND ?",
            arrayOf(teacherId.toString(), startDate.toString(), endDate.toString()),
            null, null, null
        )

        cursor?.moveToFirst()

        while (!cursor?.isAfterLast!!) {
            val record = cursorToAttendance(cursor)
            records.add(record)
            cursor.moveToNext()
        }

        cursor?.close()
        return records
    }



    private fun cursorToTeacher(cursor: Cursor?): TeacherModel {
        return TeacherModel(
            cursor?.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)) ?: -1,
            cursor?.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)) ?: "",
            cursor?.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSCODE)) ?: ""
        )
    }

    private fun cursorToAttendance(cursor: Cursor?): TeacherAttendanceModel {
        return TeacherAttendanceModel(
            cursor?.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ATTENDANCE_ID)) ?: -1,
            cursor?.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEACHER_ID)) ?: -1,
            cursor?.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SIGN_IN_TIME)) ?: "",
            cursor?.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SIGN_OUT_TIME)) ?: "",
            cursor?.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE)) ?: "",
            cursor?.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)) ?: ""
        )
    }

}