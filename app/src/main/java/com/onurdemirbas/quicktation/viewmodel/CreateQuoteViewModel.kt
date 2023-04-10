package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQuoteViewModel @Inject constructor(private val repository: QuicktationRepository) : ViewModel() {
    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> get() = _errorMessage
    private val _quoteId = mutableStateOf(0)
    val quoteId: State<Int> get() = _quoteId
    fun sendQuote(userId: Int, quote_sound: String, quote_text: String) {
        viewModelScope.launch {
            when(val result = repository.postCreateQuoteApi(userId, quote_sound, quote_text)){
                is Resource.Success ->
                {
                    _quoteId.value = result.data?.response ?: 0
                }
                is Resource.Error ->
                {
                    _errorMessage.value = result.data?.errorText ?: "Bir hata oluştu."
                }
            }
        }
    }
}