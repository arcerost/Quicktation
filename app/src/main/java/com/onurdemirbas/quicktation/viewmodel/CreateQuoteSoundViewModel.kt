package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.UserInfo
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQuoteSoundViewModel @Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var errorMessage = mutableStateOf("")
    private var answer = mutableStateOf("")
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","",""))
    fun getPhoto(postUserId: Int, myUserId: Int){
        viewModelScope.launch {
            when (val result = repository.postMyProfileApi(postUserId,myUserId)) {
                is Resource.Success -> {
                    userInfo.value = result.data!!.response.userInfo
                    errorMessage.value = ""
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                }
            }
        }
    }
    fun sendQuoteSound(userId: Int, quoteSound: String, quoteId: Int) {
        viewModelScope.launch {
            when(val result = repository.postCreateQuoteSoundApi(userId, quoteSound, quoteId)){
                is Resource.Success ->
                {
                    answer.value = result.data!!.error
                }
                is Resource.Error ->
                {
                    errorMessage.value = result.data!!.errorText
                }
            }
        }
    }
}