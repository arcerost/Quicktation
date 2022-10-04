package com.onurdemirbas.quicktation.model

data class LikeSoundResponse(
    val error: String,
    val errorText: String,
    val response: SoundResponse
)