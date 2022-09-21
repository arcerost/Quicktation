package com.onurdemirbas.quicktation.model

data class MainQuoteResponse(
    val quotations: List<Quotation>,
    val scanIndex: Int
)