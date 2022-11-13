package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.QuoteFromMyProfile
import com.onurdemirbas.quicktation.model.UserInfo
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel@Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var posts = mutableStateOf(listOf<QuoteFromMyProfile>())
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","","",""))
    var errorMessage by mutableStateOf("")
    var scanIndex = 0
    var isDeleted = -1
    var likeCount = -1
    fun loadQuotes(myUserId: Int) {
        viewModelScope.launch {
            when (val result = repository.postMyProfileApi(myUserId,myUserId)) {
                is Resource.Success -> {
                    posts.value = result.data!!.response.quotations
                    scanIndex = result.data.response.scanIndex
                    userInfo.value = result.data.response.userInfo
                    errorMessage = ""
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                }
            }
        }
    }
    fun loadMyProfileScans(myUserId: Int, scanIndexx: Int) {
        runBlocking {
            withContext(Dispatchers.IO){
                    when (val result = repository.postMyProfileScanApi(myUserId, myUserId, scanIndexx)) {
                        is Resource.Success -> {
                            posts.value += result.data!!.response.quotations
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
    var response = mutableStateOf("")
    fun deleteQuote(userid: Int, quoteId: Int)
    {
        viewModelScope.launch {
            when(val result = repository.postDeleteQuoteApi(userid,quoteId)){
                is Resource.Success -> {
                    response.value = result.data!!.error
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                }
            }
        }
    }

}