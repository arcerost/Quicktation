package com.onurdemirbas.quicktation.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences

class StoreUserInfo(private val context: Context) {

    companion object{
        private val Context.util: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore("UserEmail")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val USER_PASSWORD_KEY = stringPreferencesKey("password")
        val USER_ID_KEY = intPreferencesKey("id")
    }

    val getEmail: Flow<String?> = context.util.data
        .map {  preferences ->
            preferences[USER_EMAIL_KEY] ?:  ""
    }

    val getPassword: Flow<String?> = context.util.data.map {
        it[USER_PASSWORD_KEY] ?: ""
    }

    val getId: Flow<Int?> = context.util.data.map {
        it[USER_ID_KEY] ?: -1
    }

    suspend fun saveEmail(email: String) {
        context.util.edit { pref ->
            pref[USER_EMAIL_KEY] = email
        }
    }

    suspend fun savePassword(password: String)
    {
        context.util.edit { pref ->
            pref[USER_PASSWORD_KEY] = password
        }
    }
    suspend fun saveId(id: Int)
    {
        context.util.edit { pref ->
            pref[USER_ID_KEY] = id
        }
    }
}