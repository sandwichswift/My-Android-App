package com.example.scheduletest

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ScheduleDataAdapter(private val data: List<ScheduleData>)
    : RecyclerView.Adapter<ScheduleDataViewHolder>() {
        //创建ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleDataViewHolder {
        return ScheduleDataViewHolder.from(parent)
    }
    //绑定数据
    override fun onBindViewHolder(holder: ScheduleDataViewHolder, position: Int) {
        holder.bind(data[position])
    }
    //获取数据数量
    override fun getItemCount(): Int {
        return data.size
    }
}