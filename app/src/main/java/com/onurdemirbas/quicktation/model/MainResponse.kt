package com.onurdemirbas.quicktation.model

data class MainResponse(
    val error: Int,
    val errorText: String,
    val response: MainQuoteResponse
)