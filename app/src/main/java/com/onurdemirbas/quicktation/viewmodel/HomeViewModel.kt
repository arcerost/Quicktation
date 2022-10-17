package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var errorMessage by mutableStateOf("")
    var scanIndex by mutableStateOf(0)
    var mainList = MutableStateFlow<List<Quotation>>(listOf())
    var likeCount = -1

    fun loadMains(userid: Int) {
        viewModelScope.launch {
            when (val result = repository.postMainApi(userid)) {
                is Resource.Success -> {
                    mainList.value = result.data!!.response.quotations
                    scanIndex = result.data.response.scanIndex
                    errorMessage = ""
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                }
            }
        }
    }
    fun amILike(userid: Int, quoteId: Int) {
        viewModelScope.launch {
                when (val result = repository.postLikeApi(userid, quoteId)) {
                    is Resource.Success -> {
                        likeCount = result.data!!.response.likeCount
                    }
                    is Resource.Error -> {
                        errorMessage = result.message!!
                    }
                }
        }
    }
    private var saveIndex = 0
    fun loadMainScans(userid: Int,scanIndexx: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                when (val result = repository.postMainScanApi(userid,scanIndexx)) {
                    is Resource.Success -> {
                        mainList.value += result.data!!.response.quotations
                        scanIndex = result.data.response.scanIndex
                        errorMessage = ""
                    }
                    is Resource.Error -> {
                        errorMessage = result.message!!
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