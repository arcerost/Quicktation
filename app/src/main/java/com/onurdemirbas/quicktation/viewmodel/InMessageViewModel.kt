package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.UserInfo
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InMessageViewModel @Inject constructor(private val repository: QuicktationRepository) : ViewModel() {
    var errorMessage = mutableStateOf("")
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","","",""))
    fun loadMessages(myId: Int){
        viewModelScope.launch {

        }
    }
}