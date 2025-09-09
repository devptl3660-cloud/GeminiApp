package com.example.geminiapp.data

import com.example.geminiapp.data.remote.ApiService
import com.example.geminiapp.data.remote.dto.GenerationRequest
import com.example.geminiapp.data.remote.dto.GenerationResponse
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RemoteRepository(private val api: ApiService) {
    companion object {
        private const val BASE_URL = "https://your-backend.example.com/"
        private const val API_KEY = "YOUR_API_KEY"

        fun create(): RemoteRepository {
            val moshi = Moshi.Builder().build()
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(client)
                .build()
            val api = retrofit.create(ApiService::class.java)
            return RemoteRepository(api)
        }
    }

    suspend fun generateText(prompt: String): String? {
        val req = GenerationRequest(prompt = prompt)
        val authHeader = "Bearer $API_KEY"
        val resp: GenerationResponse = api.generate(authHeader, req)
        return resp.text
    }
}
