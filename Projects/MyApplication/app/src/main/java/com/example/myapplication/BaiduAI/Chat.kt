package com.example.myapplication.BaiduAI

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class Chat {
    private val apiKey = "776KcpoOUSFaWaM7EPURs2E3"
    private val secretKey = "cN9HWNoHXHyH0a9fMDwEQVEow0Mo1SuF"//baiduAI的apiKey和secretKey
    private var accessToken = ""
    private val tokenURL = "https://aip.baidubce.com/oauth/2.0/token"
    //获取百度AI的access token
    fun getAccessToken(): String {
        val client = OkHttpClient()
        val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, "{}")

        val request = Request.Builder()
            .url("${tokenURL}?grant_type=client_credentials&client_id=${apiKey}&client_secret=${secretKey}")
            .post(requestBody)
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
        return accessToken
    }
    //调用百度AI的对话API,文心一言3.5
    val baseurl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-3.5-128k"
    fun chatWithRobot(question: String): String {
        val client = OkHttpClient()
        val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, "{\"question\":\"${question}\"}")

        val request = Request.Builder()
            .url("$baseurl?access_token=${accessToken}")
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            return responseBody ?: ""
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
}