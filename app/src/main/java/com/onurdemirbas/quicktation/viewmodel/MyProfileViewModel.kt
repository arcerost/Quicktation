package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.QuoteFromMyProfile
import com.onurdemirbas.quicktation.model.UserInfo
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel@Inject constructor(private val repository: QuicktationRepository) : ViewModel() {
    var posts = mutableStateOf(listOf<QuoteFromMyProfile>())
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","","",""))
    var errorMessage by mutableStateOf("")
    var scanIndex = 0
    var likeCount = -1
    private val _ready = MutableLiveData(false)
    val ready: LiveData<Boolean> get() = _ready
    fun setReady(value: Boolean) {
        _ready.value = value
    }
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
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                when( val result = repository.postMyProfileScanApi(myUserId, myUserId, scanIndexx)){
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
    fun likeButtonClicked(quoteId: Int, userId: Int, callback: (Boolean, Int) -> Unit) {
        viewModelScope.launch {
            val result = repository.postLikeApi(userId, quoteId)
            if (result is Resource.Success) {
                val response = result.data
                val likeCheckResponse = response!!.response
                val isLiked = likeCheckResponse.isDeleted == 0
                val newLikeCount = likeCheckResponse.likeCount

                // Update LiveData
                val updatedList = posts.value.toMutableList()
                val index = updatedList.indexOfFirst { it.id == quoteId }
                if (index != -1) {
                    val updatedPost = updatedList[index].copy(amIlike = if (isLiked) 1 else 0, likeCount = newLikeCount)
                    updatedList[index] = updatedPost
                    posts.value = updatedList
                }
                callback(isLiked, newLikeCount)
            }
        }
    }

}