package com.onurdemirbas.quicktation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfo(
    @ColumnInfo(name = "userId") var userId: Int?,
    @ColumnInfo(name = "userEmail") val userEmail: String?,
    @ColumnInfo(name = "userPassword") val userPassword: String?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)