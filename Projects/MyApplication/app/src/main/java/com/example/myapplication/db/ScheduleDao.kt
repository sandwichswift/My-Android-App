package com.example.myapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    suspend fun getAll(): List<Schedule>

    @Query("SELECT * FROM schedule WHERE id = :id")
    suspend fun getById(id: Long): Schedule
    @Query("SELECT * FROM schedule")
    fun getAllReturnLivedata(): LiveData<List<Schedule>>
    @Query("DELETE FROM schedule")
    suspend fun deleteAll()
    @Query("DELETE FROM schedule WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert
    suspend fun insert(schedule: Schedule)

    @Update
    suspend fun update(schedule: Schedule)
    @Delete
    suspend fun delete(schedule: Schedule)
}