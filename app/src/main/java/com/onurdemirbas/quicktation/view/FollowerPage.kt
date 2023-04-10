package com.onurdemirbas.quicktation.view

import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.Room
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.database.UserDatabase
import com.onurdemirbas.quicktation.model.Follow
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.FollowerViewModel
import kotlinx.coroutines.*

@Composable
fun FollowerPage(navController: NavController, userId: Int, toUserId: Int, action: String,namesurname: String,likeCount: Int,followCount: Int,followerCount: Int, amIFollow: Int, viewModel: FollowerViewModel = hiltViewModel()) {
    Log.d("userid","first, userid: $userId, touserid: $toUserId")
    Log.d("s","$amIFollow")
    viewModel.viewModelScope.launch{
        viewModel.loadFollowers(userId,toUserId,action)
    }
    val interactionSource =  MutableInteractionSource()
    val photo = viewModel.userPhoto.collectAsState(initial = "").value?:""
    Surface(Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.mainbg), contentDescription = "background image", contentScale = ContentScale.FillHeight)
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    )
    {
        FollowerProfileRow(navController,userId, toUserId, photo, namesurname, likeCount, followCount, followerCount, action ,viewModel)
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
                        ) { navController.navigate("notifications_page/$userId") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.add_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("create_quote_page/$userId") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.chat_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("messages_page/${userId}") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("my_profile_page/$userId") }
                        .size(28.dp, 31.dp))
            }
        }
    }
}



