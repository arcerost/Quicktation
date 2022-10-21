@file:OptIn(ExperimentalCoilApi::class)

package com.onurdemirbas.quicktation.view

import android.content.Intent
import android.os.CountDownTimer
import android.widget.Toast
import androidx.compose.foundation.Image
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuoteDetailPage(id: Int, userId: Int,navController: NavController, viewModel: QuoteDetailViewModel = hiltViewModel()) {
    viewModel.viewModelScope.launch{
        viewModel.loadQuote(userId, id)
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
            Post(navController = navController, userId, myId = userId)
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

@Composable
fun RefreshWithLikeQuote(viewModel: QuoteDetailViewModel = hiltViewModel(), userId: Int, quoteId: Int) {
    viewModel.viewModelScope.launch {
        viewModel.amILike(userId,quoteId)
        delay(200) }
}

@Composable
fun RefreshWithLikeSound(viewModel: QuoteDetailViewModel = hiltViewModel(), myId: Int, quotesound_id: Int) {
    viewModel.viewModelScope.launch {
        viewModel.amILikeSound(myId,quotesound_id)
        delay(200) }
}

@Composable
fun Post(navController: NavController, userId: Int, myId: Int, viewModel: QuoteDetailViewModel = hiltViewModel()) {
    val soundList by viewModel.soundList.collectAsState()
    val errorMessage by remember { viewModel.errorMessage }
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
    val scanIndex by viewModel.scanIndex.collectAsState()
    val soundList by viewModel.soundList.collectAsState()
    var checkState by remember { mutableStateOf(false) }
    val headerPost by viewModel.head.collectAsState()
    val errorMessage by remember { viewModel.errorMessage }
    val context = LocalContext.current
    val state = rememberLazyListState()
    fun LazyListState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.firstOrNull()?.index == layoutInfo.totalItemsCount - 1
    val endOfListReached by remember { derivedStateOf { state.isScrolledToEnd() } }
    LazyColumn(contentPadding = PaddingValues(top = 5.dp, bottom = 50.dp), verticalArrangement = Arrangement.SpaceEvenly, state = state) {
        item{
            QuoteRow(post = headerPost, navController = navController, userId = userId, myId = myId)
        }
        items(posts) { post ->
            SoundRow(sound = post, navController = navController, userId = userId, myId = myId)
        }
        item {
            LaunchedEffect(endOfListReached) {
                if(scanIndex != 0) {
                    if(scanIndex == -1)
                    {
//                        Toast.makeText(context,"Yeni içerik yok",Toast.LENGTH_LONG).show()
                    }
                    else {
              //              viewModel.loadMainScans(1, scanIndex)
                            checkState = !checkState
                    }
                }
            }
        }
    }
    if(checkState) {
        if(endOfListReached)
        {
            if(scanIndex>0) {
                PostView(posts = soundList, navController = navController,userId, myId = myId)
                if (errorMessage.isNotEmpty()) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
            checkState= !checkState
        }
    }
}

@Composable
fun QuoteRow(viewModel: QuoteDetailViewModel = hiltViewModel(), post: QuoteDetailResponseRowList, userId: Int, myId: Int, navController: NavController) {
    val quoteId  = post.id
    val quoteIdFromVm = viewModel.quoteIdx.collectAsState()
    val username = post.username
    val quoteUrl = post.quote_url
    var amILike = post.amIlike
    var color: Color
    val amILikeFromVm = viewModel.isDeleted.collectAsState()
    var likeCount = post.likeCount
    val likeCountFromVm = viewModel.likeCount.collectAsState()
    val quoteText = post.quote_text
    val userPhoto = post.userphoto
    var isPressed by remember { mutableStateOf(false) }
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
                        Text(text = if(quoteId != quoteIdFromVm.value)
                        {
                            "$likeCount BEĞENME"
                        }
                        else
                        {
                            "${likeCountFromVm.value} BEĞENME"
                        }
                            , color = Color.White, modifier = Modifier
                                .padding(top = 15.dp))
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                    )
                    {
                        HashText(navController = navController, fullText = quoteText, quoteId = quoteId, userId = myId) {

                        }
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
                            Icon(painter = painterResource(id = R.drawable.voice_record),
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
                                Icon(
                                    painter = painterResource(id = R.drawable.like),
                                    contentDescription = "like",
                                    tint =
                                    if(quoteId != quoteIdFromVm.value) {
                                        when (amILike) {
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
                                    }
                                    else
                                    {
                                        when (amILikeFromVm.value) {
                                            0 -> {
                                                Color(0xFFD9DD23)
                                            }
                                            1 -> {
                                                Color.White
                                            }
                                            else -> {
                                                Color.Black
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(21.dp, 20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.padding(end = 5.dp))
                            if (isPressed) {
                                RefreshWithLikeQuote(viewModel,myId, quoteId)
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
                                isPressed = false
                            }
                        }
                    }
                }
            }
        }
        if(userPhoto == null || userPhoto == "" || userPhoto == "null") {
            Image(
                painter = painterResource(id = R.drawable.pp),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
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
                contentScale = ContentScale.FillBounds,
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
    var amILike = sound.amIlike
    var color: Color
    var likeCount = sound.likeCount
    val userPhoto by remember { mutableStateOf(sound.userphoto) }
    var isPressed by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val url = Constants.MEDIA_URL + soundURL
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
            Box(modifier = Modifier
                .defaultMinSize(343.dp, 100.dp)
                .fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                Image(painter = painterResource(id = R.drawable.backgroundbottombar), contentDescription = "background", modifier = Modifier.matchParentSize(), contentScale = ContentScale.FillBounds)
                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End,modifier = Modifier.fillMaxWidth() ){
                        Text(text = "$likeCount BEĞENME", color = Color.White, modifier = Modifier.padding(end = 20.dp, top= 10.dp))
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
                                    isPressed = true
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
                            if (isPressed) {
                                RefreshWithLikeSound(viewModel, soundId, myId)
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
                                isPressed = false
                            }
                        }
                }
            }
        }
        if(userPhoto == null || userPhoto == "" || userPhoto == "null") {
            Image(
                painter = painterResource(id = R.drawable.pp),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
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
            Image(painter = painter, contentDescription = null, contentScale = ContentScale.FillBounds, modifier = Modifier
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