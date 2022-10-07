package com.onurdemirbas.quicktation.model

data class Follow(
    var amIFollow: Int,
    val id: Int,
    val toUserId: Int,
    val userId: Int,
    val userPhoto: String?,
    val username: String
)