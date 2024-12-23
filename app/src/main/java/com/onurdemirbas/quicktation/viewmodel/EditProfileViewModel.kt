package com.onurdemirbas.quicktation.viewmodel


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurdemirbas.quicktation.model.UserInfo
import com.onurdemirbas.quicktation.repository.QuicktationRepository
import com.onurdemirbas.quicktation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel@Inject constructor(private val repository: QuicktationRepository) : ViewModel()  {
    var userphoto = MutableStateFlow("")
    var nameSurname = MutableStateFlow("")
    var userName =  MutableStateFlow("")
    var errorMessage = mutableStateOf("")
    var userInfo = MutableStateFlow(UserInfo(1,"","",1,1,1,1,"","","",""))
    fun loadEdit(userId: Int, namesurname: String, userPhoto: String, username: String?)
    {
        viewModelScope.launch {
            when(val result = repository.postEditProfileApi(userId, namesurname, userPhoto, username))
            {
                is Resource.Success -> {
                    userphoto.value = result.data!!.response.userPhoto
                    nameSurname.value = result.data.response.namesurname
                    userName.value =  result.data.response.username
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                }
            }
        }
    }

    fun loadUser(myUserId: Int) {
        viewModelScope.launch {
            when (val result = repository.postMyProfileApi(myUserId, myUserId)) {
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
}