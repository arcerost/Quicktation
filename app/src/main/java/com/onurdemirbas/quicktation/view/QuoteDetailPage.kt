@file:OptIn(ExperimentalCoilApi::class)

package com.onurdemirbas.quicktation.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
import com.onurdemirbas.quicktation.model.QuoteDetailResponseRowList
import com.onurdemirbas.quicktation.model.Sound
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.QuoteDetailViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun QuoteDetailPage(id: Int, userId: Int,navController: NavController, viewModel: QuoteDetailViewModel = hiltViewModel()) {
    viewModel.viewModelScope.launch{
        viewModel.loadQuote(userId, id)
    }
    val interactionSource =  MutableInteractionSource()
    Scaffold(Modifier.fillMaxSize(),
        topBar = {

    } , content = {
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
                    Post(navController = navController, userId, myId = userId)
                }
            }
    }, bottomBar = {
            BottomNavigationForQuoteDetailPage(navController,userId)
    })
}

@Composable
fun Post(navController: NavController, userId: Int, myId: Int, viewModel: QuoteDetailViewModel = hiltViewModel()) {
    val soundList = viewModel.soundList.value
    val errorMessage = remember { viewModel.errorMessage }
    val context = LocalContext.current
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
    else
    {
        PostView(posts = soundList, navController = navController, userId, myId = myId)
    }
}

