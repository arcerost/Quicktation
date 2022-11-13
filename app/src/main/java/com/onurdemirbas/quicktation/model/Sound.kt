package com.onurdemirbas.quicktation.model

data class Sound(
    var amIlike: Int,
    val id: Int,
    var likeCount: Int,
    val quote_id: Int,
    val soundURL: String,
    val userId: Int,
    val username: String,
    val userphoto: String?
)