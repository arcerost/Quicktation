package com.onurdemirbas.quicktaion.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktaion.model.MainResponse
import com.onurdemirbas.quicktaion.repository.QuicktationRepo
import com.onurdemirbas.quicktaion.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: QuicktationRepo) : ViewModel() {
    /*
    var cryptoList = mutableStateOf<List<MainResponse>>(listOf())
    var errorMessage = mutableStateOf("")
    lateinit var profilePic: String
    lateinit var voice: String
    lateinit var like: String
    lateinit var texts: String
    lateinit var username: String
    lateinit var likeButton: String
    fun load(userid: String)
    {
        viewModelScope.launch {
            val result = repository.postMainApi(userid)
            if (result.message != null && result.message != "")
            {
                result.message.let {
                    errorMessage.value = it
                }
            }
            else if (result.data!!.error == 0)
            {
                profilePic = result.data.profilePic
                voice = result.data.voice
                like = result.data.like
                likeButton = result.data.likeButton
                username = result.data.username
                texts = result.data.text
                val mainItems =result.data
            }
        }
    }

     */
    var mainList = mutableStateOf<List<MainResponse>>(listOf())
    var errorMessage = mutableStateOf("")
    fun loadMain(userid: String)
    {
        viewModelScope.launch {
            when(val result = repository.getMainList(userid))
            {
                is Resource.Success ->
                {
                    val mainItems =result.data!!.mapIndexed { index, mainResponse -> MainResponse(mainResponse.error,mainResponse.errorText,mainResponse.profilePic,mainResponse.voice,mainResponse.like,mainResponse.text,mainResponse.username,mainResponse.likeButton) }
                    errorMessage.value = ""
                    mainList.value += mainItems
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                }
            }
        }
    }
}