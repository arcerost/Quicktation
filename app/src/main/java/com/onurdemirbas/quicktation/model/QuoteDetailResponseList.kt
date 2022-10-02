package com.onurdemirbas.quicktation.model

data class QuoteDetailResponseList(
    val quoteDetail: QuoteDetailResponseRowList,
    val scanIndex: Int,
    val soundList: List<Sound>
)