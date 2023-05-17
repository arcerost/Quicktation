package com.onurdemirbas.quicktation.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.onurdemirbas.quicktation.database.UserDatabase
import com.onurdemirbas.quicktation.model.DetailMessageWithUser
import com.onurdemirbas.quicktation.model.UserInfo
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InMessageViewModel @Inject constructor(private val repository: QuicktationRepository, @ApplicationContext context: Context) : ViewModel() {
    var errorMessage = mutableStateOf("")
    private val db: UserDatabase = Room.databaseBuilder(context, UserDatabase::class.java, "UserInfo").build()
    private val userDao = db.UserDao()
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","","",""))
    val messageList = MutableStateFlow<List<DetailMessageWithUser>>(emptyList())
    fun loadUser(myUserId: Int, userId: Int) {
        viewModelScope.launch {
            when (val result = repository.postMyProfileApi(userId, myUserId)) {
                is Resource.Success -> {
                    userInfo.value = result.data!!.response.userInfo
                    errorMessage.value = ""
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    println(errorMessage.value)
                }
            }
        }
    }
    private suspend fun getUserId(): Int? {
        val userInfo = userDao.getUser()
        return userInfo.userId
    }
    fun loadRoom(roomId: Int){
        viewModelScope.launch {
            val userId = getUserId()
            when (val result = repository.postMessageDetailApi(roomId)){
                is Resource.Success -> {
                    result.data?.response?.messages?.let {
                        messageList.value = ArrayList(it.map { detailMessage ->
                            DetailMessageWithUser(detailMessage, userId!!)
                        })
                    }
                    errorMessage.value = ""
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                }
            }
        }
    }
}