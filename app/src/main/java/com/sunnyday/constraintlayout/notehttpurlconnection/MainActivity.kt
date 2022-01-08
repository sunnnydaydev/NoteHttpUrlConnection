package com.sunnyday.constraintlayout.notehttpurlconnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    companion object {
        // 请求方法
        const val GET = "GET"
        const val POST = "POST"
        // 默认超时。单位毫秒。
        const val DEFAULT_TIME_OUT = 5000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            sendRequest2Sever()
        }
    }

    private fun sendRequest2Sever() {
        // 子线程中访问网络。
        thread {
            //获取HttpURLConnection 对象。
            var connection: HttpURLConnection? = null
            try {
                val url = URL("https://www.baidu.com")
                connection = url.openConnection() as HttpURLConnection
                // 设置超时
                connection.connectTimeout = DEFAULT_TIME_OUT
                connection.readTimeout = DEFAULT_TIME_OUT
                //设置请求方式（GET 默认）
                connection.requestMethod = GET
                // 获取inputStream
                val ins = connection.inputStream
                val sb = StringBuilder()
                val reader = BufferedReader(InputStreamReader(ins))
                reader.use {
                    reader.forEachLine { //依次遍历reader中的每行String
                        sb.append(it)
                    }
                }
                updateUI(sb.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
        }
    }

    private fun updateUI(str: String) {
        runOnUiThread {
            tvText.text = str
        }
    }
}