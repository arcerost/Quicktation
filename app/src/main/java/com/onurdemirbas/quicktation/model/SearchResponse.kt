package com.onurdemirbas.quicktation.model

data class SearchResponse(
    val error: String,
    val errorText: String,
    val response: SearchResponseList
)