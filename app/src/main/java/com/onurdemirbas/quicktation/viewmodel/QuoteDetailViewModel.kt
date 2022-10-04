package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.*
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteDetailViewModel@Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var likeCount = MutableStateFlow(-1)
    var isDeleted = MutableStateFlow(-1)
    var quoteIdx = MutableStateFlow(-1)
    var head = MutableStateFlow(QuoteDetailResponseRowList(amIlike = 1,"",1,1,"","",1,1,"",""))
    var errorMessage = mutableStateOf("")
    var scanIndex = MutableStateFlow(0)
    var soundList = MutableStateFlow<List<Sound>>(listOf())
    var soundIdx = MutableStateFlow(-1)
    var isDeletedSound = MutableStateFlow(-1)
    var likeCountSound = MutableStateFlow(-1)
    fun loadQuote(userId: Int, quoteId: Int)
    {
        viewModelScope.launch {
            when (val result = repository.postQuoteDetailApi(userId, quoteId)) {
                is Resource.Success -> {
                    head.value = result.data!!.response.quoteDetail
                    scanIndex.value = result.data.response.scanIndex
                    soundList.value = result.data.response.soundList
                    errorMessage.value = ""
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    println(errorMessage.value)
                }
            }
        }
    }

    //tekrarlanan fonksiyon
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

    fun amILikeSound(userId: Int, quotesound_id: Int)
    {
        viewModelScope.launch {
            when(val result = repository.postLikeSoundApi(userId, quotesound_id))
            {
                is Resource.Success ->{
                    isDeletedSound.value = result.data!!.response.isDeleted
                    likeCountSound.value = result.data.response.likeCount
                    soundIdx.value = result.data.response.quotesound_id
                }
                is Resource.Error ->{
                    errorMessage.value = result.message!!
                    println(errorMessage.value)
                }
            }
        }
    }

//    fun loadMainScans(userid: Int,scanIndexx: Int) {
//        viewModelScope.launch {
//            when (val result = repository.postMainScanApi(userid,scanIndexx)) {
//                is Resource.Success -> {
//                    mainList.value += result.data!!.response.quotations
//                    scanIndex.value = result.data.response.scanIndex
//                    errorMessage.value = ""
//                }
//                is Resource.Error -> {
//                    errorMessage.value = result.message!!
//                    println(errorMessage.value)
//                }
//            }
//        }
//    }
}