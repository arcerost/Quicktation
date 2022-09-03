package com.onurdemirbas.quicktaion.repository

import com.onurdemirbas.quicktaion.model.*
import com.onurdemirbas.quicktaion.service.ForgotPwApi
import com.onurdemirbas.quicktaion.service.LoginApi
import com.onurdemirbas.quicktaion.service.MainApi
import com.onurdemirbas.quicktaion.service.RegisterApi
import com.onurdemirbas.quicktaion.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.Exception

@ActivityScoped
class QuicktationRepo @Inject constructor(private val api: RegisterApi, private val api2: LoginApi, private val api3: ForgotPwApi, private val api4: MainApi) {
    private lateinit var hatametni: String
    private lateinit var loginError: String
    suspend fun postRegisterApi(
        email: String,
        password: String,
        namesurname: String
    ): Resource<RegisterResponse> {
        val request = Register(email, password, namesurname)
        val response = api.postRegisterApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    hatametni = response.errorText
                    return Resource.Error(hatametni)
                }
                else -> {
                }
            }
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postLoginApi(email: String, password: String): Resource<LoginResponse> {
        val request = Login(email, password)
        val response = api2.postLoginApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postForgotPwApi(email: String): Resource<ForgotPasswordResponse>
    {
        val request = ForgotPassword(email)
        val response = api3.postForgotApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postCheckApi(email: String, code: String): Resource<CheckAuthPwResponse>
    {
        val request = CheckAuthPw(email,code)
        val response = api3.postCheckApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postUpdateApi(email: String, newpassword: String): Resource<UpdatePwResponse>
    {
        val request = UpdatePw(email,newpassword)
        val response = api3.postUpdateApi(request)
        try {
            when (response.error) {
                0 -> {
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }
/*
    suspend fun postMainApi(userid: String): Resource<MainResponse>
    {
        val request = Main(userid)
        val response = api4.postMainApi(request)
        var profilePic: String
        var voice: String
        var like: String
        var text: String
        var username: String
        var likeButton: String
        try {
            when (response.error) {
                0 -> {
                    profilePic = response.profilePic
                    voice = response.voice
                    like = response.like
                    likeButton = response.likeButton
                    username = response.username
                    text = response.text
                }
                1 -> {
                    loginError = response.errorText
                    return Resource.Error(loginError)
                }
                else -> {
                }
            }
        }
        catch(e: Exception)
        {
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }


 */
    suspend fun getMainList(userid: String): Resource<ArrayList<MainResponse>>{
        val response =try{
            api4.postMainApi(Main(userid = userid))
        } catch (e: Exception) {
            return Resource.Error("Error")
        }
        return Resource.Success(response)

    }



}
