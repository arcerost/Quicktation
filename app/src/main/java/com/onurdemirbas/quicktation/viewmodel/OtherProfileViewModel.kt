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

class OtherProfileViewModel@Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var posts = mutableStateOf(listOf<QuoteFromMyProfile>())
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","","",""))
    var errorMessage by mutableStateOf("")
    var scanIndex by mutableStateOf(0)
    var isDeleted = -1
    var likeCount = -1
    fun loadQuotes(userid: Int, myUserId: Int) {
        viewModelScope.launch {
            when (val result = repository.postMyProfileApi(userid,myUserId)) {
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
    fun loadQuoteScans(userId: Int, myUserId: Int, scanIndexx: Int) {
        runBlocking {
            withContext(Dispatchers.IO){
                when (val result = repository.postMyProfileScanApi(userId, myUserId, scanIndexx)) {
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
    fun amILikeFun(userid: Int, quoteId: Int) {
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

    //REPORT USER
    var response = mutableStateOf("")
    fun reportUser(userid: Int, toUserId: Int, reason: String)
    {
        viewModelScope.launch {
            when(val result = repository.postReportUserApi(userid,toUserId, reason)){
                is Resource.Success -> {
                    response.value = result.data!!.error
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                }
            }
        }
    }

    //Follow-Unfollow User
    val amIFollow = MutableStateFlow(-1)
    fun followUnFollowUser(userid: Int, toUserId: Int)
    {
        runBlocking {
            when(val result = repository.postFollowUnfollowUserApi(userid,toUserId)){
                is Resource.Success -> {
                    amIFollow.value = result.data!!.response.amIfollow
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                }
            }
        }
    }
}