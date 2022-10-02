package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.Quotation
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var errorMessage = mutableStateOf("")
    var scanIndex = MutableStateFlow(0)
    var mainList = MutableStateFlow<List<Quotation>>(listOf())
    var likeCount = MutableStateFlow(-1)
    var isDeleted = MutableStateFlow(-1)
    var quoteIdx = MutableStateFlow(-1)

    init {
        loadMains(1)
    }


    private fun loadMains(userid: Int) {
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
                    likeCount.value = result.data.response.likeCount
                    quoteIdx.value = result.data.response.quoteId
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    println(errorMessage.value)
                }
            }

        }
    }

    fun loadMainScans(userid: Int,scanIndexx: Int) {
        viewModelScope.launch {
            when (val result = repository.postMainScanApi(userid,scanIndexx)) {
                is Resource.Success -> {
                    mainList.value += result.data!!.response.quotations
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
}