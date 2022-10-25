package com.onurdemirbas.quicktation.model

data class SearchQuoteResponseList(
    val quotations: List<Quotation>,
    val scanIndex: Int
)