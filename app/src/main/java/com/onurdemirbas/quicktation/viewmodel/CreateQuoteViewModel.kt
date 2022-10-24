package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQuoteViewModel @Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var errorMessage = mutableStateOf("")
    var quoteId = mutableStateOf(0)
    fun sendQuote(userId: Int, quote_sound: String, quote_text: String) {
        viewModelScope.launch {
            when(val result = repository.postCreateQuoteApi(userId, quote_sound, quote_text)){
                is Resource.Success ->
                {
                    quoteId.value = result.data!!.response
                }
                is Resource.Error ->
                {
                    errorMessage.value = result.data!!.errorText
                }
            }
        }
    }
}