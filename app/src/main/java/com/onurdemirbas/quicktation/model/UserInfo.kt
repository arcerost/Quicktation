package com.onurdemirbas.quicktation.model

data class UserInfo(
    val amIfollow: Int,
    val createDate: String,
    val email: String,
    val followCount: Int,
    val followerCount: Int,
    val id: Int,
    val likeCount: Int,
    val namesurname: String,
    val password: String,
    val photo: String?
)