package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.Quotation
import com.onurdemirbas.quicktation.model.QuoteDetail
import com.onurdemirbas.quicktation.model.QuoteDetailResponseRowList
import com.onurdemirbas.quicktation.model.Sound
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import com.onurdemirbas.quicktation.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuoteDetailViewModel@Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var errorMessage = mutableStateOf("")
    var scanIndex = MutableStateFlow(0)
    var mainList = MutableStateFlow<List<QuoteDetailResponseRowList>>(listOf())
    var soundList = MutableStateFlow<List<Sound>>(listOf())
    init {
        loadQuotes(1,1)
    }
    fun loadQuotes(userId: Int, quoteId: Int)
    {
        viewModelScope.launch {
            when (val result = repository.postQuoteDetailApi(userId, quoteId)) {
                is Resource.Success -> {
                    mainList.value = result.data!!.response.quoteDetail
                    scanIndex.value = result.data.response.scanIndex
                    soundList.value = result.data.response.soundList
                    errorMessage.value = ""
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    println(errorMessage.value)
                }
            }
        }
    }
}