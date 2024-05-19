package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.myapplication.db.Schedule
import com.example.myapplication.db.ScheduleDatabase
import com.example.myapplication.log

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val db : ScheduleDatabase by lazy {
        Room.databaseBuilder(
            application, ScheduleDatabase::class.java, "schedule_database.db"
        ).fallbackToDestructiveMigrationFrom()
            .build()
    }
    private val dao = db.scheduleDao()
    var scheduleList: LiveData<List<Schedule>> = dao.getAllReturnLivedata()
        .map {
            it.sortedBy { it.isCompleted }
        }


    suspend fun addDataToDb() {
        coroutineScope {
            val id = System.currentTimeMillis()//获取当前时间戳
            val schedule = Schedule(id, "title$id", "time$id")
            dao.insert(schedule)
            log("addDataToDb$schedule")
        }
    }

    suspend fun clearAllData() {
        coroutineScope {
            dao.deleteAll()
            scheduleList = dao.getAllReturnLivedata()//更新数据
            log("clearAllData")
        }
    }

    suspend fun update(schedule: Schedule) {
        coroutineScope {
            dao.update(schedule)
            log("update$schedule")
        }
    }
}