package com.example.myapplication.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class Schedule (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "createTime") val createTime: String,
    @ColumnInfo(name = "location") val location: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean = false
)
