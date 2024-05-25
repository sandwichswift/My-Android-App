package com.example.myapplication.RecylerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class OCRViewHolder private constructor(itemView: View)
    : RecyclerView.ViewHolder(itemView) {
    //引用控件
        private val ocrText = itemView.findViewById<TextView>(R.id.ocrText)
    //绑定数据
    fun bind(ocrRes: String) {
        ocrText.text = ocrRes
    }
    companion object {
        //创建ViewHolder
        fun from(parent: ViewGroup): OCRViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_ocr, parent, false)
            return OCRViewHolder(view)
        }
    }
}