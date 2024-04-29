package com.example.myapplication.ui.home

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.API.ObjectRecognition
import kotlinx.coroutines.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {


    private val _imgURI = MutableLiveData<String>()//图片路径
    val imgURI: LiveData<String>
        get() = _imgURI

    private val objectRecognition = ObjectRecognition(application)//创建物体识别对象
    private val _res = MutableLiveData<String>()//识别结果
    val res: LiveData<String>
        get() = _res

    private val _openGallery = MutableLiveData<Boolean>()
    val openGallery: LiveData<Boolean>
        get() = _openGallery

    fun generalRecognition(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO){
            val res = objectRecognition.generalRecognition(uri)
            //在主线程更新UI
            withContext(Dispatchers.Main){
                _res.value = res
            }
        }
    }
    fun chooseImage() {
        _openGallery.value = true
    }

    fun resetOpenGallery() {
        _openGallery.value = false
    }

    fun setImageUri(path: String) {
        _imgURI.value = path
    }
}