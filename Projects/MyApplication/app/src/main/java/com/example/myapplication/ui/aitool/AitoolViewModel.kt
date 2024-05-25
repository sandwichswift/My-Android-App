package com.example.myapplication.ui.aitool

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.google.android.gms.common.util.Base64Utils
import java.io.ByteArrayOutputStream
import java.net.URLEncoder


class AitoolViewModel : ViewModel() {
    fun setImgUri(uri: String) {
        log("setImgUri: $uri")
        _imgUri.value = uri
    }

    private val _imgUri = MutableLiveData<String>()
    val imgUri: LiveData<String>
        get() = _imgUri

    private val _imgBase64String = MutableLiveData<String>()
    val imgBase64String: LiveData<String>
        get() = _imgBase64String

    private val _recognitionResult = MutableLiveData<String>()
    val recognitionResult: LiveData<String> get() = _recognitionResult

    fun setRecognitionResult(result: String) {
        _recognitionResult.value = result
    }

    private val _ocrImgUri = MutableLiveData<String>()
    val ocrImgUri: LiveData<String>
        get() = _ocrImgUri
    fun setOcrImgUri(uri: String) {
        log("setOcrImgUri: $uri")
        _ocrImgUri.value = uri
    }
    private val _ocrImgBase64String = MutableLiveData<String>()
    val ocrImgBase64String: LiveData<String>
        get() = _ocrImgBase64String
    private val _ocrResult = MutableLiveData<String>()
    val ocrResult: LiveData<String>
        get() = _ocrResult
    fun setOcrResult(result: String) {
        _ocrResult.value = result
    }

    fun convertImageUriToBase64(contentResolver: ContentResolver,uriString: String): String? {
        return try {
            // 将String转换为Uri
            val uri = Uri.parse(uriString)

            // 从Uri获取Bitmap
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // 将Bitmap转换为Base64编码的字符串
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)

            val urlEncode = URLEncoder.encode(base64String, "UTF-8")
            _imgBase64String.value = urlEncode//将base64String存入LiveData


            urlEncode
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun convertImageUriToBase64OCR(contentResolver: ContentResolver,uriString: String): String? {
        return try {
            // 将String转换为Uri
            val uri = Uri.parse(uriString)

            // 从Uri获取Bitmap
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // 将Bitmap转换为Base64编码的字符串
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)

            val urlEncode = URLEncoder.encode(base64String, "UTF-8")
            _ocrImgBase64String.value = urlEncode//将base64String存入LiveData

            urlEncode
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}