package com.onurdemirbas.quicktation.model

data class UserInfo(
    var amIfollow: Int,
    var createDate: String,
    var email: String,
    var followCount: Int,
    var followerCount: Int,
    var id: Int,
    var likeCount: Int,
    var namesurname: String,
    val password: String,
    var photo: String?
)