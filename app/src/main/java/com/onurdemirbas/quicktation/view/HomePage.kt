@file:OptIn(ExperimentalComposeUiApi::class)

package com.onurdemirbas.quicktation.view

import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Slider
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.Room
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.*
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.database.UserDatabase
import com.onurdemirbas.quicktation.model.Quotation
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants.MEDIA_URL
import com.onurdemirbas.quicktation.viewmodel.HomeViewModel
import kotlinx.coroutines.*


@Composable
fun HomePage(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val db: UserDatabase = Room.databaseBuilder(context, UserDatabase::class.java,"UserInfo").build()
    val userDao = db.UserDao()
    val id: Int
    runBlocking {
        id = userDao.getUser().userId!!
    }
    viewModel.viewModelScope.launch{
        delay(200)
        viewModel.loadMains(id)
    }
    val interactionSource =  MutableInteractionSource()
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFDDDDDD)) {
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
            SearchBar(hint = "Ara...", modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                viewModel.searchMainList(it)
            }
            PostList(navController = navController, myId = id)
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
                        ) { navController.navigate("notifications_page/${id}") }
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
                        ) { navController.navigate("messages_page/${id}") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("my_profile_page/${id}") }
                        .size(28.dp, 31.dp))
            }
        }
    }
}


@Composable
fun PostList(navController: NavController, viewModel: HomeViewModel = hiltViewModel(), myId: Int) {
    val postList by viewModel.mainList.collectAsState()
    val errorMessage by remember { viewModel.errorMessage }
    val context = LocalContext.current
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
    else
    {
        PostListView(posts = postList, navController = navController, myId)
    }
}

@Composable
fun PostListView(posts: List<Quotation>, navController: NavController, myId:Int, viewModel: HomeViewModel = hiltViewModel()) {
    val context= LocalContext.current
    val scanIndex by viewModel.scanIndex.collectAsState()
    val postList by viewModel.mainList.collectAsState()    //cause postlist getting new values with scanindex
    val errorMessage by remember { viewModel.errorMessage }
    var checkState by remember { mutableStateOf(false) }
    val state = rememberLazyListState()
    fun LazyListState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.firstOrNull()?.index == layoutInfo.totalItemsCount - 1
    val endOfListReached by remember { derivedStateOf { state.isScrolledToEnd() } }
    LazyColumn(contentPadding = PaddingValues(top = 5.dp, bottom = 50.dp), verticalArrangement = Arrangement.SpaceEvenly, state = state) {
        items(posts, key = { quote  ->
            quote.id
        }) { post ->
            MainRow(post = post, navController = navController, myId = myId)
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
                                viewModel.loadMainScans(myId, scanIndex)
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
                PostListView(posts = postList, navController = navController, myId)
                if (errorMessage.isNotEmpty()) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
            checkState= !checkState
        }
    }
}

@Composable
fun RefreshWithLike(viewModel: HomeViewModel = hiltViewModel(), quoteId: Int, myId: Int) {
    viewModel.viewModelScope.launch {
        viewModel.amILike(myId,quoteId)
        //delay(200)
    }
}


private fun getVideoDurationSeconds(player: ExoPlayer): Int {
    val timeMs = player.duration.toInt()
    return timeMs / 1000
}



