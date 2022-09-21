package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val repository: QuicktationRepo): ViewModel(){
    var errorMessage = mutableStateOf("")


    fun forgotPassword(
        email: String
    ) {
         viewModelScope.launch{
            val result = repository.postForgotPwApi(email)
            if (result.message != null && result.message != "")
            {
                result.message.let {
                    errorMessage.value = it
                }
            }
            else if (result.data!!.error == 0)
            {
                println("post forgot sorunsuz")
            }
        }
    }


    fun checkCode(
        email: String,
        code: String
    ) {
        viewModelScope.launch {
            val result = repository.postCheckApi(email,code)
            if (result.message != null && result.message != "")
            {
                result.message.let {
                    errorMessage.value = it
                }
            }
            else if (result.data!!.error == 0)
            {
                println("post check sorunsuz")
            }
        }
    }


    fun updatePassword(
        email: String,
        newpassword: String,
        navController: NavController
    ) {
        viewModelScope.launch {
            val result = repository.postUpdateApi(email,newpassword)
            if (result.message != null && result.message != "")
            {
                result.message.let {
                    errorMessage.value = it
                }
            }
            else if (result.data!!.error == 0)
            {
                navController.navigate("open_page")
            }
        }
    }
}