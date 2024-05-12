package com.example.scheduletest

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ScheduleDataAdapter(private val data: MutableList<ScheduleData>)
    : RecyclerView.Adapter<ScheduleDataViewHolder>() {
        //创建ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleDataViewHolder {
        return ScheduleDataViewHolder.from(parent)
    }
    //绑定数据
    override fun onBindViewHolder(holder: ScheduleDataViewHolder, position: Int) {
        holder.bind(data[position])
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            data[position].isCompleted = isChecked
            if(data[position].isCompleted){
                //设置透明度
                holder.itemView.alpha = 0.5f
                //将背景设置为灰色
                holder.itemView.setBackgroundColor(0xFFE0E0E0.toInt())
                /*//将选中项移动到最后
                val item = data.removeAt(position)
                holder.itemView.post{//延迟刷新数据,否则会出现数据错乱
                    notifyItemRemoved(position)//删除选中项
                }
                data.add(item)*/
            }else{
                holder.itemView.alpha = 1.0f
                holder.itemView.setBackgroundColor(0xFFFFFFFF.toInt())
            }
        }
    }
    //获取数据数量
    override fun getItemCount(): Int {
        return data.size
    }
}