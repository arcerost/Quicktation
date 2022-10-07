package com.onurdemirbas.quicktation.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.model.Follow
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.FollowerViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun FollowerPage(navController: NavController, userId: Int, toUserId: Int, action: String, photo:String,namesurname: String,likeCount: Int,followCount: Int,followerCount: Int, amIFollow: Int, viewModel: FollowerViewModel = hiltViewModel()) {
    viewModel.viewModelScope.launch{
        viewModel.loadFollowers(userId,toUserId,action)
    }
    val interactionSource =  MutableInteractionSource()
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFDDDDDD)) {
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    )
    {
        FollowerProfileRow(navController,userId, toUserId,photo,namesurname,likeCount,followCount,followerCount,amIFollow,viewModel)
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



@OptIn(ExperimentalCoilApi::class)
@Composable
fun FollowerProfileRow(navController: NavController, userId: Int, toUserId: Int, photo: String?, namesurname: String, likeCount: Int, followCount: Int, followerCount: Int, amIFollow: Int, viewModel: FollowerViewModel = hiltViewModel()) {
    val openDialog = remember { mutableStateOf(false) }
    val openDialog2 = remember { mutableStateOf(false) }
    val check = remember { mutableStateOf(false) }
    val check2 = remember { mutableStateOf(false) }
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(), contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.padding(top = 25.dp))
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(start = 25.dp))
                Image(painter = painterResource(id = R.drawable.options),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            if(userId==toUserId)
                            {
                                openDialog2.value = !openDialog2.value
                            }
                            else
                            {
                                openDialog.value = !openDialog.value
                            }
                        }
                        .size(52.dp, 12.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(start = 100.dp))
                Text(
                    text = namesurname,
                    modifier = Modifier.defaultMinSize(165.dp, 30.dp),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.padding(start = 15.dp))
                if (photo == null || photo == ""|| photo.isEmpty() || photo == "null") {
                    Image(
                        painter = painterResource(id = R.drawable.pp),
                        contentDescription = null,
                        modifier = Modifier
                            .size(75.dp, 75.dp)
                    )
                } else {
                    val painter = rememberImagePainter(
                        data = Constants.BASE_URL + photo,
                        builder = {})
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(75.dp, 75.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(end = 15.dp))
            }
            Spacer(Modifier.padding(top = 15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Takipçiler\n        $followerCount", fontSize = 16.sp, modifier = Modifier.clickable {
                    navController.navigate("follower_page/$userId/$toUserId/followers/$photo/$namesurname/$likeCount/$followCount/$followerCount/$amIFollow")
                })
                Text("Takip Edilenler\n            $followCount", fontSize = 16.sp, modifier = Modifier.clickable {
                    navController.navigate("follower_page/$userId/$toUserId/follows/$photo/$namesurname/$likeCount/$followCount/$followerCount/$amIFollow")
                })
                Text(text = "Beğeniler\n       $likeCount", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.padding(top = 25.dp))
            FollowerList(navController = navController, toUserId, userId)
        }
    }
    if(openDialog2.value)
    {
        Popup(alignment = Alignment.BottomCenter, onDismissRequest = {openDialog2.value = !openDialog2.value}, properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = true)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .background(
                    color = Color(4, 108, 122, 204),
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .size(750.dp, 220.dp)
                //.wrapContentSize()
                .windowInsetsPadding(WindowInsets.ime))
            {
                Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                    Divider(color = Color.Black, thickness = 3.dp, modifier = Modifier.size(width = 30.dp, height = 3.dp))
                    Spacer(modifier = Modifier.padding(5.dp))
                    Button(onClick = {

                    }, border = BorderStroke(1.dp, color = Color.Black),modifier = Modifier.size(250.dp,50.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                        Text(text = "Hesabımı Sil", fontFamily = openSansBold, fontSize = 17.sp, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(onClick = {
                        navController.navigate("edit_profile_page/$userId")
                    }, border = BorderStroke(1.dp, color = Color.Black),modifier = Modifier.size(250.dp,50.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                        Text(text = "Profilimi düzenle", fontFamily = openSansBold, fontSize = 17.sp, color = Color.Black)
                    }
                }
            }
        }
    }
    if(openDialog.value)
    {
        Popup(alignment = Alignment.BottomCenter, onDismissRequest = {openDialog.value = !openDialog.value}, properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = true)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .background(
                    color = Color(4, 108, 122, 204),
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .size(750.dp, 310.dp)
                .windowInsetsPadding(WindowInsets.ime))
            {
                Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                    Divider(color = Color.Black, thickness = 3.dp, modifier = Modifier.size(width = 30.dp, height = 3.dp))
                    Spacer(modifier = Modifier.padding(20.dp))
                    Button(onClick = {  }, border = BorderStroke(1.dp, color = Color.Black),modifier = Modifier.size(250.dp,45.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth())
                        {
                            Image(painter = painterResource(id = R.drawable.report), contentDescription = "report", modifier = Modifier.size(17.dp,17.dp))
                            Spacer(Modifier.padding(start = 25.dp))
                            Text(text = "Şikayet Et", fontFamily = openSansBold, fontSize = 17.sp, color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Button(onClick = {  }, border = BorderStroke(1.dp, color = Color.Black),modifier = Modifier.size(250.dp,45.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth())
                        {
                            Image(painter = painterResource(id = R.drawable.text), contentDescription = "text", modifier = Modifier.size(17.dp,17.dp))
                            Spacer(Modifier.padding(start = 25.dp))
                            Text(text = "Mesaj Gönder", fontFamily = openSansBold, fontSize = 17.sp, color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    if(amIFollow ==0)
                    {
                        check.value = !check.value
                    }
                    else
                    {
                        check2.value = !check2.value
                    }
                    if(check.value){
                        check.value = !check.value
                        Button(onClick = {  }, border = BorderStroke(1.dp, color = Color.Black),modifier = Modifier.size(250.dp,45.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth())
                            {
                                Image(painter = painterResource(id = R.drawable.follow), contentDescription = "follow", modifier = Modifier.size(17.dp,17.dp))
                                Spacer(Modifier.padding(start = 25.dp))
                                Text(text = "Takip Et", fontFamily = openSansBold, fontSize = 17.sp, color = Color.Black)
                            }
                        }
                    }
                    if(check2.value){
                        check2.value = !check2.value
                        Button(onClick = {  }, border = BorderStroke(1.dp, color = Color.Black),modifier = Modifier.size(250.dp,45.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth())
                            {
                                Image(painter = painterResource(id = R.drawable.unfollow), contentDescription = "unfollow", modifier = Modifier.size(17.dp,17.dp))
                                Spacer(Modifier.padding(start = 25.dp))
                                Text(text = "Takipten Çıkar", fontFamily = openSansBold, fontSize = 17.sp, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FollowerList(navController: NavController, userId: Int, myId: Int, viewModel: FollowerViewModel = hiltViewModel()) {
    val followerList by viewModel.followerList.collectAsState()
    val errorMessage by remember{viewModel.errorMessage}
    val context = LocalContext.current
    if(errorMessage.isNotEmpty()){
        Toast.makeText(context,errorMessage, Toast.LENGTH_LONG).show()
    }
    else
    {
        FollowerListView(posts = followerList, navController = navController, userId, myId)
    }
}


@Composable
fun FollowerListView(posts: List<Follow>, navController: NavController, userId: Int, myId: Int,  viewModel: FollowerViewModel= hiltViewModel()) {
    val context = LocalContext.current
    val scanIndex by viewModel.scanIndex.collectAsState()
    var checkState by remember { mutableStateOf(false) }
    val errorMessage by remember { viewModel.errorMessage }
    val followList by viewModel.followerList.collectAsState()
    val state = rememberLazyListState()
    fun LazyListState.isScrolledToEnd() =
        layoutInfo.visibleItemsInfo.firstOrNull()?.index == layoutInfo.totalItemsCount - 1
    val endOfListReached by remember { derivedStateOf { state.isScrolledToEnd() } }
    LazyColumn(
        contentPadding = PaddingValues(top = 5.dp, bottom = 25.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        state = state
    ) {
        items(posts) { post ->
            FollowerList(post = post, navController = navController, userId = userId, myId = myId)
        }
        item {
            LaunchedEffect(endOfListReached) {
                if (scanIndex != 0) {
                    if (scanIndex == -1) {
//                        Toast.makeText(context,"Yeni içerik yok",Toast.LENGTH_LONG).show()
                    } else {
                        viewModel.viewModelScope.launch {
                            async {
                                // viewModel.loadMainScans(1, scanIndex)
                            }.await()
                            checkState = !checkState
                        }
                    }
                }
            }
        }
    }
    if(checkState) {
        if(endOfListReached)
        {
            if(scanIndex>0) {
                FollowerListView(posts = followList, navController = navController, userId, myId)
                if (errorMessage.isNotEmpty()) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
            checkState= !checkState
        }
    }
}



@OptIn(ExperimentalCoilApi::class)
@Composable
fun FollowerList(navController: NavController, post: Follow, userId: Int, myId: Int) {
    val userName = post.username
    val userPhoto = post.userPhoto
    val amIFollow = post.amIFollow
    val colorx = remember { mutableStateOf(Color.Yellow) }
    var color = Color.Transparent
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(start = 20.dp, end = 20.dp, top = 20.dp), contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            if (userPhoto == null || userPhoto == ""|| userPhoto.isEmpty() || userPhoto == "null") {
                Image(
                    painter = painterResource(id = R.drawable.pp),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(44.dp, 44.dp)
                        .clickable {
                            if (myId == userId) {
                                navController.navigate("my_profile_page")
                            } else {
                                navController.navigate("other_profile_page/$userId/$myId")
                            }
                        }
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
                        .clickable {
                            if (myId == userId) {
                                navController.navigate("my_profile_page")
                            } else {
                                navController.navigate("other_profile_page/$userId/$myId")
                            }
                        }
                )
            }
            Text(
                text = userName,
                modifier = Modifier.defaultMinSize(80.dp, 30.dp),
                fontSize = 16.sp,
                fontFamily = openSansFontFamily
            )
            if(amIFollow==0)
            {
                colorx.value = Color.White
                color = Color.Black
            }
            else if(amIFollow ==1)
            {
                colorx.value = Color.Black
                color = Color.White
            }
            Button(onClick = { }, modifier = Modifier.size(120.dp,30.dp), shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(backgroundColor = colorx.value)) {
                when (amIFollow) {
                    1 -> {
                        Text(text = "Takibi bırak", color = color, fontSize = 12.sp, fontFamily = openSansFontFamily, modifier = Modifier.defaultMinSize(45.dp,16.dp))
                    }
                    0 -> {
                        Text(text = "Takip et", color = color, fontSize = 12.sp, fontFamily = openSansFontFamily, modifier = Modifier.defaultMinSize(45.dp,16.dp))
                    }
                    else -> {
                        Text(text = "Error.", color = color, fontSize = 12.sp, fontFamily = openSansFontFamily, modifier = Modifier.defaultMinSize(45.dp,16.dp))
                    }
                }
            }
        }
    }
}