package com.onurdemirbas.quicktation.model

data class User(
    val createDate: String,
    val email: String,
    val id: Int,
    val namesurname: String,
    val password: String,
    val photo: String,
    val username: String
)