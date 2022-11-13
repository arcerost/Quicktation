@file:OptIn(ExperimentalCoilApi::class)

package com.onurdemirbas.quicktation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.model.QuoteFromMyProfile
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.OtherProfileViewModel
import kotlinx.coroutines.*

@Composable
fun OtherProfilePage(navController: NavController, userId: Int, myId: Int, viewModel: OtherProfileViewModel = hiltViewModel()) {
    viewModel.viewModelScope.launch{
        viewModel.loadQuotes(userId,myId)
    }
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
            OtherProfileRow(navController = navController, userId = userId, myId = myId)
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
                Image(painter = painterResource(id = R.drawable.profile_black),
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


@Composable
fun OtherProfileRow(navController: NavController, viewModel: OtherProfileViewModel = hiltViewModel(), userId: Int,myId: Int) {
    var openDialog by remember { mutableStateOf(false) }
    var reportDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val check = remember { mutableStateOf(false) }
    var reason by remember { mutableStateOf("") }
    val user = viewModel.userInfo.collectAsState()
    var amIFollow = user.value.amIfollow
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
                IconButton(onClick = { openDialog = !openDialog }) {
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
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
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
            OtherProfilePostList(navController = navController, userId, myId)
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
                        modifier = Modifier
                            .size(250.dp, 150.dp)
                        , onValueChange = {
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
                                text = "Şikayet sebebini giriniz..",
                                fontSize = 13.sp,
                                fontFamily = openSansBold
                            )
                        })
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    Button(onClick = {
                        viewModel.reportUser(myId, userId, reason)
                        Toast.makeText(context, "Raporlama Başarıyla İletildi", Toast.LENGTH_LONG)
                            .show()
                        reportDialog = !reportDialog
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                        Text(
                            text = "Şikayeti gönder",
                            fontFamily = openSansBold,
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
    if (openDialog) {
        Popup(
            alignment = Alignment.BottomCenter,
            onDismissRequest = { openDialog = !openDialog },
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
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    Divider(
                        color = Color.Black,
                        thickness = 3.dp,
                        modifier = Modifier.size(width = 30.dp, height = 3.dp)
                    )
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    Button(
                        onClick = {
                            openDialog = !openDialog
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
                                runBlocking {
                                    viewModel.followUnFollowUser(myId, userId)
                                    amIFollow = if (viewModel.amIFollow.value == -1)
                                        amIFollow
                                    else
                                        viewModel.amIFollow.value
                                    Toast.makeText(context, "Takip edildi", Toast.LENGTH_SHORT)
                                        .show()
                                    check.value = !check.value
                                }

                            } else {
                                runBlocking {
                                    viewModel.followUnFollowUser(myId, userId)
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
fun OtherProfilePostList(navController: NavController, userId: Int, myId: Int, viewModel: OtherProfileViewModel = hiltViewModel()) {
    val postList = viewModel.posts.value
    val errorMessage = remember { viewModel.errorMessage }
    val context = LocalContext.current
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
    else
    {
        OtherProfilePostListView(posts = postList, navController = navController, userId = userId, myId)
    }
}


@Composable
fun OtherProfilePostListView(posts: List<QuoteFromMyProfile>, navController: NavController, userId: Int, myId: Int ,viewModel: OtherProfileViewModel = hiltViewModel()) {
    val scanIndex = viewModel.scanIndex
    var checkState by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val errorMessage = remember { viewModel.errorMessage }
    var postList = viewModel.posts.value
    val state = rememberLazyListState()
    val isScrollToEnd by remember { derivedStateOf { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index == state.layoutInfo.totalItemsCount - 1 } }
    LazyColumn(contentPadding = PaddingValues(top = 5.dp, bottom = 50.dp), verticalArrangement = Arrangement.SpaceEvenly, state = state) {
        items(posts
            , key = { it.id }
        ) { post ->
            OtherProfileQuoteRow(post = post, navController = navController, userId = userId, myId = myId)
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
                                    viewModel.loadQuoteScans(userId, myId, scanIndex)
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
        OtherProfilePostListView(posts = postList, navController = navController, userId = userId, myId = myId)
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
fun OtherProfileQuoteRow(viewModel: OtherProfileViewModel = hiltViewModel(), post: QuoteFromMyProfile, userId: Int,  myId: Int, navController: NavController) {
    val quoteId = post.id
    val username = post.username
    val quoteUrl = post.quote_url
    val amILike = post.amIlike
    val likeCount = post.likeCount
    val quoteText = post.quote_text
    val userPhoto = post.userphoto
    val context = LocalContext.current
    var audioPermCheck by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted: Boolean ->
        audioPermCheck = isGranted
    }
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(), contentAlignment = Alignment.TopStart
    ) {
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
                    contentScale = ContentScale.FillBounds
                )
                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceAround) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .fillMaxWidth()){
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
                        if(seconds > 10) {
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
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) -> {
                                    navController.navigate("create_quote_sound_page/$myId/$userId/$quoteText/$username/$quoteId")
                                }
                                else -> {
                                    launcher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            }
                        },
                            modifier = Modifier
                                .size(19.dp, 19.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.voice_record),
                                contentDescription = "microphone button",
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
                                    viewModel.amILikeFun(myId,quoteId)
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
        if (userPhoto == null || userPhoto == "" || userPhoto == "null") {
            Image(
                painter = painterResource(id = R.drawable.pp),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(44.dp, 44.dp)
                    .clickable {
                        navController.navigate("other_profile_page/$userId/$myId")
                    }
                    .clip(CircleShape)
            )
        } else {
            val painter = rememberImagePainter(data = Constants.MEDIA_URL + userPhoto, builder = {})
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(44.dp, 44.dp)
                    .clickable {
                        navController.navigate("other_profile_page/$userId/$myId")
                    }
                    .clip(CircleShape)
            )
        }
    }
    Spacer(Modifier.padding(bottom = 15.dp))
}