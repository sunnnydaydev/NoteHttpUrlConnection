# 原生网络访问

网络在安卓中是一个不小的模块，各种网络框架也是层出不穷，庆幸我们生活在了一个好年代使用okHttp就可以轻松的访问网络。虽然“框架”更加方便但是原生的网络访问我们还是有必要“了解”下的。

安卓上原生访问网络一般有两种方式：HttpUrlConnection和HttpClient。

# HttpClient

安卓原生网络访问框架之一，由于这个框架存在API数量过多、扩展困难等缺点，在安卓6.0系统中API被移除。

# HttpUrlConnection

 Android 2.2版本及其之前 ,HttpUrlConnection 还是存在一些bug的，比如某些情况下连接池失效问题，这时使用HttpClient是较好的选择 。

安卓2.3开始 HttpURLConnection 做了优化，优点就显现出来了如： API简单，体积较小，非常适用于Android项目。压缩和缓存机制可以有效地减少网络访问的流量，在提升速度和省电方面也起到了较大的作用 。

安卓6.0开始HttpClient 被从安卓系统API类库中剔除，这时原生访问网络时HttpUrlConnection就成了唯一选择。

# HttpUrlConnection 

直接上栗子吧！！！

```java

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
```





