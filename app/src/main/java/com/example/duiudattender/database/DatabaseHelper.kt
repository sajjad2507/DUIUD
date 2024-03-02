package com.example.duiudattender.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "Teachers.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_TEACHERS = "teachers"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PASSCODE = "passcode"

        const val TABLE_ATTENDANCE = "attendance"
        const val COLUMN_ATTENDANCE_ID = "attendance_id"
        const val COLUMN_TEACHER_ID = "teacher_id"
        const val COLUMN_SIGN_IN_TIME = "sign_in_time"
        const val COLUMN_SIGN_OUT_TIME = "sign_out_time"
        const val COLUMN_NOTE = "note"
        const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTeachersTable = "CREATE TABLE $TABLE_TEACHERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_PASSCODE TEXT" +
                ");"

        val createAttendanceTable = "CREATE TABLE $TABLE_ATTENDANCE (" +
                "$COLUMN_ATTENDANCE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TEACHER_ID INTEGER, " +
                "$COLUMN_SIGN_IN_TIME TEXT, " +
                "$COLUMN_SIGN_OUT_TIME TEXT, " +
                "$COLUMN_NOTE TEXT, " +
                "$COLUMN_DATE TEXT" +
                ");"

        db.execSQL(createTeachersTable)
        db.execSQL(createAttendanceTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHERS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE)
        onCreate(db)
    }

}
