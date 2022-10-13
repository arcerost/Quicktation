package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.QuoteFromMyProfile
import com.onurdemirbas.quicktation.model.UserInfo
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class OtherProfileViewModel@Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var likeCount = MutableStateFlow(-1)
    var posts = MutableStateFlow<List<QuoteFromMyProfile>>(listOf())
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","",""))
    var errorMessage = mutableStateOf("")
    var scanIndex = MutableStateFlow(0)
    fun loadQuotes(userid: Int, myUserId: Int) {
        viewModelScope.launch {
            when (val result = repository.postMyProfileApi(userid,myUserId)) {
                is Resource.Success -> {
                    posts.value = result.data!!.response.quotations
                    scanIndex.value = result.data.response.scanIndex
                    userInfo.value = result.data.response.userInfo
                    errorMessage.value = ""
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                }
            }
        }
    }
    fun amILikeFun(userid: Int, quoteId: Int) {
        viewModelScope.launch {
            when (val result = repository.postLikeApi(userid, quoteId)) {
                is Resource.Success -> {
                    likeCount.value = result.data!!.response.likeCount
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
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
                    errorMessage.value = result.message!!
                }
            }
        }
    }

    //Follow-Unfollow User
    val amIFollow = MutableStateFlow(-1)
    fun followUnFollowUser(userid: Int, toUserId: Int)
    {
        viewModelScope.launch {
            when(val result = repository.postFollowUnfollowUserApi(userid,toUserId)){
                is Resource.Success -> {
                    amIFollow.value = result.data!!.response.amIfollow
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                }
            }
        }
    }
}