package com.onurdemirbas.quicktation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val repository: QuicktationRepository): ViewModel(){
    val errorMessage = MutableStateFlow("")
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