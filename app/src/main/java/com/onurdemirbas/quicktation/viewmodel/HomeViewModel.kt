package com.onurdemirbas.quicktation.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.onurdemirbas.quicktation.database.UserDatabase
import com.onurdemirbas.quicktation.model.Quotation
import com.onurdemirbas.quicktation.model.User
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: QuicktationRepository, @ApplicationContext context: Context) : ViewModel() {
    private val db: UserDatabase = Room.databaseBuilder(context, UserDatabase::class.java, "UserInfo").build()
    private val userDao = db.UserDao()
    var errorMessage by mutableStateOf("")
    var scanIndex by mutableStateOf(0)
    var user = MutableStateFlow<List<User>>(listOf())
    var quotes = MutableStateFlow<List<Quotation>>(listOf())
    var mainList = mutableStateOf(listOf<Quotation>())
    var isDeleted = -1
    var likeCount = -1
    private val _ready = MutableLiveData(false)
    val ready: LiveData<Boolean> get() = _ready
    fun setReady(value: Boolean) {
        _ready.value = value
    }
    suspend fun loadMains(userid: Int) {
        when(val result = repository.postMainApi(userid)){
            is Resource.Success -> {
                mainList.value = result.data!!.response.quotations
                scanIndex = result.data.response.scanIndex
                errorMessage = ""
            }
            is Resource.Error -> {
                errorMessage = result.message!!
            }
        }
    }
    fun amILike(userid: Int, quoteId: Int) {
        viewModelScope.launch {
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
    fun loadMainScans(userid: Int,scanIndexx: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                when (val result = repository.postMainApi(userid,scanIndexx)) {
                    is Resource.Success -> {
                        mainList.value += result.data!!.response.quotations
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
    fun search(userId: Int, action: String, searchKey: String, scanIndex: Int){
        viewModelScope.launch {
            when(val result = repository.postSearchApi(userId, action, searchKey, scanIndex)){
                is Resource.Success -> {
                    user.value = result.data!!.response.users
                    errorMessage = ""
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                }
            }
        }
    }
    fun searchQuote(userId: Int, action: String, searchKey: String, scanIndexx: Int){
        viewModelScope.launch {
            when(val result = repository.postSearchQuoteApi(userId, action, searchKey, scanIndexx)){
                is Resource.Success -> {
                    quotes.value = result.data!!.response.quotations
                    scanIndex = result.data.response.scanIndex
                    errorMessage = ""
                }
                is Resource.Error -> {
                    errorMessage = result.message!!
                }
            }
        }
    }
    fun loadSearchScans(userId: Int, action: String, searchKey: String, scanIndexx: Int)
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                when (val result = repository.postSearchQuoteApi(userId, action, searchKey, scanIndexx)) {
                    is Resource.Success -> {
                        quotes.value += result.data!!.response.quotations
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
    fun likeButtonClicked(quoteId: Int, userId: Int, callback: (Boolean, Int) -> Unit) {
        viewModelScope.launch {
            val result = repository.postLikeApi(userId, quoteId)
            if (result is Resource.Success) {
                val response = result.data
                val likeCheckResponse = response!!.response
                val isLiked = likeCheckResponse.isDeleted == 0
                val newLikeCount = likeCheckResponse.likeCount

                // Update LiveData
                val updatedList = mainList.value.toMutableList()
                val index = updatedList.indexOfFirst { it.id == quoteId }
                if (index != -1) {
                    val updatedPost = updatedList[index].copy(amIlike = if (isLiked) 1 else 0, likeCount = newLikeCount)
                    updatedList[index] = updatedPost
                    mainList.value = updatedList
                }
                callback(isLiked, newLikeCount)
            }
        }
    }
    suspend fun getUserId(): Int? {
        val userInfo = userDao.getUser()
        return userInfo.userId
    }
//
//    suspend fun getUser(): UserInfo {
//        return userDao.getUser()
//    }

//
//    suspend fun anyData(): Int {
//        return userDao.anyData()
//    }
//
//    suspend fun insert(user:UserInfo) {
//        userDao.insert(user)
//    }
//
//    suspend fun delete(user: UserInfo) {
//        userDao.delete(user)
//    }
}