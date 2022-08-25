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
class RegisterViewModel @Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var errorMessage = mutableStateOf("")
    fun beRegister(
        email: String,
        password: String,
        namesurname: String,
        navController: NavController
    ) {
        viewModelScope.launch {
            val result = repository.postRegisterApi(email, password, namesurname)
            println("Result mesajı = " + result.message)
            if (result.message != null && result.message != "")
            {
                println("Error Message:  "+ result.message)
                result.message.let {
                    errorMessage.value = it
                }
            }
            else if (result.data!!.error == 0)
            {
                navController.navigate("open_page")
                println("Result Data Error:  "+result.data.error.toString())
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