package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.UserInfo
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(private val repository: QuicktationRepository) : ViewModel(){
    var errorMessage = mutableStateOf("")
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","","",""))
    fun loadQuotes(myUserId: Int) {
        viewModelScope.launch {
            when (val result = repository.postMyProfileApi(myUserId,myUserId)) {
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
}