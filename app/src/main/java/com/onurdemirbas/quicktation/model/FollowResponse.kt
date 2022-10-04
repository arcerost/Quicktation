package com.onurdemirbas.quicktation.model

data class FollowResponse(
    val error: String,
    val errorText: String,
    val response: FollowResponseList
)