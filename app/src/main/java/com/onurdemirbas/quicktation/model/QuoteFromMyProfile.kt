package com.onurdemirbas.quicktation.model

data class QuoteFromMyProfile(
    val amIlike: Int,
    val createDate: String,
    val id: Int,
    val likeCount: Int,
    val quote_text: String,
    val quote_url: String,
    val stat: Int,
    val userId: Int,
    val username: String,
    val userphoto: String?
)