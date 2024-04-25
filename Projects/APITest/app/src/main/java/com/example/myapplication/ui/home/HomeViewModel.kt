package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.API.ObjectRecognition

class HomeViewModel : ViewModel() {


    private val _imgURL = MutableLiveData<String>()//图片路径
    val imgURL: LiveData<String>
        get() = _imgURL

    private val objectRecognition = ObjectRecognition()//创建物体识别对象

    private val _openGallery = MutableLiveData<Boolean>()
    val openGallery: LiveData<Boolean>
        get() = _openGallery


    fun chooseImage() {
        _openGallery.value = true
    }

    fun resetOpenGallery() {
        _openGallery.value = false
    }

    fun setImagePath(path: String) {
        _imgURL.value = path
    }
}