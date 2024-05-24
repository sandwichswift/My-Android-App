package com.example.myapplication

import com.example.myapplication.BaiduAI.Chat
import com.example.myapplication.ui.aitool.ObjRecognition
import com.example.myapplication.ui.home.Weather
import kotlinx.coroutines.coroutineScope
import org.junit.Test

class APITest {
    /*@Test
    fun testGetAccessToken() {
        val chat = Chat()
        val accessToken = chat.getAccessToken()
        println(accessToken)
        assert(accessToken.isNotEmpty())
    }

    @Test
    fun testChatWithRobot() {
        val chat = Chat()
        val accessToken = chat.getAccessToken()
        val response = chat.chatWithRobot("你好")
        println(response)
        assert(response.isNotEmpty())
    }

    @Test
    fun testWeather(){
        Weather.getWeather("北京")
    }*/

    val url = "https://jinxuliang.com/public/images/image_01.jpg"
    @Test
    fun testAccessToken(){
        ObjRecognition.generalRecognitionFromUrlTest(url)
    }
}