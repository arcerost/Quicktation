package com.onurdemirbas.quicktation.model

data class SearchQuoteResponse(
    val error: String,
    val errorText: String,
    val response: SearchQuoteResponseList
)