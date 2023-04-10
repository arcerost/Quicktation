@file:OptIn(ExperimentalCoilApi::class)

package com.onurdemirbas.quicktation.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.MessagesViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import java.util.*

@Composable
fun MessagesPage(navController: NavController, myId: Int, viewModel: MessagesViewModel = hiltViewModel()) {
    val client = OkHttpClient()
    val req = Request.Builder().url("ws://63.32.138.61:4000/").build()
    val listener = com.onurdemirbas.quicktation.websocket.WebSocketListener(myId.toString())
    val webSocket = client.newWebSocket(req,listener)
    listener.onAssignUser(webSocket,myId,"assignUser")
    var userName = ""
    viewModel.loadQuotes(myId)
    runBlocking {
        launch {
            userName = viewModel.userInfo.value.namesurname
        }
    }
    val userPhoto = viewModel.userInfo.collectAsState().value.photo
    val interactionSource =  MutableInteractionSource()
    Surface(Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.mainbg), contentDescription = "background image", contentScale = ContentScale.FillHeight)
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
            Spacer(modifier = Modifier.padding(top = 20.dp))
            MessageRow(userPhoto,userName, myId, navController)
            MessageRow(userPhoto,userName, myId, navController)
            MessageRow(userPhoto,userName, myId, navController)
            MessageRow(userPhoto,userName, myId, navController)
            MessageRow(userPhoto,userName, myId, navController)
            MessageRow(userPhoto,userName, myId, navController)
            MessageRow(userPhoto,userName, myId, navController)
            MessageRow(userPhoto,userName, myId, navController)
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
                        ) { navController.navigate("notifications_page/${myId}") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.add_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("create_quote_page/$myId") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.chat),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("messages_page/${myId}") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("my_profile_page/${myId}") }
                        .size(28.dp, 31.dp))
            }
        }
    }
}


@Composable
fun MessageRow(userPhoto: String?, userName: String, myId: Int, navController: NavController) {
    Log.d("s", userName)
    Box(
        modifier = Modifier
            .defaultMinSize(315.dp, 50.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("in_message_page/${myId}")
            }, contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                if(userPhoto == null || userPhoto == "" || userPhoto == "null") {
                    Image(
                        painter = painterResource(id = R.drawable.pp),
                        contentDescription = "user photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(44.dp, 44.dp)
                            .clip(CircleShape)
                    )
                }
                else {
                    val painter = rememberImagePainter(data = Constants.MEDIA_URL + userPhoto, builder = {})
                    Image(
                        painter = painter,
                        contentDescription = "user photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(44.dp, 44.dp)
                            .clip(CircleShape)
                    )
                }
                Box(modifier = Modifier
                    .wrapContentSize(), contentAlignment = Alignment.Center) {
                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.wrapContentSize()) {
                        Text(text = "username", fontFamily = openSansBold, fontSize = 18.sp)
                        Text(text = "message", fontFamily = openSansFontFamily, fontSize = 13.sp)
                    }
                }
                Spacer(modifier = Modifier.padding(horizontal = 20.dp))
                Text(text = "1g once")
                Spacer(modifier = Modifier.padding(end = 5.dp))
            }
        }
    }
}