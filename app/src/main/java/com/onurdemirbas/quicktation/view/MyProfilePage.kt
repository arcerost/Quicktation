package com.onurdemirbas.quicktation.view

import android.content.Intent
import android.media.AudioAttributes
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.Room
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.database.UserDatabase
import com.onurdemirbas.quicktation.model.QuoteFromMyProfile
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.MyProfileViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun MyProfilePage(navController: NavController,myId: Int,viewModel: MyProfileViewModel = hiltViewModel()) {
    viewModel.loadQuotes(myId)
    val interactionSource =  MutableInteractionSource()
    Surface(Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.mainbg), contentDescription = "background image", contentScale = ContentScale.FillHeight)
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    )
        {
            ProfileRow(navController = navController, myId = myId)
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
                        ) { navController.navigate("notifications_page/$myId") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.add_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("create_quote_page/$myId") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.chat_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("messages_page/${myId}") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("my_profile_page/$myId") }
                        .size(28.dp, 31.dp))
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileRow(navController: NavController, viewModel: MyProfileViewModel = hiltViewModel(), myId: Int) {
    val context = LocalContext.current
    val openDialog2 = remember { mutableStateOf(false) }
    val user = viewModel.userInfo.collectAsState()
    var exit by remember { mutableStateOf(false) }
    val db: UserDatabase = Room.databaseBuilder(context, UserDatabase::class.java,"UserInfo").build()
    val userDao = db.UserDao()
    if(exit)
    {
        viewModel.viewModelScope.launch {
            val userDb = userDao.getUser()
            userDao.delete(userDb)
            navController.navigate("login_page")
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.padding(top = 15.dp))
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {openDialog2.value = !openDialog2.value }) {
                    Icon(painter = painterResource(id = R.drawable.options), contentDescription = "options",
                        modifier = Modifier
                            .size(52.dp, 20.dp))
                }
                Spacer(modifier = Modifier.padding(end = 25.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                if (user.value.photo == null || user.value.photo == "" || user.value.photo == "null") {
                    Image(
                        painter = painterResource(id = R.drawable.pp),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .size(75.dp, 75.dp)
                            .clip(CircleShape)
                    )
                } else {
                    val painter = rememberImagePainter(
                        data = Constants.MEDIA_URL + user.value.photo,
                        builder = {})
                    Image(
                        painter = painter,
                        contentScale = ContentScale.Crop
                        ,contentDescription = null,
                        modifier = Modifier
                            .size(75.dp, 75.dp)
                            .clip(CircleShape)
                    )
                }
                Text(
                    text = user.value.namesurname,
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
                    navController.navigate("follower_page/$myId/${user.value.id}/followers/${user.value.namesurname}/${user.value.likeCount}/${user.value.followCount}/${user.value.followerCount}/${user.value.amIfollow}")
                }, colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)) {
                    Text(text = "Takipçiler\n        ${user.value.followerCount}", fontSize = 16.sp, fontFamily = openSansFontFamily)
                }
                TextButton(onClick = {
                    navController.navigate("follower_page/$myId/${user.value.id}/follows/${user.value.namesurname}/${user.value.likeCount}/${user.value.followCount}/${user.value.followerCount}/${user.value.amIfollow}")
                }, colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)) {
                    Text(text = "Takip Edilenler\n            ${user.value.followCount}", fontSize = 16.sp, fontFamily = openSansFontFamily)
                }
                Text(text = "Beğeniler\n       ${user.value.likeCount}", fontSize = 16.sp, fontFamily = openSansFontFamily, letterSpacing = 1.sp)
            }
            Spacer(modifier = Modifier.padding(top = 25.dp))
            ProfilePostList(navController = navController,myId)
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
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                        Button(onClick = {
                            exit=!exit
                        }, border = BorderStroke(1.dp, color = Color.Black),modifier = Modifier.size(250.dp,50.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.logout),
                                    contentDescription = "delete post",
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
                        Button(onClick = {

                        }, border = BorderStroke(1.dp, color = Color.Black),modifier = Modifier.size(250.dp,50.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.deletepost),
                                    contentDescription = "delete post",
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
                    Spacer(modifier = Modifier.padding(top =10.dp))
                        Button(onClick = {
                            navController.navigate("edit_profile_page/$myId")
                        }, border = BorderStroke(1.dp, color = Color.Black),modifier = Modifier.size(250.dp,50.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.editprofile),
                                    contentDescription = "delete post",
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
}


@Composable
fun ProfilePostList(navController: NavController, myId: Int, viewModel: MyProfileViewModel = hiltViewModel()) {
    val postList by viewModel.posts.collectAsState()
    val errorMessage by remember { viewModel.errorMessage }
    val context = LocalContext.current
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
    else
    {
        ProfilePostListView(posts = postList, navController = navController,myId)
    }
}



@Composable
fun ProfilePostListView(posts: List<QuoteFromMyProfile>, navController: NavController, myId: Int ,viewModel: MyProfileViewModel = hiltViewModel()) {
    val scanIndex by viewModel.scanIndex.collectAsState()
    var checkState by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val errorMessage by remember { viewModel.errorMessage }
    val state = rememberLazyListState()
    fun LazyListState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.firstOrNull()?.index == layoutInfo.totalItemsCount - 1
    val endOfListReached by remember { derivedStateOf { state.isScrolledToEnd() } }
    val postList by viewModel.posts.collectAsState()
    LazyColumn(contentPadding = PaddingValues(top = 5.dp, bottom = 50.dp), verticalArrangement = Arrangement.SpaceEvenly, state = state) {
        items(posts) { post ->
            ProfileQuoteRow(post = post, navController = navController, myId = myId)
        }
        item {
            LaunchedEffect(endOfListReached) {
                if(scanIndex != 0) {
                    if(scanIndex == -1)
                    {
//                        Toast.makeText(context,"Yeni içerik yok",Toast.LENGTH_LONG).show()
                    }
                    else {
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
                ProfilePostListView(posts = postList, navController = navController,myId)
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
fun ProfileQuoteRow(viewModel: MyProfileViewModel = hiltViewModel(), post: QuoteFromMyProfile, navController: NavController, myId: Int) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    val quoteId  = post.id
    val username = post.username
    val quoteUrl = post.quote_url
    var amILike = post.amIlike
    var likeCount = post.likeCount
    val quoteText = post.quote_text
    val userPhoto = post.userphoto
    var color: Color
    var isPressed by remember { mutableStateOf(false) }
    val url = Constants.MEDIA_URL +quoteUrl
    var mediaPressed by remember { mutableStateOf(false)}
    var playPressed by remember { mutableStateOf(false) }
    val mediaItem = MediaItem.fromUri(url)
    val player = ExoPlayer.Builder(context).build()
    player.setMediaItem(mediaItem)
    player.setAudioAttributes(com.google.android.exoplayer2.audio.AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .build(),true)
    LaunchedEffect(player) {
        player.prepare()
    }
    var progress by remember { mutableStateOf(0f) }
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(), contentAlignment = Alignment.TopStart) {
        Surface(shape = RoundedCornerShape(15.dp), modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .clickable {
                navController.navigate("quote_detail_page/$quoteId/$myId")
            }) {
            Box(
                modifier = Modifier
                    .defaultMinSize(343.dp, 140.dp)
                    .fillMaxWidth(), contentAlignment = Alignment.TopStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.backgroundbottombar),
                    contentDescription = "background",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Spacer(modifier = Modifier.padding(15.dp))
                        IconButton(onClick = {
                            playPressed = !playPressed
                            mediaPressed = !mediaPressed
                            player.playWhenReady
                        })
                        {
                            if(playPressed)
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.play_pause),
                                    modifier = Modifier.size(10.dp, 12.dp),
                                    contentDescription = "play/pause",
                                    tint = Color.White
                                )
                                player.play()
                            }
                            else
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.play),
                                    modifier = Modifier.size(10.dp, 12.dp),
                                    contentDescription = "play/pause",
                                    tint = Color.White
                                )
                                player.pause()
                            }
                        }
                        Slider(value = progress, onValueChange = { progress = it }, modifier = Modifier.size(100.dp,50.dp),enabled = false, colors = SliderDefaults.colors(thumbColor = Color.White, disabledThumbColor = Color.White, activeTickColor = Color.White, inactiveTickColor = Color.White, activeTrackColor = Color.White, inactiveTrackColor = Color.White, disabledActiveTickColor = Color.White, disabledActiveTrackColor = Color.White, disabledInactiveTickColor = Color.White, disabledInactiveTrackColor = Color.White))
                        Spacer(modifier = Modifier.padding(start=0.dp))
                        Text(text = "3:21", color = Color.White, modifier = Modifier.padding(top = 15.dp))
                        Spacer(modifier = Modifier.padding(start = 60.dp))
                        Text(text = "$likeCount BEĞENME"
                            , color = Color.White, modifier = Modifier
                                .padding(top = 15.dp))
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                    )
                    {
                        HashText(navController = navController, fullText = quoteText, quoteId = quoteId, userId = myId)
                        Spacer(modifier = Modifier.padding(top = 20.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Text(text = "-$username", color = Color.White)
                            Spacer(modifier = Modifier.padding(start = 150.dp))
                            Icon(painter = painterResource(id = R.drawable.options),
                                contentDescription = "more button",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(21.dp, 21.dp)
                                    .clickable {
                                        openDialog.value = !openDialog.value
                                    })
                            Spacer(modifier = Modifier.padding(start = 20.dp))
                            Image(painter = painterResource(id = R.drawable.share),
                                contentDescription = "share button",
                                modifier = Modifier
                                    .size(21.dp, 21.dp)
                                    .clickable {
                                        val type = "text/plain"
                                        val subject = "Your subject"
                                        val shareWith = "Paylaş"
                                        val intent = Intent(Intent.ACTION_SEND)
                                        intent.type = type
                                        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                                        intent.putExtra(Intent.EXTRA_TEXT, url)
                                        ContextCompat.startActivity(
                                            context,
                                            Intent.createChooser(intent, shareWith),
                                            null
                                        )
                                    })
                            Spacer(modifier = Modifier.padding(start = 20.dp))
                            IconButton(
                                onClick = {
                                    isPressed = true
                                }, modifier = Modifier
                                    .size(21.dp, 20.dp)
                            ) {
                                color = when (amILike) {
                                    1 -> {
                                        Color(0xFFD9DD23)
                                    }
                                    0 -> {
                                        Color.White
                                    }
                                    else -> {
                                        Color.Black
                                    }
                                }
                                Icon(
                                    painter = painterResource(id = R.drawable.like),
                                    contentDescription = "like",
                                    tint = color,
                                    modifier = Modifier
                                        .size(21.dp, 20.dp)
                                )
                            }
                            if (isPressed) {
                                ProfileRefreshWithLikeQuote(viewModel, myId, quoteId)
                                isPressed = false
                                when(amILike){
                                    1 -> {
                                        color = Color.White
                                        likeCount -= 1
                                        amILike = 0
                                    }
                                    0 -> {
                                        color = Color(0xFFD9DD23)
                                        likeCount += 1
                                        amILike = 1
                                    }
                                    else -> {
                                        color = Color.Black
                                    }
                                }
                            }
                            if(mediaPressed)
                            {
                                LaunchedEffect(Unit){
                                    while(isActive){
                                        progress = (player.currentPosition / player.duration).toFloat()
                                        delay(200)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(userPhoto == null || userPhoto == "") {
            Image(
                painter = painterResource(id = R.drawable.pp),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clip(CircleShape)
                    .size(44.dp, 44.dp)
                    .clickable {
                        navController.navigate("my_profile_page/$myId")
                    }
            )
        }
        else {
            val painter = rememberImagePainter(data = Constants.MEDIA_URL + userPhoto, builder = {})
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop
                ,modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(44.dp, 44.dp)
                    .clickable {
                        navController.navigate("my_profile_page/$myId")
                    }
                    .clip(CircleShape)
            )
        }
    }
    Spacer(Modifier.padding(bottom = 15.dp))
    if(openDialog.value)
    {
        Box(Modifier.fillMaxSize()) {
            Popup(alignment = Alignment.BottomCenter, onDismissRequest = {openDialog.value = !openDialog.value}, properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = true)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier
                    .background(
                        color = Color(201, 114, 12, 204),
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
                    .size(750.dp, 100.dp)
                    .windowInsetsPadding(WindowInsets.ime))
                {
                    Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                        Divider(color = Color.Black, thickness = 3.dp, modifier = Modifier.size(width = 30.dp, height = 3.dp))
                        Spacer(modifier = Modifier.padding(top = 5.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                            Button(onClick = {
                                viewModel.deleteQuote(myId,quoteId)
                                Toast.makeText(context,"Gönderi Başarıyla Silindi.",Toast.LENGTH_LONG).show()
                            }, border = BorderStroke(1.dp, color = Color.Black),modifier = Modifier.size(250.dp,50.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                {
                                    Icon(
                                        painter = painterResource(id = R.drawable.deletepost),
                                        contentDescription = "delete post",
                                        Modifier.size(25.dp)
                                    )
                                    Spacer(modifier = Modifier.padding(start = 20.dp))
                                    Text(
                                        text = "Gönderiyi Sil",
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
    }
}




@Composable
fun ProfileRefreshWithLikeQuote(viewModel: MyProfileViewModel = hiltViewModel(), userId: Int, quoteId: Int) {
    viewModel.viewModelScope.launch {
        viewModel.amILikeFun(userId,quoteId)
        delay(200) }
}