package com.example.myapplication.RecylerView

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OCRAdapter(private val data: List<String>)
    : RecyclerView.Adapter<OCRViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OCRViewHolder {
        return OCRViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OCRViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}