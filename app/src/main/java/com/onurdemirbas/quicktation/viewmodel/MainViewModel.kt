package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.Quotation
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var errorMessage = mutableStateOf("")
    var scanIndex = mutableStateOf(0)
    var mainList = MutableStateFlow<List<Quotation>>(listOf())
    var isDeleted = mutableStateOf(5)

    init {
        loadMains(1)
    }

    fun loadMains(userid: Int) {
        viewModelScope.launch {
            when (val result = repository.postMainApi(userid)) {
                is Resource.Success -> {
                    mainList.value = result.data!!.response.quotations
                    scanIndex.value = result.data.response.scanIndex
                    errorMessage.value = ""
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    println(errorMessage.value)
                }
            }
        }
    }

    fun amILike(userid: Int, quoteId: Int) {
        viewModelScope.launch {
            when (val result = repository.postLikeApi(userid, quoteId)) {
                is Resource.Success -> {
                    isDeleted.value = result.data!!.response.isDeleted
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    println(errorMessage.value)
                }
            }
        }
    }
}