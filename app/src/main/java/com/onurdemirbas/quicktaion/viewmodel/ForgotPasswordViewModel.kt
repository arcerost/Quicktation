package com.onurdemirbas.quicktaion.viewmodel

import android.annotation.SuppressLint
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.onurdemirbas.quicktaion.repository.QuicktationRepo
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