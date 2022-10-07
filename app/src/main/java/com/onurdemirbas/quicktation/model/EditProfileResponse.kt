package com.onurdemirbas.quicktation.model

data class EditProfileResponse(
    val error: String,
    val errorText: String,
    val response: EditProfileEditResponse
)