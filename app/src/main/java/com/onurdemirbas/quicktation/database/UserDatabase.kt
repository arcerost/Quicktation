package com.onurdemirbas.quicktation.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserInfo::class], version = 3)
abstract class UserDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao
}