@OptIn(ExperimentalCoilApi::class)
@Composable
fun MainRow(viewModel: HomeViewModel = hiltViewModel(), post: Quotation, navController: NavController, myId: Int) {
    val quoteId  = post.id
    val username = post.username
    val quoteUrl = post.quote_url
    var amILike = post.amIlike
    var likeCount = post.likeCount
    val quoteText = post.quote_text
    val userPhoto = post.userphoto
    val userId = post.userId
    var isPressed by remember { mutableStateOf(false) }
    val url = "http://commondatastorage.googleapis.com/codeskulptor-demos/pyman_assets/ateapill.ogg"
    var color: Color
    val context = LocalContext.current
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
                                painter = painterResource(id =  if (!playing.value || player.currentPosition==durationForSlider) R.drawable.play else R.drawable.play_pause),
                                contentDescription = "image",
                                tint = Color.White, modifier = Modifier
                                    .padding(16.dp)
                                    .size(10.dp,12.dp)
                            )
                        }
                        Slider(value = position, valueRange = 0F..durationForSlider.toFloat(), onValueChange =
                        {
                            position = it
                            player.seekTo(it.toLong())
                        }
                        ,modifier = Modifier.size(130.dp,50.dp),enabled = true, colors = SliderDefaults.colors(thumbColor = Color.White, disabledThumbColor = Color.White, activeTickColor = Color.White, inactiveTickColor = Color.White, activeTrackColor = Color.White, inactiveTrackColor = Color.White, disabledActiveTickColor = Color.White, disabledActiveTrackColor = Color.White, disabledInactiveTickColor = Color.White, disabledInactiveTrackColor = Color.White))
                        Text(text = if(seconds > 10)
                        {
                            "$minute:$seconds"
                        }
                        else
                        {
                            "$minute:0$seconds"
                        }
                        , color = Color.White, modifier = Modifier.padding(top = 15.dp))
                        Spacer(modifier = Modifier.padding(start=10.dp))
                        Text(text = "$likeCount BEĞENME"
                        ,color = Color.White, modifier = Modifier
                                .padding(top = 15.dp))
                        Spacer(modifier = Modifier.padding(end=5.dp))
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
                            Image(painter = painterResource(id = R.drawable.voice_record),
                                contentDescription = "microphone button",
                                modifier = Modifier
                                    .size(21.dp, 21.dp)
                                    .clickable {})
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
            val painter = rememberImagePainter(data = MEDIA_URL + userPhoto, builder = {})
            Image(
                painter = painter,
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
    }
    Spacer(Modifier.padding(bottom = 15.dp))



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

@Composable
fun HashText(
    navController: NavController,
    modifier: Modifier = Modifier,
    fullText: String,
    quoteId: Int,
    userId: Int,
    linkTextColor: Color  = Color.Yellow,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    fontSize: TextUnit = TextUnit.Unspecified,
    onClick: () -> Unit,
) {
    var startIndex: Int
    var endIndex: Int
    val annotatedString = buildAnnotatedString {
        append(fullText)
        addStyle(
            style = SpanStyle(
                fontSize = fontSize,
                color = Color.White,
                fontFamily = openSansFontFamily
            ), start = 0, end = fullText.length
        )
        val words = fullText.split("\\s+".toRegex()).map { word ->
            word.replace("""^[,\.]|[,\.]$""".toRegex(), "")
        }
        words.forEachIndexed { _, s ->
            if (s.startsWith("#")) {
                startIndex = fullText.indexOf(s)
                endIndex = startIndex + s.length
                addStyle(
                    style = SpanStyle(
                        color = linkTextColor,
                        fontSize = fontSize,
                        fontWeight = linkTextFontWeight,
                        textDecoration = linkTextDecoration
                    ),
                    start = startIndex,
                    end = endIndex
                )
                addStringAnnotation(
                    tag = "TAG",
                    annotation = s,
                    start = startIndex,
                    end = endIndex
                )
            } else if(!s.startsWith("#")) {
                startIndex = fullText.indexOf(s)
                endIndex = startIndex + s.length
                addStyle(
                    style = SpanStyle(
                        color = Color.White
                    ),
                    start = startIndex,
                    end = endIndex
                )
                addStringAnnotation(
                    tag = "sss",
                    annotation = s,
                    start = startIndex,
                    end = endIndex
                )
            }
        }
    }
    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = {
            annotatedString.getStringAnnotations("TAG", it, it)
                .firstOrNull()?.let { a ->
                    if (a.tag == "TAG") {
                        navController.navigate("home_page")
                    }
                }
            annotatedString.getStringAnnotations("sss",it,it)
                .firstOrNull()?.let { a ->
                    if (a.tag == "sss") {
                        navController.navigate("quote_detail_page/$quoteId/$userId")
                    }
                }
        })
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, hint: String = "", onSearch: (String) -> Unit = {}) {
    var text by remember { mutableStateOf("") }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Box(modifier = modifier) {
        BasicTextField(value = text, onValueChange = {
            text = it
            onSearch(it)
        }, maxLines = 1, singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {keyboardController?.hide()
                    focusManager.clearFocus()})
            ,textStyle = TextStyle(color = Color.Black), modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                })
        if(isHintDisplayed) {
            Text(text = hint, color = Color.LightGray, modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
        }
    }
}