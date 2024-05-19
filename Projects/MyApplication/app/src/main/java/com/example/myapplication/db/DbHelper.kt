package com.example.myapplication.db

object DbHelper {
    fun createExampleData(): Schedule {
        val id = System.currentTimeMillis()//获取当前时间
        return Schedule(
            id,
            "title$id",
            "time$id"
        )
    }
}