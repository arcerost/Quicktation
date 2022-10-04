package com.onurdemirbas.quicktation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.Follow
import com.onurdemirbas.quicktation.repository.QuicktationRepo
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel@Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    var followerList = MutableStateFlow<List<Follow>>(listOf())
    var errorMessage = mutableStateOf("")
    var scanIndex = MutableStateFlow(0)

    init {
        loadFollowers(1,4,"followers")
    }
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
        }
    }
}