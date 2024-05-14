package com.example.scheduletest

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.scheduletest.database.DbTestHelper
import com.example.scheduletest.database.ScheduleDao
import com.example.scheduletest.database.ScheduleDatabase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

const val TAG = "Room"
fun log(info:Any){
    Log.d(TAG,info.toString())
}

@RunWith(AndroidJUnit4::class)
class ScheduleDatabaseTest {
    //用于引用数据库和dao
    private lateinit var db: ScheduleDatabase
    private lateinit var dao: ScheduleDao

    @Before
    fun createDb(){
        //获取上下文对象
        val context = ApplicationProvider.getApplicationContext<Context>()
        //实例化Room数据库对象，测试常用内存数据库
        db = Room.inMemoryDatabaseBuilder(
            context,ScheduleDatabase::class.java
        ).build()
        dao = db.scheduleDao()
        log("Database created.")
    }

    @After//测试结束关闭数据库
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
        log("Database closed.")
    }

    @Test
    fun addTest(){
        val scheduledata = DbTestHelper.createExampleData()
        dao.insert(scheduledata)
        log("$scheduledata added.")
        val obj = dao.getById(scheduledata.id)
        log("get record $scheduledata")
        Assert.assertNotNull(obj)
    }

    @Test
    fun deleteTest(){
        val scheduledata = DbTestHelper.createExampleData()
        dao.insert(scheduledata)
        log("$scheduledata added.")
        dao.delete(scheduledata)
        log("$scheduledata deleted.")
        val obj = dao.getById(scheduledata.id)
        log("get record $scheduledata")
        Assert.assertNull(obj)
    }

    @Test
    fun updateTest(){
        val scheduledata = DbTestHelper.createExampleData()
        dao.insert(scheduledata)
        log("$scheduledata added.")
        val newdata = scheduledata.copy(title = "new title")
        dao.update(newdata)
        log("$newdata updated.")
        val obj = dao.getById(scheduledata.id)
        log("get record $newdata")
        Assert.assertEquals(newdata,obj)
    }
}