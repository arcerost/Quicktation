package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.Message
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(private val repository: QuicktationRepository) : ViewModel(){
    var errorMessage = mutableStateOf("")
    var messageList = MutableStateFlow<List<Message>>(listOf())
    fun loadMessageList(myId: Int){
        viewModelScope.launch {
            when (val result = repository.postMessageListApi(myId)){
                is Resource.Success -> {
                    messageList.value = result.data!!.response.messageList
                    errorMessage.value = ""
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                }
            }
        }
    }
}