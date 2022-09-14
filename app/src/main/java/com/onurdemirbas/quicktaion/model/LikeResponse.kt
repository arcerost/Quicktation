package com.onurdemirbas.quicktaion.model

data class LikeResponse(
    val error: String,
    val errorText: String,
    val response: LikeCheckResponse
)