package com.example.geminiapp.data.remote

import com.example.geminiapp.data.remote.dto.GenerationRequest
import com.example.geminiapp.data.remote.dto.GenerationResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("v1/generate")
    suspend fun generate(
        @Header("Authorization") authorization: String,
        @Body request: GenerationRequest
    ): GenerationResponse
}