@OptIn(ExperimentalCoilApi::class)
@Composable
fun FollowerProfileRow(navController: NavController, userId: Int, toUserId: Int, photo: String?, namesurname: String, likeCount: Int, followCount: Int, followerCount: Int, action: String, viewModel: FollowerViewModel = hiltViewModel()) {
    val openDialog = remember { mutableStateOf(false) }
    var reportDialog by remember { mutableStateOf(false) }
    var reason by remember { mutableStateOf("") }
    val openDialog2 = remember { mutableStateOf(false) }
    val check = remember { mutableStateOf(false) }
    var exit by remember { mutableStateOf(false) }
    val user = viewModel.userInfo.collectAsState()
    var amIFollow = user.value.amIfollow
    val context = LocalContext.current
    val db: UserDatabase =
        Room.databaseBuilder(context, UserDatabase::class.java, "UserInfo").build()
    val userDao = db.UserDao()
    val myId: Int
    runBlocking {
        myId = userDao.getUser().userId!!
    }
    if(exit)
    {
        viewModel.viewModelScope.launch {
            val userDb = userDao.getUser()
            userDao.delete(userDb)
            navController.navigate("login_page")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.padding(top = 15.dp))
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    if (userId == toUserId) {
                        openDialog2.value = !openDialog2.value
                    } else {
                        openDialog.value = !openDialog.value
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.options),
                        contentDescription = "options",
                        modifier = Modifier
                            .size(52.dp, 20.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(end = 25.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (photo == null || photo == "" || photo.isEmpty() || photo == "null") {
                    Image(
                        painter = painterResource(id = R.drawable.pp),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(75.dp, 75.dp)
                            .clip(CircleShape)
                    )
                } else {
                    val painter = rememberImagePainter(
                        data = Constants.MEDIA_URL + photo,
                        builder = {})
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(75.dp, 75.dp)
                            .clip(CircleShape)
                    )
                }
                Text(
                    text = namesurname,
                    modifier = Modifier.defaultMinSize(165.dp, 30.dp),
                    fontSize = 20.sp
                )
            }
            Spacer(Modifier.padding(top = 15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = {
                    navController.navigate("follower_page/$userId/$toUserId/followers/$namesurname/$likeCount/$followCount/$followerCount/$amIFollow")
                }, colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)) {
                    Text(
                        text = "Takipçiler\n        $followerCount",
                        fontSize = 16.sp,
                        fontFamily = openSansFontFamily
                    )
                }
                TextButton(onClick = {
                    navController.navigate("follower_page/$userId/$toUserId/follows/$namesurname/$likeCount/$followCount/$followerCount/$amIFollow")
                }, colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)) {
                    Text(
                        text = "Takip Edilenler\n            $followCount",
                        fontSize = 16.sp,
                        fontFamily = openSansFontFamily
                    )
                }
                Text(
                    text = "Beğeniler\n       $likeCount",
                    fontSize = 16.sp,
                    fontFamily = openSansFontFamily,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.padding(top = 25.dp))
            FollowerList(navController = navController, toUserId, userId, action)
        }
    }
    if (reportDialog) {
        Popup(
            alignment = Alignment.BottomCenter,
            onDismissRequest = { reportDialog = !reportDialog },
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .background(
                        color = Color(4, 108, 122, 204),
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomEnd = 20.dp,
                            bottomStart = 20.dp
                        )
                    )
                    .size(400.dp, 300.dp)
                    .windowInsetsPadding(WindowInsets.ime)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(value = reason,
                        modifier = Modifier.size(250.dp, 150.dp),
                        onValueChange = {
                            reason = it
                        },
                        textStyle = TextStyle(
                            fontFamily = openSansFontFamily,
                            fontSize = 15.sp,
                            color = Color.Black
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            disabledBorderColor = Color.Black
                        ),
                        shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp),
                        placeholder = {
                            Text(
                                text = "Report reason",
                                fontSize = 13.sp,
                                fontFamily = openSansBold
                            )
                        })
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    Button(onClick = {
                        viewModel.reportUser(myId, toUserId, reason)
                        Toast.makeText(context, "Raporlama Başarıyla İletildi", Toast.LENGTH_LONG)
                            .show()
                        reportDialog = !reportDialog
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                        Text(
                            text = "Send Report",
                            fontFamily = openSansBold,
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
    if (openDialog2.value) {
        Popup(
            alignment = Alignment.BottomCenter,
            onDismissRequest = { openDialog2.value = !openDialog2.value },
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
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
                    .windowInsetsPadding(WindowInsets.ime)
            )
            {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Divider(
                        color = Color.Black,
                        thickness = 3.dp,
                        modifier = Modifier.size(width = 30.dp, height = 3.dp)
                    )
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    Button(
                        onClick = {
                            exit = !exit
                        },
                        border = BorderStroke(1.dp, color = Color.Black),
                        modifier = Modifier.size(250.dp, 50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.logout),
                                contentDescription = "exit",
                                Modifier.size(25.dp)
                            )
                            Spacer(modifier = Modifier.padding(start = 20.dp))
                            Text(
                                text = "Çıkış Yap",
                                fontFamily = openSansBold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Button(
                        onClick = {

                        },
                        border = BorderStroke(1.dp, color = Color.Black),
                        modifier = Modifier.size(250.dp, 50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.deletepost),
                                contentDescription = "delete account",
                                Modifier.size(25.dp)
                            )
                            Spacer(modifier = Modifier.padding(start = 20.dp))
                            Text(
                                text = "Hesabımı Sil",
                                fontFamily = openSansBold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Button(
                        onClick = {
                            navController.navigate("edit_profile_page/$myId")
                        },
                        border = BorderStroke(1.dp, color = Color.Black),
                        modifier = Modifier.size(250.dp, 50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.editprofile),
                                contentDescription = "edit profile",
                                Modifier.size(25.dp)
                            )
                            Spacer(modifier = Modifier.padding(start = 20.dp))
                            Text(
                                text = "Profilimi düzenle",
                                fontFamily = openSansBold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
    if (openDialog.value) {
        Popup(
            alignment = Alignment.BottomCenter,
            onDismissRequest = { openDialog.value = !openDialog.value },
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                contentAlignment = Alignment.TopCenter, modifier = Modifier
                    .background(
                        color = Color(4, 108, 122, 204), shape = RoundedCornerShape(
                            topStart = 20.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 0.dp
                        )
                    )
                    .size(750.dp, 250.dp)
                    .windowInsetsPadding(WindowInsets.ime)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(top = 25.dp))
                    Divider(
                        color = Color.Black,
                        thickness = 3.dp,
                        modifier = Modifier.size(width = 30.dp, height = 3.dp)
                    )
                    Spacer(modifier = Modifier.padding(top =20.dp))
                    Button(
                        onClick = {
                            openDialog.value = !openDialog.value
                            reportDialog = !reportDialog
                        },
                        border = BorderStroke(1.dp, color = Color.Black),
                        modifier = Modifier.size(250.dp, 45.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.report),
                                contentDescription = "report",
                                modifier = Modifier.size(17.dp, 17.dp)
                            )
                            Spacer(Modifier.padding(start = 25.dp))
                            Text(
                                text = "Şikayet Et",
                                fontFamily = openSansBold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Button(
                        onClick = { },
                        border = BorderStroke(1.dp, color = Color.Black),
                        modifier = Modifier.size(250.dp, 45.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.text),
                                contentDescription = "text",
                                modifier = Modifier.size(17.dp, 17.dp)
                            )
                            Spacer(Modifier.padding(start = 25.dp))
                            Text(
                                text = "Mesaj Gönder",
                                fontFamily = openSansBold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    check.value = amIFollow == 0
                    Button(
                        onClick = {
                            if (check.value) {
                                viewModel.viewModelScope.launch {
                                    viewModel.followUnFollowUser(myId, userId)
                                    delay(200)
                                    amIFollow = if (viewModel.amIFollow.value == -1)
                                        amIFollow
                                    else
                                        viewModel.amIFollow.value
                                    Toast.makeText(context, "Takip edildi", Toast.LENGTH_SHORT)
                                        .show()
                                    check.value = !check.value
                                }

                            } else {
                                viewModel.viewModelScope.launch {
                                    viewModel.followUnFollowUser(myId, userId)
                                    delay(200)
                                    amIFollow = if (viewModel.amIFollow.value == -1)
                                        amIFollow
                                    else
                                        viewModel.amIFollow.value
                                    Toast.makeText(
                                        context,
                                        "Takipten çıkarıldı",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    check.value = !check.value
                                }
                            }
                        },
                        border = BorderStroke(1.dp, color = Color.Black),
                        modifier = Modifier.size(250.dp, 45.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = if (check.value) R.drawable.follow else R.drawable.unfollow),
                                contentDescription = "follow",
                                modifier = Modifier.size(17.dp, 17.dp)
                            )
                            Spacer(Modifier.padding(start = 25.dp))
                            Text(
                                text = if (check.value) "Takip Et" else "Takipten Çıkar",
                                fontFamily = openSansBold,
                                fontSize = 17.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FollowerList(navController: NavController, userId: Int, myId: Int, action: String, viewModel: FollowerViewModel = hiltViewModel()) {
    val followerList by viewModel.followerList.collectAsState()
    val errorMessage by remember{viewModel.errorMessage}
    val context = LocalContext.current
    if(errorMessage.isNotEmpty()){
        Toast.makeText(context,errorMessage, Toast.LENGTH_LONG).show()
    }
    else
    {
        FollowerListView(posts = followerList, navController = navController, userId, myId, action = action)
    }
}


@Composable
fun FollowerListView(posts: List<Follow>, navController: NavController, userId: Int, myId: Int, action: String,  viewModel: FollowerViewModel= hiltViewModel()) {
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
            FollowerList(post = post, navController = navController, userId = userId, myId = myId, action = action)
        }
        item {
            LaunchedEffect(endOfListReached) {
                if (scanIndex != 0) {
                    if (scanIndex == -1) {
//                        Toast.makeText(context,"Yeni içerik yok",Toast.LENGTH_LONG).show()
                    } else {
                        viewModel.viewModelScope.launch {
                            // viewModel.loadMainScans(1, scanIndex)
                            withContext(Dispatchers.Default) {
                                // viewModel.loadMainScans(1, scanIndex)
                            }
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
                FollowerListView(posts = followList, navController = navController, userId, myId, action)
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
fun FollowerList(navController: NavController, post: Follow, userId: Int, action: String, myId: Int, viewModel: FollowerViewModel= hiltViewModel()) {
    val userName = post.username
    val toUserId = post.toUserId
    val userIdFromRow = post.userId
    val userPhoto = post.userPhoto
//    val amIFollow = post.amIFollow
    val colorx = remember { mutableStateOf(Color.Yellow) }
//    var color = Color.Transparent
    val context = LocalContext.current
    val check = remember { mutableStateOf(false) }
    var amIFollowed = viewModel.userInfo.collectAsState().value.amIfollow
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(start = 20.dp, end = 20.dp, top = 20.dp), contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            if (userPhoto == null || userPhoto == ""|| userPhoto.isEmpty() || userPhoto == "null") {
                Image(
                    painter = painterResource(id = R.drawable.pp),
                    contentDescription = null,
                    contentScale  = ContentScale.Crop,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(44.dp, 44.dp)
                        .clickable {
                            if (action == "follows") {
                                if (myId == toUserId) {
                                    navController.navigate("my_profile_page/$myId")
                                } else {
                                    navController.navigate("other_profile_page/$toUserId/$myId")
                                }
                            } else if (action == "followers") {
                                if (myId == userIdFromRow) {
                                    navController.navigate("my_profile_page/$myId")
                                } else {
                                    navController.navigate("other_profile_page/$userIdFromRow/$myId")
                                }
                            }
                        }
                        .clip(CircleShape)
                )
            }
            else {
                val painter = rememberImagePainter(data = Constants.MEDIA_URL + userPhoto, builder = {})
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(44.dp, 44.dp)
                        .clickable {
                            if (action == "follows") {
                                if (myId == toUserId) {
                                    navController.navigate("my_profile_page/$myId")
                                } else {
                                    navController.navigate("other_profile_page/$toUserId/$myId")
                                }
                            } else if (action == "followers") {
                                if (myId == userIdFromRow) {
                                    navController.navigate("my_profile_page/$myId")
                                } else {
                                    navController.navigate("other_profile_page/$userIdFromRow/$myId")
                                }
                            }
                        }
                        .clip(CircleShape)
                )
            }
            Text(
                text = userName,
                modifier = Modifier.defaultMinSize(80.dp, 30.dp).clickable {
                    if(action == "follows")
                    {
                        if (myId == toUserId) {
                            navController.navigate("my_profile_page/$myId")
                        } else {
                            navController.navigate("other_profile_page/$toUserId/$myId")
                        }
                    }
                    else if(action == "followers")
                    {
                        if (myId == userIdFromRow) {
                            navController.navigate("my_profile_page/$myId")
                        } else {
                            navController.navigate("other_profile_page/$userIdFromRow/$myId")
                        }
                    }
                },
                fontSize = 16.sp,
                fontFamily = openSansFontFamily,
            )
            if(amIFollowed==0)
            {
                colorx.value = Color.White
//                color = Color.Black
            }
            else if(amIFollowed ==1)
            {
                colorx.value = Color.Black
//                color = Color.White
            }
            check.value = amIFollowed == 0
            if (action == "follows") {
                if (myId == toUserId) {
                    Spacer(Modifier.padding(0.dp))
                }
                else {
                    Button(onClick = {
                        if (check.value) {
                            runBlocking {
                                viewModel.followUnFollowUser(myId, userId)
                                amIFollowed = if (viewModel.amIFollow.value == -1)
                                    amIFollowed
                                else
                                    viewModel.amIFollow.value
                                Toast.makeText(context, "Takip edildi", Toast.LENGTH_SHORT)
                                    .show()
                                check.value = !check.value
                            }

                        } else {
                            runBlocking {
                                viewModel.followUnFollowUser(myId, userId)
                                amIFollowed = if (viewModel.amIFollow.value == -1)
                                    amIFollowed
                                else
                                    viewModel.amIFollow.value
                                Toast.makeText(
                                    context,
                                    "Takipten çıkarıldı",
                                    Toast.LENGTH_SHORT
                                ).show()
                                check.value = !check.value
                            }
                        }
                    }, modifier = Modifier.size(120.dp,30.dp), shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(backgroundColor = colorx.value)) {
                        Text(
                            text = if (check.value) "Takip Et" else "Takipten Çıkar",
                            fontFamily = openSansBold,
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                    }
                }
            } else if (action == "followers") {
                if (myId == userIdFromRow) {
                    Spacer(Modifier.padding(0.dp))
                } else {
                    Button(onClick = {
                        if (check.value) {
                            runBlocking {
                                viewModel.followUnFollowUser(myId, userId)
                                amIFollowed = if (viewModel.amIFollow.value == -1)
                                    amIFollowed
                                else
                                    viewModel.amIFollow.value
                                Toast.makeText(context, "Takip edildi", Toast.LENGTH_SHORT)
                                    .show()
                                check.value = !check.value
                            }

                        } else {
                            runBlocking {
                                viewModel.followUnFollowUser(myId, userId)
                                amIFollowed = if (viewModel.amIFollow.value == -1)
                                    amIFollowed
                                else
                                    viewModel.amIFollow.value
                                Toast.makeText(
                                    context,
                                    "Takipten çıkarıldı",
                                    Toast.LENGTH_SHORT
                                ).show()
                                check.value = !check.value
                            }
                        }
                    }, modifier = Modifier.size(120.dp,30.dp), shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(backgroundColor = colorx.value)) {
                        Text(
                            text = if (check.value) "Takip Et" else "Takipten Çıkar",
                            fontFamily = openSansBold,
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}