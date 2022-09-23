package com.onurdemirbas.quicktation.model

data class HomeQuoteResponse(
    val quotations: List<Quotation>,
    val scanIndex: Int
)