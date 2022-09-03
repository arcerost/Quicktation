package com.onurdemirbas.quicktaion.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.onurdemirbas.quicktaion.repository.QuicktationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: QuicktationRepo) : ViewModel(){
    var errorMessage = mutableStateOf("")
    fun beLogin(
        email: String,
        password: String,
        navController: NavController
    ) {
        viewModelScope.launch {
            val result = repository.postLoginApi(email, password)
            if (result.message != null && result.message != "")
            {
                result.message.let {
                    errorMessage.value = it
                }
            }
            else if (result.data!!.error == 0)
            {
                navController.navigate("home_page")
            }
            /*
            if(result.data!!.error ==1)
            {
                errorMessage.value = result.message!!
            }
             */
        }
    }
}