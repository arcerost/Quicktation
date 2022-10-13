package com.onurdemirbas.quicktation.model

data class FollowUnFollowUserResponse(
    val error: String,
    val errorText: String,
    val response: FollowUnfollowUserResponseObject
)