package com.onurdemirbas.quicktation.model

data class QuoteDetailResponse(
    val error: String,
    val errorText: String,
    val response: QuoteDetailResponseList
)