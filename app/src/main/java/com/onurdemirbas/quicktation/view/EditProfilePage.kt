@file:OptIn(ExperimentalCoilApi::class)

package com.onurdemirbas.quicktation.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.EditProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun EditProfilePage(navController: NavController, myId: Int, viewModel: EditProfileViewModel = hiltViewModel()) {
    viewModel.viewModelScope.launch {
        viewModel.loadUser(myId)
    }
    val userInfo = viewModel.userInfo.collectAsState()
    val userNameAlr = userInfo.value.namesurname
    val userEmailAlr = userInfo.value.email
    val userPhotoAlr = userInfo.value.photo
    val username = remember { mutableStateOf(TextFieldValue()) }
    val email = remember { mutableStateOf(TextFieldValue()) }
    var userPhoto = remember { mutableStateOf("") }
    val photoFromVm = viewModel.userphoto.collectAsState()
    val interactionSource =  MutableInteractionSource()
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        )
        {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            )
            {
                Spacer(modifier = Modifier.padding(top = 25.dp))
                Text(text = "PROFİLİ DÜZENLE", fontSize = 18.sp, fontFamily = openSansBold)
                Spacer(modifier = Modifier.padding(top = 15.dp))
                Divider(
                    thickness = 1.dp,
                    color = Color.Black,
                    modifier = Modifier.size(300.dp, 1.dp)
                )
                Spacer(modifier = Modifier.padding(top = 30.dp))
                Box(contentAlignment = Alignment.Center)
                {
                    if (userPhotoAlr == "" || userPhotoAlr == null || userPhotoAlr == "null") {
                        Image(
                            painter = painterResource(id = R.drawable.pp),
                            contentDescription = "profile photo",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(100.dp)
                        )
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier.size(120.dp)
                        ) {
                            Image(painter = painterResource(id = R.drawable.addphoto),
                                contentDescription = "add photo",
                                Modifier
                                    .size(30.dp, 30.dp)
                                    .clickable {
                                        userPhoto = userPhoto
                                    })
                        }
                    } else {
                        val painter = rememberImagePainter(
                            data = Constants.BASE_URL + userPhotoAlr,
                            builder = {})
                        Image(
                            painter = painter,
                            contentDescription = "profile photo",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(100.dp)
                        )
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier.size(120.dp)
                        ) {
                            Image(painter = painterResource(id = R.drawable.addphoto),
                                contentDescription = "add photo",
                                Modifier
                                    .size(30.dp, 30.dp)
                                    .clickable { userPhoto = userPhoto })
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(top = 50.dp))
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    enabled = false,
                    textStyle = TextStyle(fontFamily = openSansFontFamily),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color(217, 217, 217, 255)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = {
                        Text(
                            text = userEmailAlr
                        )
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = "email icon",
                            Modifier.size(25.dp)
                        )
                    },
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(20.dp))
                OutlinedTextField(
                    value = username.value,
                    onValueChange = {
                        username.value = it
                    },
                    textStyle = TextStyle(fontFamily = openSansFontFamily),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color(217, 217, 217, 255)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = {
                        Text(
                            text = userNameAlr
                        )
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.useredit),
                            contentDescription = "username icon",
                            Modifier.size(25.dp)
                        )
                    },
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(30.dp))
                Button(
                    onClick = {
                        viewModel.loadEdit(myId, username.value.text, userPhoto.value)
                        userPhoto.value = photoFromVm.value
                        viewModel.viewModelScope.launch {
                            delay(300)
                            Toast.makeText(context, "Değişiklikler Kaydedildi.", Toast.LENGTH_LONG)
                                .show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
                    border = BorderStroke(1.dp, color = Color.Black),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.size(130.dp, 50.dp)
                ) {
                    Text(text = "KAYDET", color = Color.Black, fontFamily = openSansBold)
                }
            }
        }
    }
    //BottomBar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(), contentAlignment = Alignment.BottomStart
    )
    {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .size(height = 50.dp, width = 500.dp), color = Color(
                0xFFC1C1C1
            )
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.backgroundbottombar), contentDescription = "background", contentScale = ContentScale.FillWidth)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.home),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.notifications_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("notifications_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.add_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.chat_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("messages_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("my_profile_page") }
                        .size(28.dp, 31.dp))
            }
        }
    }
}