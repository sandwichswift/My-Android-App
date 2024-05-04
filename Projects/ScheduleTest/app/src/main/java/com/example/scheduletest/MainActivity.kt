package com.example.scheduletest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Random

class MainActivity : AppCompatActivity() {
    lateinit var adapter: ScheduleDataAdapter
    lateinit var scheduleRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val data = createDataTest()
        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView)
        scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ScheduleDataAdapter(data)
        scheduleRecyclerView.adapter = adapter

    }
    fun createDataTest(): List<ScheduleData>{
        val data = mutableListOf<ScheduleData>()
        val ran  = Random()
        for(i in 0..10){
            val ranValue = ran.nextInt(100)
            data.add(ScheduleData("Title $ranValue", "Time $ranValue", "Location $ranValue", "Description $ranValue"))
        }
        return data
    }
}