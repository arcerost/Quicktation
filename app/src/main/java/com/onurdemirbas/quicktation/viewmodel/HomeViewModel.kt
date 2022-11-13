package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.onurdemirbas.quicktation.model.Quotation
import com.onurdemirbas.quicktation.model.User
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var errorMessage by mutableStateOf("")
    var scanIndex by mutableStateOf(0)
    var user = MutableStateFlow<List<User>>(listOf())
    var quotes = MutableStateFlow<List<Quotation>>(listOf())
    var mainList = mutableStateOf(listOf<Quotation>())
    var isDeleted = -1
    var likeCount = -1

    fun loadMains(userid: Int) {
        runBlocking {
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
        runBlocking {
            when (val result = repository.postLikeApi(userid, quoteId)) {
                is Resource.Success -> {
                    likeCount = result.data!!.response.likeCount
                    isDeleted = result.data.response.isDeleted
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                }
            }
        }
    }
    fun loadMainScans(userid: Int,scanIndexx: Int) {
        runBlocking {
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
    fun search(userId: Int, action: String, searchKey: String, scanIndex: Int){
        runBlocking {
            when(val result = repository.postSearchApi(userId, action, searchKey, scanIndex)){
                is Resource.Success -> {
                    user.value = result.data!!.response.users
                    errorMessage = ""
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                }
            }
        }
    }
    fun searchQuote(userId: Int, action: String, searchKey: String, scanIndexx: Int){
        runBlocking {
            when(val result = repository.postSearchQuoteApi(userId, action, searchKey, scanIndexx)){
                is Resource.Success -> {
                    quotes.value = result.data!!.response.quotations
                    scanIndex = result.data.response.scanIndex
                    errorMessage = ""
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                }
            }
        }
    }
    fun loadSearchScans(userId: Int, action: String, searchKey: String, scanIndexx: Int)
    {
        runBlocking {
            withContext(Dispatchers.IO){
                when (val result = repository.postSearchQuoteApi(userId, action, searchKey, scanIndexx)) {
                    is Resource.Success -> {
                        quotes.value += result.data!!.response.quotations
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
}