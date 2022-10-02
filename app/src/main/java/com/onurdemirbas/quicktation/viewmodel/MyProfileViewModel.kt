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
class MyProfileViewModel@Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var likeCount = MutableStateFlow(-1)
    var isDeleted = MutableStateFlow(-1)
    var quoteIdx = MutableStateFlow(-1)
    var posts = MutableStateFlow<List<QuoteFromMyProfile>>(listOf())
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","",""))
    var errorMessage = mutableStateOf("")
    var scanIndex = MutableStateFlow(0)

    init {
        loadQuotes(4,1)
    }
    private fun loadQuotes(userid: Int, myUserId: Int) {
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
                    println(errorMessage.value)
                }
            }
        }
    }
    fun amILikeFun(userid: Int, quoteId: Int) {
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
}