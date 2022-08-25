package com.onurdemirbas.quicktaion.repository

import com.onurdemirbas.quicktaion.model.Register
import com.onurdemirbas.quicktaion.model.RegisterResponse
import com.onurdemirbas.quicktaion.service.RegisterApi
import com.onurdemirbas.quicktaion.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.Exception

@ActivityScoped
class QuicktationRepo @Inject constructor(private val api: RegisterApi) {
    private lateinit var hatametni: String
    suspend fun postRegisterApi(email: String, password: String, namesurname: String): Resource<RegisterResponse> {
        val request = Register(email, password, namesurname)
        val response = api.postRegisterApi(request)
        try {
            when (response.error) {
                0 -> {
                    println("value 0")
                }
                1 -> {
                    println("value 1")
                    hatametni = response.errorText
                    return Resource.Error(hatametni)
                }
                else -> {
                    println("Values is not 0-1")
                }
            }
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
        println("Error: "+response.error.toString()+" //   Response: " +response.errorText)
        return Resource.Success(response)
    }
}