package com.onurdemirbas.quicktation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.model.Quotation
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.HomeViewModel

@Composable
fun SearchPage(myId: Int, text:String, navController:NavController,viewModel: HomeViewModel= hiltViewModel()) {
    viewModel.searchQuote(userId = myId,"quote",text,0)
    val interactionSource =  MutableInteractionSource()
    Scaffold(Modifier.fillMaxSize(), bottomBar = {
        BottomNavigation {
            Surface(modifier = Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.backgroundbottombar), contentDescription = "background", contentScale = ContentScale.FillWidth)
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
                            ) { navController.navigate("my_profile_page/${myId}") }
                            .size(28.dp, 31.dp))
                }
            }
        }
    }) {
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
                PostListSearch(navController,viewModel,myId)
            }
        }
    }
}

private fun getVideoDurationSeconds(player: ExoPlayer): Int {
    val timeMs = player.duration.toInt()
    return timeMs / 1000
}

@Composable
fun PostListSearch(navController: NavController, viewModel: HomeViewModel = hiltViewModel(), myId: Int) {
    val postList = viewModel.quotes.value
    val errorMessage = remember { viewModel.errorMessage }
    val context = LocalContext.current
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
    else
    {
        PostListSearchView(posts = postList, navController = navController, myId)
    }
}

@Composable
fun PostListSearchView(posts: List<Quotation>, navController: NavController, myId:Int, viewModel: HomeViewModel = hiltViewModel()) {
    val context= LocalContext.current
    val scanIndex = viewModel.scanIndex
    val postList = viewModel.quotes.value  //cause postlist getting new values with scanindex
    val errorMessage = remember { viewModel.errorMessage }
    var checkState by remember { mutableStateOf(false) }
    val state = rememberLazyListState()
    val isScrollToEnd by remember { derivedStateOf { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index == state.layoutInfo.totalItemsCount - 1 } }
    LazyColumn(contentPadding = PaddingValues(top = 5.dp, bottom = 50.dp), verticalArrangement = Arrangement.SpaceEvenly, state = state) {
        items(posts, key = {
            it.id
        }) { post ->
            MainSearchRow(post = post, navController = navController, myId = myId)
        }
        item {
            LaunchedEffect(isScrollToEnd) {
                if(scanIndex != 0) {
                    if(scanIndex == -1)
                    {
//                        Toast.makeText(context,"Yeni içerik yok",Toast.LENGTH_LONG).show()
                    }
                    else {
                        viewModel.loadMainScans(myId, scanIndex)
                        checkState = !checkState
                    }
                }
            }
        }
    }
    if(checkState) {
        if(isScrollToEnd)
        {
            if(scanIndex>0) {
                PostListSearchView(posts = postList, navController = navController, myId)
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
fun MainSearchRow(viewModel: HomeViewModel = hiltViewModel(), post: Quotation, navController: NavController, myId: Int) {
    var audioPermCheck by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted: Boolean ->
        audioPermCheck = isGranted
    }
    val quoteId  = post.id
    val username = post.username
    val quoteUrl = post.quote_url
    var amILike = post.amIlike
    var likeCount = post.likeCount
    val quoteText = post.quote_text
    val userPhoto = post.userphoto
    val userId = post.userId
    var isPressed by remember { mutableStateOf(false) }
    val url = Constants.MEDIA_URL +quoteUrl
    var color: Color
    val context = LocalContext.current
    val playing = remember { mutableStateOf(false) }
    //MEDIAPLAYERSTARTED
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
        .fillMaxWidth(), contentAlignment = Alignment.TopStart) {
        Surface(shape = RoundedCornerShape(15.dp), modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .clickable {
                navController.navigate("quote_detail_page/$quoteId/$myId")
            }) {
            Box(
                modifier = Modifier
                    .defaultMinSize(343.dp, 140.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("quote_detail_page/$quoteId/$myId")
                    }, contentAlignment = Alignment.TopStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.backgroundrow),
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
                        Spacer(modifier = Modifier.padding(start = 25.dp))
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
//                                    .padding(start = 0.dp)
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
                            color = Color.White,
                            modifier = Modifier.padding(top = 15.dp))
                        Spacer(modifier = Modifier.padding(start=30.dp))
                        Text(
                            text = "$likeCount BEĞENİ" ,
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 15.dp))
                        Spacer(modifier = Modifier.padding(end=10.dp))
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    {
                        HashText(navController = navController, fullText = quoteText, quoteId = quoteId, userId = myId)
                        Spacer(modifier = Modifier.padding(top = 40.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Text(
                                text = "-$username",
                                color = Color.White)
                            Spacer(modifier = Modifier.padding(start = 160.dp))
                            Image(
                                painter = painterResource(id = R.drawable.voice_record),
                                contentDescription = "microphone button",
                                modifier = Modifier
                                    .size(21.dp, 21.dp)
                                    .clickable {
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
                                    })
                            Spacer(modifier = Modifier.padding(start = 20.dp))
                            Image(
                                painter = painterResource(id = R.drawable.share),
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
                                },
                                modifier = Modifier
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
                                Spacer(modifier = Modifier.padding(end = 10.dp))
                            }
                            if (isPressed) {
                                RefreshWithLike(viewModel, quoteId, myId)
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
                    .clip(CircleShape)
                    .clickable {
                        if (myId == userId) {
                            navController.navigate("my_profile_page/$myId")
                        } else {
                            navController.navigate("other_profile_page/$userId/$myId")
                        }
                    }
            )
        }
    }
    Spacer(Modifier.padding(bottom = 10.dp))
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                }
                Lifecycle.Event.ON_RESUME -> {
                }
                Lifecycle.Event.ON_DESTROY -> {
                    player.run {
                        stop()
                        release()
                    }
                }
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}