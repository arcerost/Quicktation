package com.onurdemirbas.quicktation.model

data class HomeResponse(
    val error: Int,
    val errorText: String,
    val response: HomeQuoteResponse
)