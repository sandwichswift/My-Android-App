package com.example.scheduletest.database

import com.example.scheduletest.ScheduleData
import kotlin.random.Random

object DbTestHelper {
    fun createExampleData():Schedule{
        val ranval = Random.nextLong(1,100)
        return Schedule(ranval,"title$ranval","time$ranval",
            "location$ranval","description$ranval")
    }
}