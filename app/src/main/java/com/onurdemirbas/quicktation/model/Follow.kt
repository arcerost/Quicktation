package com.onurdemirbas.quicktation.model

data class Follow(
    val amIFollow: Int,
    val id: Int,
    val toUserId: Int,
    val userId: Int,
    val userPhoto: Any,
    val username: String
)