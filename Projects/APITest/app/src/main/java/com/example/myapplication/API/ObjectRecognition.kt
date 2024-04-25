package com.example.myapplication.API

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Base64


class ObjectRecognition {
    private val apiKey = "d1YKX02B7FQdGlqtcPVLwB9N"
    private val secretKey = "oOxTFhXc8knWPbqi42lJixkaO64X6gbe"
    private var accessToken = ""
    private val tokenURL = "https://aip.baidubce.com/oauth/2.0/token"
    //获取百度AI的access token
    fun getAccessToken() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("${tokenURL}?grant_type=client_credentials&client_id=${apiKey}&client_secret=${secretKey}")
            .method("POST", null)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            accessToken = responseBody?.split("\"access_token\":\"")?.get(1)?.split("\"")?.get(0) ?: ""
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //调用百度AI的物体识别API
    fun generalRecognition(imgPath: String): String{
        getAccessToken()//获取access token
        val generalURL = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general"
        val imageBytes = Files.readAllBytes(Paths.get(imgPath))
        val imageBase64 = Base64.getEncoder().encodeToString(imageBytes)//图片转base64
        val json = """
        {
            "image": "$imageBase64",
            "baike_num": 5
        }
        """.trimIndent()//构建json
        val mediaType: MediaType? = "application/json".toMediaTypeOrNull()//设置请求头
        val requestBody = RequestBody.create(mediaType, json)//构建请求体
        val client = OkHttpClient()
        //构建请求
        val request = Request.Builder()
            .url("${generalURL}?access_token=${accessToken}")
            .method("POST", requestBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()
        //发送请求
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            println(responseBody)
            return responseBody ?: ""
        } catch (e: IOException) {
            e.printStackTrace()
            return "error"
        }
    }
}