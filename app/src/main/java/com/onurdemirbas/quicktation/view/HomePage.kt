@file:OptIn(ExperimentalComposeUiApi::class)

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
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.onurdemirbas.quicktation.model.Quotation
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants.MEDIA_URL
import com.onurdemirbas.quicktation.viewmodel.HomeViewModel
import kotlinx.coroutines.*

@Composable
fun HomePage(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val interactionSource =  MutableInteractionSource()
    var ready by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db: UserDatabase = Room.databaseBuilder(context, UserDatabase::class.java,"UserInfo").build()
    val userDao = db.UserDao()
    val id: Int
    runBlocking {
        id = userDao.getUser().userId!!
        viewModel.loadMains(id)
        ready = true
    }
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
                            ) { navController.navigate("notifications_page/${id}") }
                            .size(28.dp, 31.dp))
                    Image(painter = painterResource(id = R.drawable.add_black),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(
                                interactionSource,
                                indication = null
                            ) { navController.navigate("create_quote_page/$id") }
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
    }) {
        Surface(Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.mainbg), contentDescription = "background image", contentScale = ContentScale.FillHeight)
        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
                SearchBar(id,viewModel,navController)
                if(ready)
                {
                    PostList(navController = navController, myId = id)
                }
        }
    }
}
private fun getVideoDurationSeconds(player: ExoPlayer): Int {
    val timeMs = player.duration.toInt()
    return timeMs / 1000
}

@Composable
fun PostList(navController: NavController, viewModel: HomeViewModel = hiltViewModel(), myId: Int) {
    val postList = viewModel.mainList.value
    val errorMessage = remember { viewModel.errorMessage }
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
    val scanIndex = viewModel.scanIndex
    var postList = viewModel.mainList.value
    val errorMessage = remember { viewModel.errorMessage }
    var checkState by remember { mutableStateOf(false) }
    val state = rememberLazyListState()
    val isScrollToEnd by remember { derivedStateOf { state.layoutInfo.visibleItemsInfo.lastOrNull()?.index == state.layoutInfo.totalItemsCount - 1 } }
    LazyColumn(contentPadding = PaddingValues(top = 5.dp, bottom = 50.dp), verticalArrangement = Arrangement.SpaceEvenly, state = state) {
        items(posts
            , key = { it.id }
        ) { post ->
            MainRow(post = post, navController = navController, myId = myId)
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
                                viewModel.loadMainScans(myId, scanIndex)
                                postList = viewModel.mainList.value
                                checkState = true
                            }
                        }
                    }
                }
            }
        }
    }
    if(checkState) {
        PostListView(posts = postList, navController = navController, myId = myId)
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
        checkState= false
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MainRow(viewModel: HomeViewModel = hiltViewModel(), post: Quotation, navController: NavController, myId: Int) {
    val mainList = viewModel.mainList.value
    var audioPermCheck by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){isGranted: Boolean ->
        audioPermCheck = isGranted
    }
    val quoteId  = post.id
    val username = post.username
    val quoteUrl = post.quote_url
    val amILike = post.amIlike
    val likeCount = post.likeCount
    var countLike by remember { mutableStateOf(-1) }
    val quoteText = post.quote_text
    val userPhoto = post.userphoto
    val userId = post.userId
    val url = MEDIA_URL+quoteUrl
    var likeCountFromVm: Int
    var amILikeFromVm: Int
    var color by remember { mutableStateOf(Color.Black) }
    val context = LocalContext.current
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
        .fillMaxWidth(), contentAlignment = Alignment.TopStart) {
        Surface(shape = RoundedCornerShape(15.dp), modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .clickable {
                navController.navigate("quote_detail_page/$quoteId/$myId")
            }) {
            Box(modifier = Modifier
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
                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceAround) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth()){
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
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth()){
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
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth()){
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
        if(userPhoto == null || userPhoto == "" || userPhoto == "null") {
            Image(
                painter = painterResource(id = R.drawable.pp),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
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
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
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
fun SearchBar(myId: Int,viewModel: HomeViewModel, navController: NavController) {
    var text by remember { mutableStateOf("") }
    var textExpand by remember { mutableStateOf(false) }
    val users = viewModel.user.collectAsState().value
    var userId by remember { mutableStateOf(-1) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Box {
            DropdownMenu(expanded = textExpand, onDismissRequest = { textExpand = false }, modifier = Modifier.fillMaxWidth(), properties = PopupProperties(focusable = false, dismissOnClickOutside = true, dismissOnBackPress = true)){
                users.forEach{
                    DropdownMenuItem(onClick = {
                        userId = it.id
                        if(myId == userId)
                            navController.navigate("my_profile_page/$myId")
                        else
                            navController.navigate("other_profile_page/$userId/$myId")
                    }) {
                        Text(text = it.username)
                    }
                }
        }
        TextField(value = text,
            onValueChange = {
                text = it
                if(text.startsWith("@"))
                {
                    if(text.length>3)
                    {
                        viewModel.viewModelScope.launch {
                            val sendText = text.removeRange(0,1)
                            viewModel.search(myId,"user",sendText,0)
                            textExpand = true
                        }
                    }
                }
            },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    navController.navigate("search_page/$myId/$text")
                }),
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            placeholder = {
                Text(text = "Ara..", fontSize = 12.sp)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "search",
                    modifier = Modifier.size(30.dp),
                    tint = Color.Black
                )
            },
            shape = CircleShape,
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
    }
}