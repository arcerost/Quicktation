@file:OptIn(ExperimentalCoilApi::class)

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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
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
    var reason by remember { mutableStateOf("") }
    val user = viewModel.userInfo.collectAsState()
    var amIFollow = user.value.amIfollow
    val check = remember { mutableStateOf(false) }
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
            alignment = Alignment.Center,
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
                        viewModel.reportUser(myId, userId, reason)
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
                contentAlignment = Alignment.TopCenter, modifier = Modifier.background(
                    color = Color(4, 108, 122, 204), shape = RoundedCornerShape(
                        topStart = 20.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 0.dp
                    )
                )
                    .size(750.dp, 310.dp).windowInsetsPadding(WindowInsets.ime)
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
                    Spacer(modifier = Modifier.padding(20.dp))
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
fun OtherProfilePostList(navController: NavController, userId: Int, myId: Int, viewModel: OtherProfileViewModel = hiltViewModel()) {
    val postList by viewModel.posts.collectAsState()
    val errorMessage by remember { viewModel.errorMessage }
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
            OtherProfileQuoteRow(post = post, navController = navController, userId = userId, myId = myId)
        }
        item {
            LaunchedEffect(endOfListReached) {
                if(scanIndex != 0) {
                    if(scanIndex == -1)
                    {
//                        Toast.makeText(context,"Yeni içerik yok",Toast.LENGTH_LONG).show()
                    }
                    else {
                        runBlocking {
                            launch {
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
                OtherProfilePostListView(posts = postList, navController = navController, userId, myId)
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
fun OtherProfileQuoteRow(viewModel: OtherProfileViewModel = hiltViewModel(), post: QuoteFromMyProfile, userId: Int,  myId: Int, navController: NavController) {
    val quoteId = post.id
    val username = post.username
    val quoteUrl = post.quote_url
    var amILike = post.amIlike
    var color: Color
    var likeCount = post.likeCount
    val quoteText = post.quote_text
    val userPhoto = post.userphoto
    var isPressed by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
                        Slider(
                            value = progress,
                            onValueChange = { progress = it },
                            modifier = Modifier.size(100.dp, 50.dp),
                            enabled = false,
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                disabledThumbColor = Color.White,
                                activeTickColor = Color.White,
                                inactiveTickColor = Color.White,
                                activeTrackColor = Color.White,
                                inactiveTrackColor = Color.White,
                                disabledActiveTickColor = Color.White,
                                disabledActiveTrackColor = Color.White,
                                disabledInactiveTickColor = Color.White,
                                disabledInactiveTrackColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.padding(start = 0.dp))
                        Text(
                            text = "3:21",
                            color = Color.White,
                            modifier = Modifier.padding(top = 15.dp)
                        )
                        Spacer(modifier = Modifier.padding(start = 60.dp))
                        Text(
                            text = "$likeCount BEĞENME", color = Color.White, modifier = Modifier
                                .padding(top = 15.dp)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                    )
                    {
                        HashText(navController = navController, fullText = quoteText, quoteId = quoteId, userId = myId)
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Text(text = "-$username", color = Color.White)
                            Spacer(modifier = Modifier.padding(start = 180.dp))
                            Image(painter = painterResource(id = R.drawable.voice_record),
                                contentDescription = "microphone button",
                                modifier = Modifier
                                    .size(21.dp, 21.dp)
                                    .clickable {
                                        navController.navigate("create_quote_sound_page/$myId/$quoteText/$userPhoto/$username/$quoteId")
                                    })
                            Spacer(modifier = Modifier.padding(10.dp))
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
                            Spacer(modifier = Modifier.padding(10.dp))
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
                                OtherProfileRefreshWithLikeQuote(viewModel, 1, quoteId)
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
                            if (mediaPressed) {
                                LaunchedEffect(Unit) {
                                    while (isActive) {
                                            progress = (player.currentPosition / player.duration).toFloat()
                                        delay(200) // change this to what feels smooth without impacting performance too much
                                    }
                                }
                            }
                        }
                    }
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

@Composable
fun OtherProfileRefreshWithLikeQuote(viewModel: OtherProfileViewModel = hiltViewModel(), userId: Int, quoteId: Int) {
    viewModel.viewModelScope.launch {
        viewModel.amILikeFun(userId,quoteId)
        delay(200) }
}