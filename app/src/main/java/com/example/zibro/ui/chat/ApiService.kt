package com.example.zibro.ui.chat
import com.example.zibro.model.GenerateTextRequest
import com.example.zibro.model.GeneratedTextResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/generate")
    fun generateText(@Body request: GenerateTextRequest): Call<GeneratedTextResponse>
}
