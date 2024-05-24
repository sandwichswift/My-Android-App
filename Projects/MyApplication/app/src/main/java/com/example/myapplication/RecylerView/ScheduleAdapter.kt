package com.example.myapplication.RecylerView

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.db.Schedule
import com.example.myapplication.db.ScheduleDao
import com.example.myapplication.log
import com.example.myapplication.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ScheduleDataAdapter(private val data: List<Schedule>,private val homeViewModel: HomeViewModel)//传入List和vm
    : RecyclerView.Adapter<ScheduleViewHolder>() {
    //创建ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder.from(parent)
    }
    //绑定数据
    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {

        //绑定数据
        holder.bind(data[position])
        //设置checkbox监听
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->

            data[position].isCompleted = isChecked

            //更新数据库
            GlobalScope.launch(Dispatchers.IO){
                homeViewModel.update(data[position])
            }
        }
    }
    //获取数据数量
    override fun getItemCount(): Int {
        return data.size
    }
}