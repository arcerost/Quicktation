package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: QuicktationRepository) : ViewModel() {
    val errorMessage = MutableStateFlow("")
    fun beRegister(
        email: String,
        password: String,
        namesurname: String,
        username: String,
        navController: NavController
    ) {
        viewModelScope.launch {
            val result = repository.postRegisterApi(email, password, namesurname, username)
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