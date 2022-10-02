package com.onurdemirbas.quicktation.model

data class MyProfileQuoteResponse(
    val quotations: List<QuoteFromMyProfile>,
    val scanIndex: Int,
    val userInfo: UserInfo
)