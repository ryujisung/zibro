package com.example.zibro.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.zibro.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class DasfdasfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url = "http://127.0.0.1:8000/generate"  // FastAPI API의 URL로 변경
        val data = JSONObject().apply {
            put("prompt", "Your prompt text here")
            put("max_length", 50)
        }

        val client = OkHttpClient()

        val body = data.toString().toRequestBody("application/json".toMediaType())
        Log.d("MainActivity","시작")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

                val response = client.newCall(request).execute()
                Log.d("MainActivity","dddd")
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody!!)
                    val generatedText = jsonResponse.getString("generated_text")
                    Log.d("MainActivity","통신성공")
                    Log.d("MainActivity", "Generated Text: $generatedText")
                    // 여기서 생성된 텍스트를 UI에 표시하거나 다른 처리를 수행합니다.
                } else {
                    Log.d("MainActivity","통신실패2")
                    Log.e("MainActivity", "Error: ${response.code} ${response.message}")
                }
            } catch (e: IOException) {
                Log.d("MainActivity","통신실패1")
                e.printStackTrace()
            }
        }
    }
}