@Composable
fun PostView(posts: List<Sound>, navController: NavController, userId: Int, myId: Int, viewModel: QuoteDetailViewModel = hiltViewModel()) {
    val scanIndex = viewModel.scanIndex
    var soundList = viewModel.soundList.value
    var checkState by remember { mutableStateOf(false) }
    val headerPost by viewModel.head.collectAsState()
    val errorMessage = remember { viewModel.errorMessage }
    val context = LocalContext.current
    val state = rememberLazyListState()
    val isScrollToEnd by remember { derivedStateOf { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index == state.layoutInfo.totalItemsCount - 1 } }
    LazyColumn(
        contentPadding = PaddingValues(top = 5.dp, bottom = 50.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        state = state
    ) {
        item {
            QuoteRow(post = headerPost, navController = navController, userId = userId, myId = myId)
        }
        items(posts) { post ->
            SoundRow(sound = post, navController = navController, userId = userId, myId = myId)
        }
        item {
            LaunchedEffect(isScrollToEnd) {
                if (isScrollToEnd) {
                    if (scanIndex != 0) {
                        if (scanIndex == -1) {
//                            Toast.makeText(context,"Yeni içerik yok",Toast.LENGTH_LONG).show()
                        } else {
                            runBlocking {
                                try {
                                    viewModel.loadQuoteScans(userId, myId, scanIndex)
                                    soundList = viewModel.soundList.value
                                    checkState = true
                                } catch (e: Exception) {
                                    Log.e("exception", "$e")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (checkState) {
        PostView(posts = soundList, navController = navController, userId, myId = myId)
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
        checkState = !checkState
    }
}

@Composable
fun QuoteRow(viewModel: QuoteDetailViewModel = hiltViewModel(), post: QuoteDetailResponseRowList, userId: Int, myId: Int, navController: NavController) {
    val quoteId  = post.id
    val username = post.username
    val quoteUrl = post.quote_url
    val amILike = post.amIlike
    val likeCount = post.likeCount
    val mainList = viewModel.head.value
    var likeCountFromVm: Int
    var amILikeFromVm: Int
    var countLike by remember { mutableStateOf(-1) }
    var color by remember { mutableStateOf(Color.Black) }
    countLike = likeCount
    color = if(amILike == 0) {
        Color.White
    }
    else
        Color.Yellow
    val quoteText = post.quote_text
    val userPhoto = post.userphoto
    val context = LocalContext.current
    val url = Constants.MEDIA_URL +quoteUrl
    val playing = remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(0F) }
    //MEDIAPLAYERSTARTED
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
        .fillMaxWidth()
        .wrapContentSize(), contentAlignment = Alignment.TopStart) {
        Surface(shape = RoundedCornerShape(15.dp), modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)) {
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
                                    .padding(16.dp)
                                    .size(10.dp, 12.dp)
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
                            color = Color.White,
                            modifier = Modifier.padding(top = 15.dp))
                        Spacer(modifier = Modifier.padding(start = 20.dp))
                        Text(text = "$countLike BEĞENME"
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
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Text(text = "-$username", color = Color.White)
                            Spacer(modifier = Modifier.padding(start = 170.dp))
                            Icon(painter = painterResource(id = R.drawable.voice_record),
                                contentDescription = "microphone button",
                                modifier = Modifier
                                    .size(21.dp, 21.dp)
                                    .clickable {
                                        navController.navigate("create_quote_sound_page/$myId/$quoteText/$userPhoto/$username/$quoteId")
                                    })
                            Spacer(modifier = Modifier.padding(start = 10.dp))
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
                            Spacer(modifier = Modifier.padding(start = 10.dp))
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
                                        mainList.amIlike = if(amILikeFromVm==0) 1 else 0
                                        mainList.likeCount = likeCountFromVm
                                    }
                                }, modifier = Modifier
                                    .size(21.dp, 20.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.like),
                                    contentDescription = "like",
                                    tint = color,
                                    modifier = Modifier
                                        .size(21.dp, 20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.padding(end = 5.dp))
                        }
                    }
                }
            }
        }
        if(userPhoto == null || userPhoto == "" || userPhoto == "null") {
            Image(
                painter = painterResource(id = R.drawable.pp),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(44.dp, 44.dp)
                    .clickable {
                        if (myId == userId) {
                            navController.navigate("my_profile_page/$myId")
                        } else {
                            navController.navigate("other_profile_page/$userId/$myId")
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
                        if (myId == userId) {
                            navController.navigate("my_profile_page/$myId")
                        } else {
                            navController.navigate("other_profile_page/$userId/$myId")
                        }
                    }
                    .clip(CircleShape)
            )
        }
    }
    Spacer(Modifier.padding(bottom = 15.dp))
}

private fun getVideoDurationSeconds(player: ExoPlayer): Int {
    val timeMs = player.duration.toInt()
    return timeMs / 1000
}

@Composable
fun SoundRow(viewModel: QuoteDetailViewModel = hiltViewModel(), sound: Sound, userId: Int, myId: Int,navController: NavController) {
    val soundId  = sound.id
    val username by remember { mutableStateOf(sound.username) }
    val soundURL = sound.soundURL
    val amILike = sound.amIlike
    val likeCount = sound.likeCount
    val mainList = viewModel.soundList.value
    var likeCountFromVm: Int
    var amILikeFromVm: Int
    var countLike by remember { mutableStateOf(-1) }
    var color by remember { mutableStateOf(Color.Black) }
    countLike = likeCount
    color = if(amILike == 0) {
        Color.White
    }
    else
        Color.Yellow
    val userPhoto by remember { mutableStateOf(sound.userphoto) }
    val url = Constants.MEDIA_URL + soundURL
    val playing = remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(0F) }
    //MEDIAPLAYERSTARTED
    var duration: Int
    val context = LocalContext.current
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
        .fillMaxWidth()
        .wrapContentSize(), contentAlignment = Alignment.TopStart) {
        Surface(shape = RoundedCornerShape(15.dp), modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)) {
            Box(modifier = Modifier
                .defaultMinSize(343.dp, 100.dp)
                .fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                Image(painter = painterResource(id = R.drawable.backgroundbottombar), contentDescription = "background", modifier = Modifier.matchParentSize(), contentScale = ContentScale.FillBounds)
                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End,modifier = Modifier.fillMaxWidth() ){
                        Text(text = "$countLike BEĞENME", color = Color.White, modifier = Modifier.padding(end = 20.dp, top= 10.dp))
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            painter = painterResource(id =
                            if (!playing.value || player.currentPosition==durationForSlider)
                                R.drawable.play
                            else
                                R.drawable.play_pause),
                            contentDescription = "image",
                            tint = Color.White, modifier = Modifier
                                .size(10.dp, 18.dp)
                                .padding(top = 0.dp)
                                .clickable {
                                    if (playing.value) {
                                        playing.value = false
                                        player.pause()
                                    } else {
                                        playing.value = true
                                        player.play()
                                    }
                                    object : CountDownTimer(durationForSlider, 100) {
                                        override fun onTick(millisUntilFinished: Long) {
                                            position = player.currentPosition.toFloat()
                                            if (player.currentPosition == durationForSlider) {
                                                playing.value = false
                                            }
                                        }

                                        override fun onFinish() {
                                        }
                                    }.start()
                                }
                        )
                        Spacer(Modifier.padding(start=25.dp))
                        Slider(
                            value = position,
                            valueRange = 0F..durationForSlider.toFloat(),
                            onValueChange = {
                                position = it
                                player.seekTo(it.toLong())
                            },
                            modifier = Modifier.size(130.dp,18.dp),
                            enabled = true,
                            colors = SliderDefaults.colors(thumbColor = Color.White, disabledThumbColor = Color.White, activeTickColor = Color.White, inactiveTickColor = Color.White, activeTrackColor = Color.White, inactiveTrackColor = Color.White, disabledActiveTickColor = Color.White, disabledActiveTrackColor = Color.White, disabledInactiveTickColor = Color.White, disabledInactiveTrackColor = Color.White))
                        Spacer(Modifier.padding(start=0.dp))
                        Text(text =
                        if(seconds > 10) {
                            "$minute:$seconds"
                        }
                        else
                        {
                            "$minute:0$seconds"
                        },
                            color = Color.White,
                            modifier = Modifier.padding(start = 15.dp))
                        Spacer(modifier = Modifier.padding(end = 20.dp))
                    }
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Start, modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)) {
                            Text(text = "-$username", color = Color.White)
                            Spacer(modifier = Modifier.padding(start = 200.dp))
                            Image(painter = painterResource(id = R.drawable.share), contentDescription = "share button", modifier = Modifier
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
                            IconButton(onClick = {
                                runBlocking {
                                    viewModel.amILikeSound(myId,soundId)
                                    likeCountFromVm = viewModel.likeCountSound
                                    amILikeFromVm = viewModel.isDeletedSound
                                    countLike = likeCountFromVm
                                    color = if(amILikeFromVm == 0) {
                                        Color.Yellow
                                    } else
                                        Color.White
                                    mainList.onEach {
                                        if(soundId == it.id)
                                        {
                                            it.amIlike = if(amILikeFromVm==0) 1 else 0
                                            it.likeCount = likeCountFromVm
                                        }
                                    }
                                }
                                }, modifier = Modifier.size(21.dp, 20.dp)) {
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
                                Icon(painter = painterResource(id = R.drawable.like), contentDescription = "like", tint =color,
                                    modifier = Modifier.size(21.dp, 20.dp))
                            }
                            Spacer(modifier = Modifier.padding(end = 5.dp))
                        }
                }
            }
        }
        if(userPhoto == null || userPhoto == "" || userPhoto == "null") {
            Image(
                painter = painterResource(id = R.drawable.pp),
                contentDescription = "profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(44.dp, 44.dp)
                    .clickable {
                        if (myId == userId) {
                            navController.navigate("my_profile_page/$myId")
                        } else {
                            navController.navigate("other_profile_page/$userId/$myId")
                        }
                    }
                    .clip(CircleShape)
            )
        }
        else {
            val painter = rememberImagePainter(data = Constants.MEDIA_URL + userPhoto, builder = {})
            Image(
                painter = painter,
                contentDescription = "profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(44.dp, 44.dp)
                    .clickable {
                        if (myId == userId) {
                            navController.navigate("my_profile_page/$myId")
                        } else {
                            navController.navigate("other_profile_page/$userId/$myId")
                        }
                    }
                    .clip(CircleShape)
            )
        }
    }
    Spacer(Modifier.padding(bottom = 15.dp))
}

@Composable
fun BottomNavigationForQuoteDetailPage(navController: NavController, userId: Int) {
    val selectedIndex = remember { mutableStateOf(0) }
    val navigationItems = listOf(
        NavigationItem("Ana Sayfa", R.drawable.home, "home_page"),
        NavigationItem("Bildirimler", R.drawable.notifications_black,"notifications_page/${userId}"),
        NavigationItem("Ekle", R.drawable.add_black,"create_quote_page/${userId}"),
        NavigationItem("Mesajlar", R.drawable.chat_black,"messages_page/${userId}"),
        NavigationItem("Profil", R.drawable.profile_black,"my_profile_page/${userId}"))
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