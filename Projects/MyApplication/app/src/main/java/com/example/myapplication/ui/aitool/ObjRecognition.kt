package com.example.myapplication.ui.aitool

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.net.URL

class ObjRecognition {

    companion object{
        private val apiKey = "d1YKX02B7FQdGlqtcPVLwB9N"
        private val secretKey = "oOxTFhXc8knWPbqi42lJixkaO64X6gbe"
        private var accessToken = ""
        private val tokenURL = "https://aip.baidubce.com/oauth/2.0/token"
        suspend fun getAccessToken() {
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
                log("getAccessToken:$accessToken")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun getAccessTokenTest() {
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
                log("getAccessToken:$accessToken")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        suspend fun generalRecognitionFromBase64(base64String: String): String {
            getAccessToken()
            val generalURL = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general"

            val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
            val body = RequestBody.create(mediaType, "image=${base64String}")
            val request = Request.Builder()
                .url("${generalURL}?access_token=${accessToken}")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build()
            val client = OkHttpClient().newBuilder().build()
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                return responseBody ?: ""
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }

        suspend fun generalRecognitionFromUrl(url: String): String {
            getAccessToken()
            val generalURL = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general"
            val json = """
            {
                "url": "${url}",
                "baike_num": 5
            }
            """.trimIndent()
            val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, json)
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("${generalURL}?access_token=${accessToken}")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer ${accessToken}")
                .build()
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                log("generalRecognitionFromUrl:$responseBody")
                return responseBody ?: ""
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }

        fun generalRecognitionFromUrlTest(url: String): String {
            getAccessTokenTest()
            val generalURL = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general"

            val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
            val body = RequestBody.create(mediaType, "url=${url}")
            val request = Request.Builder()
                .url("${generalURL}?access_token=${accessToken}")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build()
            val client = OkHttpClient().newBuilder().build()
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                log("generalRecognitionFromUrl:$responseBody")
                return responseBody ?: ""
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}