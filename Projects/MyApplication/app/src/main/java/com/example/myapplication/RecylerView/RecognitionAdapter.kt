package com.example.myapplication.RecylerView

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.aitool.AitoolViewModel

class RecognitionAdapter(private val data: List<RecognitionRes>)
    :RecyclerView.Adapter<RecognitionViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecognitionViewHolder {
        return RecognitionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecognitionViewHolder, position: Int) {
        holder.bind(data[position])
    }

    //获取数据数量
    override fun getItemCount(): Int {
        return data.size
    }
}