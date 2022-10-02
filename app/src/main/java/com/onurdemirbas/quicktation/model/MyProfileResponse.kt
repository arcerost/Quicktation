package com.onurdemirbas.quicktation.model

data class MyProfileResponse(
    val error: String,
    val errorText: String,
    val response: MyProfileQuoteResponse
)