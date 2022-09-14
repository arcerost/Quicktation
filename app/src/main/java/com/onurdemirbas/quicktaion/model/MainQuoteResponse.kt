package com.onurdemirbas.quicktaion.model

data class MainQuoteResponse(
    val quotations: List<Quotation>,
    val scanIndex: Int
)