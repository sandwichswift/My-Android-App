package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Schedule::class],
    version = 1,
    exportSchema = false//不导出schema
)
abstract class ScheduleDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {//伴生对象,类似于Java中的静态方法
        @Volatile
        private var INSTANCE: ScheduleDatabase? = null

        fun getDatabase(context: Context): ScheduleDatabase {
            val tempInstance = INSTANCE//临时实例
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {//同步
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScheduleDatabase::class.java,
                    "schedule_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}