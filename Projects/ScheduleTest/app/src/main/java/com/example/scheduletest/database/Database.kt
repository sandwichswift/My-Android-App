package com.example.scheduletest.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Schedule::class],//表的个数
    version = 1,//版本号
    exportSchema = false//不导出schema
)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}