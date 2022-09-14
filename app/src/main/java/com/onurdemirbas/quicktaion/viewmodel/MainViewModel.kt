package com.onurdemirbas.quicktaion.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.core.graphics.component1
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktaion.model.MainResponse
import com.onurdemirbas.quicktaion.model.Quotation
import com.onurdemirbas.quicktaion.repository.QuicktationRepo
import com.onurdemirbas.quicktaion.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var errorMessage = mutableStateOf("")
    var scanIndex = mutableStateOf(0)
    var item = mutableStateOf(0)
    var mainList2 = mutableStateOf<List<Quotation>>(listOf())
    init {
        loadMains(1)
    }
    fun loadMains(userid: Int)
    {
        viewModelScope.launch {
            when(val result = repository.postMainApi(userid))
            {
                is Resource.Success ->
                {
                    mainList2.value = result.data!!.response.quotations.toMutableStateList()
                    scanIndex.value = result.data.response.scanIndex
                    item.value = result.data.response.quotations.size
                    errorMessage.value = ""
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    println(errorMessage.value)
                }
            }
        }
    }
    var isDeleted = mutableStateOf( 2)
    fun amILike(userid: Int,quoteId: Int)
    {
        viewModelScope.launch {
            val a = async {
                when (val result = repository.postLikeApi(userid, quoteId)) {
                    is Resource.Success -> {
                        isDeleted.value = result.data!!.response.isDeleted
                        errorMessage.value = ""
                    }
                    is Resource.Error -> {
                        errorMessage.value = result.message!!
                        println(errorMessage.value)
                    }
                }
            }
            a.await()
        }
    }
}