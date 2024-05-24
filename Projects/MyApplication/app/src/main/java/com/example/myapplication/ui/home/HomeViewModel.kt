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
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.myapplication.R
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

    val scheduleListCompleted: LiveData<List<Schedule>> = scheduleList.map {
        it.filter { it.isCompleted }
    }
    val scheduleListUncompleted: LiveData<List<Schedule>> = scheduleList.map {
        it.filter { !it.isCompleted }
    }

    val weatherInfo: MutableLiveData<String> = MutableLiveData()






    suspend fun addDataToDb(schedule: Schedule) {
        coroutineScope {
            dao.insert(schedule)
            log("addDataToDb$schedule")
        }
    }

    suspend fun clearAllData() {
        coroutineScope {
            dao.deleteAll()
            log("clearAllData")
        }
    }

    suspend fun update(schedule: Schedule) {
        coroutineScope {
            dao.update(schedule)
            log("update$schedule")
        }
    }

    suspend fun delete(id: Long) {
        coroutineScope {
            dao.deleteById(id)
            log("delete$id")
        }

    }
}