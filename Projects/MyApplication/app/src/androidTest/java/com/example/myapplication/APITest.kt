package com.example.myapplication

import com.example.myapplication.BaiduAI.Chat
import org.junit.Test

class APITest {
    @Test
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
}