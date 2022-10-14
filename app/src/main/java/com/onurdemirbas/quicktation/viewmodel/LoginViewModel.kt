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
class LoginViewModel @Inject constructor(private val repository: QuicktationRepo) : ViewModel(){
    var errorMessage = mutableStateOf("")
    var id = mutableStateOf(-1)
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
                id.value = result.data.response
            }
            else if(result.data.error ==1)
            {
                errorMessage.value = result.message!!
            }

        }
    }
}