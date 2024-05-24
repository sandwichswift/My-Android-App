package com.example.myapplication.RecylerView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.db.Schedule
import java.time.LocalDate
import java.time.Period

class ScheduleViewHolder private constructor(itemView: View)
    : RecyclerView.ViewHolder(itemView){
    //引用控件
    private val title = itemView.findViewById<TextView>(R.id.titletv)
    private val time = itemView.findViewById<TextView>(R.id.timetv)
    private val detail = itemView.findViewById<TextView>(R.id.detailtv)
    val checkbox = itemView.findViewById<CheckBox>(R.id.checkBox)//公开checkbox
    private val daysLeft = itemView.findViewById<TextView>(R.id.daysLeft)

    init {
        itemView.setOnClickListener {
            checkbox.isChecked = !checkbox.isChecked
        }
    }

    //绑定数据
    fun bind(data: Schedule){
        title.text = data.title
        time.text = data.time
        detail.text = data.description
        checkbox.isChecked = data.isCompleted  //设置checkbox的选中状态

        if(data.isCompleted){
            //设置透明度
            itemView.alpha = 0.4f
            //将背景设置为灰色
            itemView.setBackgroundColor(0xFFE0E0E0.toInt())
        }else{
            try{
                val dateStr = data.time;
                val date = LocalDate.parse(dateStr);
                val today = LocalDate.now();
                val period = Period.between(today, date);//计算日期差,date-today
                val days = period.days;
                daysLeft.text = "剩余${days}天"

                itemView.alpha = 1.0f
                //itemView.setBackgroundColor(0xFFFFFFFF.toInt())
                //根据剩余天数设置背景颜色,argb模式
                if(days < 0){
                    itemView.setBackgroundColor(0xFFF5321D.toInt())//已过期设置红色背景
                }
                else if(days < 3 ){
                    itemView.setBackgroundColor(0xffdccea2.toInt())//剩余3天设置黄色背景
                }
                else if(days < 7){
                    itemView.setBackgroundColor(0xFF1BD6F5.toInt())//剩余7天设置蓝色背景
                }
                else{
                    itemView.setBackgroundColor(0xFF77F54C.toInt())//剩余7天以上设置绿色背景
                }
            }catch (e: Exception){
                daysLeft.text = "格式错误"
            }

        }


        itemView.setOnLongClickListener {
            val arguments = Bundle();
            arguments.putLong("id", data.id);
            arguments.putString("title", data.title)
            arguments.putString("time", data.time)
            arguments.putString("detail", data.description)
            itemView.findNavController().navigate(R.id.action_navigation_home_to_editScheduleFragment, arguments)
            true
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