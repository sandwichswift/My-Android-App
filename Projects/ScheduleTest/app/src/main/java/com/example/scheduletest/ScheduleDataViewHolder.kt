package com.example.scheduletest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleDataViewHolder private constructor(itemView: View)
    : RecyclerView.ViewHolder(itemView){
        //引用控件
    private val title = itemView.findViewById<TextView>(R.id.titletv)
    private val time = itemView.findViewById<TextView>(R.id.timetv)
    private val detail = itemView.findViewById<TextView>(R.id.detailtv)
    val checkbox = itemView.findViewById<CheckBox>(R.id.checkBox)//公开checkbox

    //绑定数据
    fun bind(data: ScheduleData){
        title.text = data.title
        time.text = data.time
        detail.text = data.description
        checkbox.isChecked = data.isCompleted  //设置checkbox的选中状态
    }

    companion object{
        //用工厂方法创建ViewHolder
        fun from(parent: ViewGroup): ScheduleDataViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val root = layoutInflater.inflate(R.layout.item_schedule, parent, false)
            return ScheduleDataViewHolder(root)
        }
    }

}