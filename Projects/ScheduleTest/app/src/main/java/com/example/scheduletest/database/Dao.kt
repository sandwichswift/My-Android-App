package com.example.scheduletest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    fun getAll(): List<Schedule>

    @Query("SELECT * FROM schedule WHERE id = :id")
    fun getById(id: Long): Schedule

    @Insert
    fun insert(schedule: Schedule)

    @Update
    fun update(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)
}