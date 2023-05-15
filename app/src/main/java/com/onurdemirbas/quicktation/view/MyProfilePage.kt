@file:Suppress("DEPRECATION")

package com.onurdemirbas.quicktation.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.database.UserDatabase
import com.onurdemirbas.quicktation.model.QuoteFromMyProfile
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.MyProfileViewModel
import kotlinx.coroutines.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyProfilePage(navController: NavController,myId: Int,viewModel: MyProfileViewModel = hiltViewModel()) {
    viewModel.loadQuotes(myId)
    Scaffold(topBar = {}, content = {
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
    }, bottomBar = {
        BottomNavigationForMyProfilePage(navController = navController, userId = myId)
    })
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileRow(navController: NavController, viewModel: MyProfileViewModel = hiltViewModel(), myId: Int) {
    val context = LocalContext.current
    val openDialog2 = remember { mutableStateOf(false) }
    val user = viewModel.userInfo.collectAsState()
    var exit by remember { mutableStateOf(false) }
    val db: UserDatabase =
        Room.databaseBuilder(context, UserDatabase::class.java, "UserInfo").build()
    val userDao = db.UserDao()
    if (exit) {
        LaunchedEffect(key1 = myId) {
            viewModel.viewModelScope.launch {
                val userDb = userDao.getUser()
                userDao.delete(userDb)
                navController.navigate("login_page")
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
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
                IconButton(onClick = { openDialog2.value = !openDialog2.value }) {
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
                        contentScale = ContentScale.Crop, contentDescription = null,
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
                    Text(
                        text = "Takipçiler\n        ${user.value.followerCount}",
                        fontSize = 16.sp,
                        fontFamily = openSansFontFamily
                    )
                }
                TextButton(onClick = {
                    navController.navigate("follower_page/$myId/${user.value.id}/follows/${user.value.namesurname}/${user.value.likeCount}/${user.value.followCount}/${user.value.followerCount}/${user.value.amIfollow}")
                }, colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)) {
                    Text(
                        text = "Takip Edilenler\n            ${user.value.followCount}",
                        fontSize = 16.sp,
                        fontFamily = openSansFontFamily
                    )
                }
                Text(
                    text = "Beğeniler\n       ${user.value.likeCount}",
                    fontSize = 16.sp,
                    fontFamily = openSansFontFamily,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.padding(top = 25.dp))
            ProfilePostList(navController = navController, myId)
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
}

@Composable
fun ProfilePostList(navController: NavController, myId: Int, viewModel: MyProfileViewModel = hiltViewModel()) {
    val postList = viewModel.posts.value
    val errorMessage = remember { viewModel.errorMessage }
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
    var checkState by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scanIndex = viewModel.scanIndex
    val errorMessage = remember { viewModel.errorMessage }
    var postList = viewModel.posts.value
    val state = rememberLazyListState()
    val isScrollToEnd by remember { derivedStateOf { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index == state.layoutInfo.totalItemsCount - 1 } }
    LazyColumn(contentPadding = PaddingValues(top = 5.dp, bottom = 50.dp), verticalArrangement = Arrangement.SpaceEvenly, state = state) {
        items(posts
            , key = { it.id }
        ) { post ->
            ProfileQuoteRow(post = post, navController = navController, myId = myId)
        }
        item {
            LaunchedEffect(isScrollToEnd) {
                if(isScrollToEnd) {
                    if(scanIndex != 0) {
                        if(scanIndex == -1) {
//                            Toast.makeText(context,"Yeni içerik yok",Toast.LENGTH_LONG).show()
                        }
                        else {
                            runBlocking {
                                try {
                                    viewModel.loadMyProfileScans(myId, scanIndex)
                                    postList = viewModel.posts.value
                                    checkState = true
                                }
                                catch (e: Exception){
                                    Log.e("exception","$e")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if(checkState) {
        ProfilePostListView(posts = postList, navController = navController, myId = myId)
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
        checkState= false
    }
}

private fun getVideoDurationSeconds(player: ExoPlayer): Int {
    val timeMs = player.duration.toInt()
    return timeMs / 1000
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileQuoteRow(viewModel: MyProfileViewModel = hiltViewModel(), post: QuoteFromMyProfile, navController: NavController, myId: Int) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    val quoteId  = post.id
    val username = post.username
    val quoteUrl = post.quote_url
    val quoteText = post.quote_text
    val userPhoto = post.userphoto
    val amILike = post.amIlike
    val likeCount = post.likeCount
    val mainList = viewModel.posts.value
    var likeCountFromVm: Int
    var amILikeFromVm: Int
    var countLike by remember { mutableStateOf(-1) }
    val url = Constants.MEDIA_URL +quoteUrl
    var color by remember { mutableStateOf(Color.Black) }
    countLike = likeCount
    color = if(amILike == 0) {
        Color.White
    }
    else
        Color.Yellow
    //MEDIAPLAYERSTARTED
    val playing = remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(0F) }
    var duration: Int
    var durationForSlider by remember { mutableStateOf(0L) }
    val player = ExoPlayer.Builder(context).build()
    var minute by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }
    player.setMediaItem(MediaItem.fromUri(url))
    player.setAudioAttributes(com.google.android.exoplayer2.audio.AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build(),true)
    player.prepare()
    player.addListener(object : Player.Listener{
        @Deprecated("Deprecated in Java")
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if(playbackState == ExoPlayer.STATE_READY)
            {
                durationForSlider = player.duration
                duration = getVideoDurationSeconds(player)
                minute = duration/60
                seconds = duration%60
            }
            super.onPlayerStateChanged(playWhenReady, playbackState)
            if(playbackState == ExoPlayer.STATE_ENDED)
            {
                playing.value = false
                position = 0F
                player.seekTo(0L)
                player.pause()
            }
        }
    })
    //MEDIAPLAYERENDED
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
                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceAround) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .fillMaxWidth()){
                        Spacer(modifier = Modifier.padding(0.dp))
                        IconButton(onClick = {
                            if (playing.value) {
                                playing.value = false
                                player.pause()
                            } else {
                                playing.value = true
                                player.play()
                            }
                            object :
                                CountDownTimer(durationForSlider, 100) {
                                override fun onTick(millisUntilFinished: Long) {
                                    position =
                                        player.currentPosition.toFloat()
                                    if (player.currentPosition == durationForSlider) {
                                        playing.value = false
                                    }
                                }
                                override fun onFinish() {
                                }
                            }.start()
                        })
                        {
                            Icon(
                                painter = painterResource(id =
                                if (!playing.value || player.currentPosition==durationForSlider)
                                    R.drawable.play
                                else
                                    R.drawable.play_pause),
                                contentDescription = "play/pause",
                                tint = Color.White, modifier = Modifier
                                    .size(15.dp, 15.dp)
                            )
                        }
                        Slider(
                            value = position,
                            valueRange = 0F..durationForSlider.toFloat(),
                            onValueChange = {
                                position = it
                                player.seekTo(it.toLong())
                            },
                            modifier = Modifier.size(130.dp,50.dp),
                            enabled = true,
                            colors = SliderDefaults.colors(thumbColor = Color.White, disabledThumbColor = Color.White, activeTickColor = Color.White, inactiveTickColor = Color.White, activeTrackColor = Color.White, inactiveTrackColor = Color.White, disabledActiveTickColor = Color.White, disabledActiveTrackColor = Color.White, disabledInactiveTickColor = Color.White, disabledInactiveTrackColor = Color.White))
                        Text(text =
                        if(seconds > 9) {
                            "$minute:$seconds"
                        }
                        else
                        {
                            "$minute:0$seconds"
                        },
                            color = Color.White)
                        Spacer(modifier = Modifier.padding(start=0.dp)) // for arrangement
                        Text(
                            text = "$countLike BEĞENİ",
                            color = Color.White)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .fillMaxWidth()){
                        HashText(
                            navController = navController,
                            fullText = quoteText,
                            quoteId = quoteId,
                            userId = myId
                        )
                        Spacer(Modifier.padding(0.dp))
                        Spacer(Modifier.padding(0.dp))
                        Spacer(Modifier.padding(0.dp))
                        Spacer(Modifier.padding(0.dp))
                    }
                    Row{ Text(text = "")} //for design alignment
                    Row{Text(text = "")}  //for design alignment
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .fillMaxWidth()){
                        Text(
                            text = "-$username",
                            color = Color.White)
                        Spacer(Modifier.padding(0.dp)) // for design arrangement
                        Spacer(Modifier.padding(0.dp)) // for design arrangement
                        Spacer(Modifier.padding(0.dp)) // for design arrangement
                        Spacer(Modifier.padding(0.dp)) // for design arrangement
                        Spacer(Modifier.padding(0.dp)) // for design arrangement
                        IconButton(onClick = {
                            openDialog.value = !openDialog.value
                        },
                            modifier = Modifier
                                .size(21.dp, 21.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.options),
                                contentDescription = "more button",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(21.dp, 21.dp))
                        }
                        IconButton(onClick = {
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
                        },
                            modifier = Modifier
                                .size(19.dp, 19.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.share),
                                contentDescription = "share button",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(19.dp, 19.dp))
                        }
                        IconButton(
                            onClick = {
                                runBlocking {
                                    viewModel.amILike(myId,quoteId)
                                    likeCountFromVm = viewModel.likeCount
                                    amILikeFromVm = viewModel.isDeleted
                                    countLike = likeCountFromVm
                                    color = if(amILikeFromVm == 0) {
                                        Color.Yellow
                                    } else
                                        Color.White
                                    mainList.onEach {
                                        if(quoteId == it.id)
                                        {
                                            it.amIlike = if(amILikeFromVm==0) 1 else 0
                                            it.likeCount = likeCountFromVm
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(21.dp, 20.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.like),
                                contentDescription = "like",
                                tint = color,
                                modifier = Modifier
                                    .size(21.dp, 20.dp)
                            )
                            Spacer(modifier = Modifier.padding(end = 10.dp))
                        }
                    }
                    Row{Text(text = "")}
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
            Popup(alignment = Alignment.BottomCenter, onDismissRequest = {openDialog.value = !openDialog.value}, properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = true)) {
                Box(contentAlignment = Alignment.TopCenter, modifier = Modifier
                    .background(
                        color = Color(201, 114, 12, 204),
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
                    .size(750.dp, 150.dp)
                    .windowInsetsPadding(WindowInsets.ime))
                {
                    Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                        Divider(color = Color.Black, thickness = 3.dp, modifier = Modifier.size(width = 30.dp, height = 3.dp))
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
                        Spacer(modifier = Modifier.padding(0.dp))
                    }
                }
            }
    }
}

@SuppressLint("SuspiciousIndentation", "AutoboxingStateValueProperty")
@Composable
fun BottomNavigationForMyProfilePage(navController: NavController, userId: Int) {
    val selectedIndex = remember { mutableStateOf(0) }
    val navigationItems = listOf(
        NavigationItem("Ana Sayfa", R.drawable.homeblack, "home_page"),
        NavigationItem("Bildirimler", R.drawable.notifications_black,"notifications_page/${userId}"),
        NavigationItem("Ekle", R.drawable.add_black,"create_quote_page/${userId}"),
        NavigationItem("Mesajlar", R.drawable.chat_black,"messages_page/${userId}"),
        NavigationItem("Profil", R.drawable.profile,"my_profile_page/${userId}"))
    BottomNavigation(backgroundColor = Color.DarkGray, contentColor = LocalContentColor.current) {
        navigationItems.forEachIndexed{ index, item ->
            BottomNavigationItem(
                icon = { CustomIcon(item.iconResId, contentDescription = item.title) },
                selected = selectedIndex.value == index,
                onClick = {
                    selectedIndex.value = index
                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Black
            )
        }
    }
}