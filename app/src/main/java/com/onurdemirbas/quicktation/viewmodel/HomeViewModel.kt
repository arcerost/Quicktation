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
                }
            }
        }
    }
    fun amILike(userid: Int, quoteId: Int) {
        viewModelScope.launch {
                when (val result = repository.postLikeApi(userid, quoteId)) {
                    is Resource.Success -> {
                        likeCount.value = result.data!!.response.likeCount
//                        mainList.value[quoteId-1].likeCount = isDeleted.value
//                        mainList.value[quoteId-1].amIlike = likeCount.value
                    }
                    is Resource.Error -> {
                        errorMessage.value = result.message!!
                    }
                }
        }
    }

    fun loadMainScans(userid: Int,scanIndexx: Int) {
        viewModelScope.launch {
            withContext(coroutineContext){
                when (val result = repository.postMainScanApi(userid,scanIndexx)) {
                    is Resource.Success -> {
                        mainList.value += result.data!!.response.quotations
                        scanIndex.value = result.data.response.scanIndex
                        errorMessage.value = ""
                    }
                    is Resource.Error -> {
                        errorMessage.value = result.message!!
                    }
                }
            }
        }
    }
    private var isSearchStarting = true
    private var initialMainList = listOf<Quotation>()
    fun searchMainList(query: String) {
        val listToSearch = if(isSearchStarting) {
            mainList.value
        } else {
            initialMainList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()) {
                mainList.value = initialMainList
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.quote_text.contains(query.trim(), ignoreCase = true)
            }
            if(isSearchStarting) {
                initialMainList = mainList.value
                isSearchStarting = false
            }
            mainList.value = results
        }
    }
}