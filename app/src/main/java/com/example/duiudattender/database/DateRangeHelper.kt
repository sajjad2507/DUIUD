package com.example.duiudattender.database

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateRangeHelper {
    private var weeksToGoBack = 0

    fun getCurrentWeekDateRange(): Pair<String, String> {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.add(Calendar.DATE, -7 * weeksToGoBack) // Adjust the number of weeks to go back
        val currentSunday = calendar.time

        calendar.add(Calendar.DATE, 7) // Next Saturday is one week (6 days) ahead
        val nextSaturday = calendar.time

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentSundayString = dateFormat.format(currentSunday)
        val nextSaturdayString = dateFormat.format(nextSaturday)

        return Pair(currentSundayString, nextSaturdayString)
    }

    fun getPreviousWeekDateRange() {
        weeksToGoBack++
    }

    fun getNextWeekDateRange() {
        if (weeksToGoBack > 0) {
            weeksToGoBack--
        }
    }
}
