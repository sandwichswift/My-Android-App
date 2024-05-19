package com.example.myapplication.RecylerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.db.Schedule

class ScheduleViewHolder private constructor(itemView: View)
    : RecyclerView.ViewHolder(itemView){
    //引用控件
    private val title = itemView.findViewById<TextView>(R.id.titletv)
    private val time = itemView.findViewById<TextView>(R.id.timetv)
    private val detail = itemView.findViewById<TextView>(R.id.detailtv)
    val checkbox = itemView.findViewById<CheckBox>(R.id.checkBox)//公开checkbox

    //绑定数据
    fun bind(data: Schedule){
        title.text = data.title
        time.text = data.time
        detail.text = data.description
        checkbox.isChecked = data.isCompleted  //设置checkbox的选中状态
        if(data.isCompleted){
            //设置透明度
            itemView.alpha = 0.5f
            //将背景设置为灰色
            itemView.setBackgroundColor(0xFFE0E0E0.toInt())
        }else{
            itemView.alpha = 1.0f
            itemView.setBackgroundColor(0xFFFFFFFF.toInt())
        }
    }

    companion object{
        //用工厂方法创建ViewHolder
        fun from(parent: ViewGroup): ScheduleViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val root = layoutInflater.inflate(R.layout.item_schedule, parent, false)
            return ScheduleViewHolder(root)
        }
    }

}