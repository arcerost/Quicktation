@file:OptIn(ExperimentalCoilApi::class)

package com.onurdemirbas.quicktation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.onurdemirbas.quicktation.util.StoreUserInfo
import com.onurdemirbas.quicktation.viewmodel.MessagesViewModel
import kotlinx.coroutines.launch

@Composable
fun MessagesPage(navController: NavController, viewModel: MessagesViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val myId = StoreUserInfo(context = context).getId.collectAsState(-1)
    viewModel.viewModelScope.launch {
        viewModel.loadQuotes(myId.value!!)
    }
    val userName = viewModel.userInfo.value.namesurname
    val userPhoto = viewModel.userInfo.value.photo
    val interactionSource =  MutableInteractionSource()
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFDDDDDD)) {
    }
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
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(text = userName, fontFamily = openSansFontFamily, fontSize = 20.sp)
                Spacer(modifier = Modifier.padding(20.dp))
                if(userPhoto == null || userPhoto == "" || userPhoto == "null") {
                    Image(
                        painter = painterResource(id = R.drawable.pp),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(75.dp, 75.dp)
                    )
                }
                else {
                    val painter = rememberImagePainter(data = Constants.BASE_URL + userPhoto, builder = {})
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(75.dp, 75.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            MessageRow(userPhoto,userName)
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
                Image(painter = painterResource(id = R.drawable.homeblack),
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
                Image(painter = painterResource(id = R.drawable.chat),
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


@Composable
fun MessageRow(userPhoto: String?, userName: String) {
    Box(
        modifier = Modifier
            .defaultMinSize(315.dp, 50.dp)
            .fillMaxWidth(), contentAlignment = Alignment.TopStart
    ) {
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start) {
                if(userPhoto == null || userPhoto == "" || userPhoto == "null") {
                    Image(
                        painter = painterResource(id = R.drawable.pp),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(44.dp, 44.dp)
                    )
                }
                else {
                    val painter = rememberImagePainter(data = Constants.BASE_URL + userPhoto, builder = {})
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(44.dp, 44.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(start = 15.dp))
                Text(text = userName+"\n"+ Text(text = "last message",fontSize = 13.sp, fontFamily = openSansFontFamily), fontFamily = openSansBold, fontSize = 20.sp)
                Spacer(modifier = Modifier.padding(start = 20.dp))
                Text(text = "1g once")
            }
        }
    }
}