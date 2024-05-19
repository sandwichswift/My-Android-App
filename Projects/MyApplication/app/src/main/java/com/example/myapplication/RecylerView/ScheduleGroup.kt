package com.example.myapplication.RecylerView

import com.example.myapplication.db.Schedule

data class ScheduleGroup (
    val title: String,
    val schedules: List<Schedule>
) {
    fun getCompletedSchedules(): List<Schedule> {
        return schedules.filter { it.isCompleted }
    }
    fun getUncompletedSchedules(): List<Schedule> {
        return schedules.filter { !it.isCompleted }
    }
}