package com.example.myapplication.RecylerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class RecognitionViewHolder private constructor(itemView: View)
    : RecyclerView.ViewHolder(itemView){
    //引用控件
    private val roottv = itemView.findViewById<TextView>(R.id.roottv)
    private val keywordtv = itemView.findViewById<TextView>(R.id.keywordtv)
    private val scoretv = itemView.findViewById<TextView>(R.id.scoretv)

    //绑定数据
    fun bind(recognitionRes: RecognitionRes){
        roottv.text = recognitionRes.root
        keywordtv.text = recognitionRes.keyword
        scoretv.text = recognitionRes.score.toString()
    }

    companion object{
        //创建ViewHolder
        fun from(parent: ViewGroup): RecognitionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_recognition, parent, false)
            return RecognitionViewHolder(view)
        }
    }

}