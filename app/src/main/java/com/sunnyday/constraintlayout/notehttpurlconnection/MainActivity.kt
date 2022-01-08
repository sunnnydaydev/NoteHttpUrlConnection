package com.sunnyday.constraintlayout.notehttpurlconnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.DataOutputStream
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

    /**
     * 以get方式访问百度，获取请求返回的String。
     * */
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
                // 以字符流方式处理数据（若网络返回的数据为文件我们可以以字节流处理）
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

    /**
     * Post 请求上传key-value。
     * ps：
     * 1、注意每对key-value之间使用&分隔。
     * 2、这里简单举个例子，如需要很多键值对上传，这里方法可以定个map
     * 参数，然后遍历拼接。
     */
    private fun postTest(connect:HttpURLConnection){
        connect.requestMethod = POST
        val ops = DataOutputStream(connect.outputStream)
        ops.write("userName=徐凤年&tvPlay=雪中悍刀行".toByteArray())
    }
}