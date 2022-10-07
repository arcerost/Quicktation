package com.onurdemirbas.quicktation.model

data class Sound(
    val amIlike: Int,
    val id: Int,
    val likeCount: Int,
    val quote_id: Int,
    val soundURL: String,
    val userId: Int,
    val username: String,
    val userphoto: String?
)