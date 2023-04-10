package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.*
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuoteDetailViewModel@Inject constructor(private val repository: QuicktationRepository) : ViewModel() {
    var head = MutableStateFlow(QuoteDetailResponseRowList(amIlike = 1,"",1,1,"","",1,1,"",""))
    var errorMessage by mutableStateOf("")
    var scanIndex by mutableStateOf(0)
    var soundList = mutableStateOf(listOf<Sound>())
    var isDeletedSound = -1
    var likeCountSound = -1
    var isDeleted = -1
    var likeCount = -1
    fun loadQuote(userId: Int, quoteId: Int)
    {
        viewModelScope.launch {
            when (val result = repository.postQuoteDetailApi(userId, quoteId)) {
                is Resource.Success -> {
                    head.value = result.data!!.response.quoteDetail
                    soundList.value = result.data.response.soundList
                    scanIndex = result.data.response.scanIndex
                    errorMessage = ""
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                    println(errorMessage)
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


    fun loadQuoteScans(userId: Int, quoteId: Int ,scanIndexx: Int) {
        runBlocking {
            withContext(Dispatchers.IO){
                when (val result = repository.postQuoteDetailScanApi(userId, quoteId, scanIndexx)) {
                    is Resource.Success -> {
                        head.value = result.data!!.response.quoteDetail
                        soundList.value += result.data.response.soundList
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

    fun amILikeSound(myId: Int, quotesound_id: Int)
    {
        viewModelScope.launch {
            when(val result = repository.postLikeSoundApi(myId, quotesound_id))
            {
                is Resource.Success ->{
                    isDeletedSound = result.data!!.response.isDeleted
                    likeCountSound = result.data.response.likeCount
                }
                is Resource.Error ->{
                    errorMessage = result.message!!
                }
            }
        }
    }
}