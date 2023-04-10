package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.Follow
import com.onurdemirbas.quicktation.model.QuoteFromMyProfile
import com.onurdemirbas.quicktation.model.UserInfo
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel@Inject constructor(private val repository: QuicktationRepository) : ViewModel() {
    var followerList = MutableStateFlow<List<Follow>>(listOf())
    var errorMessage = mutableStateOf("")
    var scanIndex = MutableStateFlow(0)
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","","",""))
    private val x: String? = null
    var userPhoto = MutableStateFlow(x)
    fun loadFollowers(userId: Int, toUserId: Int, action: String) {
        viewModelScope.launch {
            when(val result = repository.postFollowerApi(userId, toUserId, action))
            {
                is Resource.Success -> {
                    followerList.value = result.data!!.response.followList
                    scanIndex.value = result.data.response.scanIndex
                    errorMessage.value= ""
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                }
            }
            when(val result = repository.postMyProfileApi(toUserId, userId))
            {
                is Resource.Success ->{
                    userPhoto.value = result.data!!.response.userInfo.photo
                    userInfo.value = result.data.response.userInfo
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
        runBlocking {
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