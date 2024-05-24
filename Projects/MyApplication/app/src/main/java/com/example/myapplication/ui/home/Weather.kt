package com.example.myapplication.ui.home

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class Weather {
    companion object{//伴生对象直接调用
        private val apiKey = "05b24e4b27e17fc90c94173271264138"
        suspend fun getAdCode(address: String):String {
            //if(address == null)return "11010"//默认北京
            val client = OkHttpClient()
            val url = "https://restapi.amap.com/v3/geocode/geo?address=$address&output=JSON&key=$apiKey"
            val request = Request.Builder().url(url).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseData = response.body?.string()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData)
                    if (jsonObject.has("geocodes")) {
                        val geoCodes = jsonObject.getJSONArray("geocodes")
                        if (geoCodes.length() > 0) {
                            val geoCodeInfo = geoCodes.getJSONObject(0)
                            val adcode = geoCodeInfo.getString("adcode")
                            log("adcode:$adcode")
                            return adcode
                        }
                    } else {
                        throw JSONException("No value for geocodes")
                    }
                }
                return ""
            }
        }
        suspend fun getWeather(city: String):String {
            val geocode = getAdCode(city)
            val client = OkHttpClient()
            val url = "https://restapi.amap.com/v3/weather/weatherInfo?city=$geocode&key=$apiKey"
            val request = Request.Builder().url(url).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseData = response.body?.string()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData)
                    val livesArray = jsonObject.getJSONArray("lives")
                    if (livesArray.length() > 0) {
                        val weatherInfo = livesArray.getJSONObject(0)
                        val weather = weatherInfo.getString("weather")
                        val temperature = weatherInfo.getString("temperature")

                        log("weather:$weather")
                        return "Weather in $city: $weather, temperature: $temperature"
                    }
                }
                return "No response"
            }
        }
    }
}
