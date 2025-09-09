package com.example.geminiapp.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenerationRequest(
    val prompt: String
)
