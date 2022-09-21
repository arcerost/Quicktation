package com.onurdemirbas.quicktaion.view

import android.annotation.SuppressLint
import android.icu.text.UnicodeSet.CASE_INSENSITIVE
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.onurdemirbas.quicktaion.R
import com.onurdemirbas.quicktaion.model.Quotation
import com.onurdemirbas.quicktaion.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktaion.util.Constants.BASE_URL
import com.onurdemirbas.quicktaion.viewmodel.MainViewModel
import kotlinx.coroutines.*
import org.intellij.lang.annotations.Pattern
import java.util.regex.Pattern.DOTALL
import java.util.regex.Pattern.compile


@Composable
fun HomePage(navController: NavController) {
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
            PostList(navController = navController)
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
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
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
                        ) { navController.navigate("notifications_page") }
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
                        ) { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
            }
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PostList(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val postList by viewModel.mainList.collectAsState()
    val errorMessage by remember { viewModel.errorMessage }
    val context = LocalContext.current
    val scanIndex by remember { viewModel.scanIndex}
    PostListView(posts = postList, navController = navController)
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
}

@Composable
fun PostListView(posts: List<Quotation>, navController: NavController) {
    LazyColumn(contentPadding = PaddingValues(top = 5.dp, bottom = 50.dp), verticalArrangement = Arrangement.SpaceEvenly) {
        items(posts) { post ->
            MainRow(post = post, navController = navController)
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RefreshWithLike(viewModel: MainViewModel = hiltViewModel(), userId: Int, id: Int) {
    viewModel.amILike(userId,id)
    viewModel.viewModelScope.launch { delay(500)
        viewModel.loadMains(userId) }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MainRow(viewModel: MainViewModel = hiltViewModel(), post: Quotation, navController: NavController) {
    val id  by remember { mutableStateOf(post.id) }
    val username by remember { mutableStateOf(post.username) }
    val quoteUrl by remember { mutableStateOf(post.quote_url) }
    val amILike = post.amIlike
    val likeCount = post.likeCount
    val quoteText by remember { mutableStateOf(post.quote_text) }
    val userPhoto by remember { mutableStateOf(post.userphoto) }
    val userId by remember { mutableStateOf(post.userId) }
    var isPressed by remember { mutableStateOf(false) }
    val color = remember { mutableStateOf(Color.Black) }
    val mediaCheck = remember { mutableStateOf(false) }
    val url = BASE_URL+quoteUrl
    val mediaPlayer = MediaPlayer()
    mediaPlayer.apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        setDataSource(url)
        setOnPreparedListener {
            fun onPrepared(player: MediaPlayer) {
                if (mediaCheck.value) {
                    it.start()
                } else if (!mediaCheck.value) {
                    it.pause()
                }
            }
        }
        prepareAsync()
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(), contentAlignment = Alignment.TopStart) {
        Surface(shape = RoundedCornerShape(15.dp), modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .clickable {}) {
            Box(modifier = Modifier
                .defaultMinSize(343.dp, 140.dp)
                .fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                Image(painter = painterResource(id = R.drawable.background), contentDescription = "background", modifier = Modifier.matchParentSize(), contentScale = ContentScale.FillWidth)
                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start) {
                        Spacer(modifier = Modifier.padding(15.dp))
                        IconButton(onClick = { mediaCheck.value = !mediaCheck.value
                            if (mediaCheck.value) { mediaPlayer.start() } else if (!mediaCheck.value) { mediaPlayer.pause() } }) {
                            Icon(painter = painterResource(id = R.drawable.play_pause), modifier = Modifier.size(10.dp, 12.dp), contentDescription = "favorite", tint = Color.White) }
                        Spacer(modifier = Modifier.padding(start = 175.dp))
                        
                        Text(text = "$likeCount BEĞENME", color = Color.White, modifier = Modifier
                            .padding(top = 10.dp)
                            .clickable {}) }
                    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top, modifier = Modifier.padding(start = 15.dp, end = 15.dp))
                    {
                        //Text(quoteText, color = Color.White)
                        HashText(navController = navController, fullText = quoteText) {

                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Start, modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)) {
                            Text(text = "-$username", color = Color.White) }
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.End, modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)) {
                            Image(painter = painterResource(id = R.drawable.voice_record), contentDescription = "microphone button", modifier = Modifier
                                .size(21.dp, 21.dp)
                                .clickable {})
                            Spacer(modifier = Modifier.padding(10.dp))
                            Image(painter = painterResource(id = R.drawable.share), contentDescription = "share button", modifier = Modifier
                                .size(21.dp, 21.dp)
                                .clickable {})
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
                                    tint = if(amILike ==1)
                                            {
                                                Color(0xFFD9DD23)
                                            }
                                            else if (amILike ==0) {
                                                Color.White
                                        }
                                        else
                                        {
                                            Color.Black
                                        }
                                    ,
                                    modifier = Modifier
                                        .size(21.dp, 20.dp)
                                )
                            }
                          if(isPressed)
                           {
                               RefreshWithLike(viewModel,1,id)
                               isPressed = false
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
                        }
                    }
                }
            }
        }
        if(userPhoto == null || userPhoto == "") {
            Image(
                painter = painterResource(id = R.drawable.pp),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(44.dp, 44.dp)
                    .clickable {
                        //profile page
                    }
            )
        }
        else {
            val painter = rememberImagePainter(data = BASE_URL + userPhoto, builder = {})
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(44.dp, 44.dp)
                    .clickable {
                        //profile page
                    }
            )
        }
    }
    Spacer(Modifier.padding(bottom = 15.dp))
}

@Composable
fun HashText(
    navController: NavController,
    modifier: Modifier = Modifier,
    fullText: String,
    linkTextColor: Color  = Color.Yellow,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    fontSize: TextUnit = TextUnit.Unspecified,
    onClick: () -> Unit,
) {
    var startIndex = 0
    var endIndex = 0
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
        words.forEachIndexed { index, s ->
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
//                modifier.clickable{
//                    navController.navigate("notifications_page")
//                }
            } else {
                addStyle(
                    style = SpanStyle(
                        color = Color.White
                    ),
                    start = endIndex,
                    end = fullText.length
                )
                addStringAnnotation(
                    tag = "sss",
                    annotation = "",
                    start = 0,
                    end = 0
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
                        navController.navigate("notifications_page")
                    }
                }
        })
}