package com.example.scheduletest

data class ScheduleData(val title: String, val time: String,
                        val location: String, val description: String,
                        var isCompleted: Boolean = false) {
}