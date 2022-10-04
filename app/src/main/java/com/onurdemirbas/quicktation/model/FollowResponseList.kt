package com.onurdemirbas.quicktation.model

data class FollowResponseList(
    val followList: List<Follow>,
    val scanIndex: Int
)