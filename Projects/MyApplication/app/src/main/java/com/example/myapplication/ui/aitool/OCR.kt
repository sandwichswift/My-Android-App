package com.example.myapplication.ui.aitool

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class OCR {
    companion object{
        private val apiKey = "jxmtQnmUoAvodGKMCp1NHWLG"
        private val secretKey = "8oceAcYAFQ9z2uFymq3OXm66ztOziR5s"
        private var accessToken = ""
        private val tokenURL = "https://aip.baidubce.com/oauth/2.0/token"
        suspend fun getAccessToken(){
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
                log("getAccessToken:${accessToken}")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        fun getAccessTokenTest(){
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
                log("getAccessToken:${accessToken}")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        suspend fun ocrFromBase64(Base64String:String):String {
            getAccessToken()
            val ocrUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic"

            val mediaType: MediaType? = "application/x-www-form-urlencoded".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, "access_token=${accessToken}&image=${Base64String}")
            val request = Request.Builder()
                .url(ocrUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build()
            val client = OkHttpClient().newBuilder().build()
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                return responseBody?: ""
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }

        fun ocrFromBase64Test(Base64String:String):String {
            getAccessTokenTest()
            val ocrUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic"

            val mediaType: MediaType? = "application/x-www-form-urlencoded".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, "access_token=${accessToken}&image=${Base64String}")
            val request = Request.Builder()
                .url(ocrUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build()
            val client = OkHttpClient().newBuilder().build()
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                return responseBody?: ""
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}