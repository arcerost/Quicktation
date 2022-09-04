package com.onurdemirbas.quicktaion.view

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.onurdemirbas.quicktaion.R
import com.onurdemirbas.quicktaion.model.MainResponse
import com.onurdemirbas.quicktaion.viewmodel.MainViewModel

@Composable
fun HomePage(navController: NavController) {

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFDDDDDD)) {
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.padding(vertical = 40.dp))
        Image(
            painter = painterResource(id = R.drawable.quicktation_black),
            contentDescription = "quicktationlogo",
            Modifier.size(181.dp, 55.dp)
        )
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        )
        {
            //Lazy column row
            MainRow()
            MainRow()
            MainRow()
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
                        .clickable { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.notifications_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.add_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.chat_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
            }
        }
    }
}


@Composable
fun PostList(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val postList by remember { viewModel.mainList }
    PostListView(posts = postList, navController = navController)
}

@Composable
fun PostListView(posts: List<MainResponse>,navController: NavController) {
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(posts) { post ->
           // MainRow(post =  post)
        }
    }
}
@Composable
fun MainRow() {
    val colorControl = MutableInteractionSource()
    val isPressed by colorControl.collectIsPressedAsState()
    val color = if (isPressed) Color(0xFFD9DD23) else Color.White
    val mediaCheck = remember { mutableStateOf(false)}
    val url = "http://cld3097web.audiovideoweb.com/va90web25003/companions/Foundations%20of%20Rock/13.01.mp3" // your URL here
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
                if(mediaCheck.value)
                {
                    it.start()
                }
                else if(!mediaCheck.value)
                {
                    it.pause()
                }
            }
        }
        prepareAsync()
    }
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart)
    {
        Surface(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                .clickable {
                    //quote detail page
                }
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(343.dp, 140.dp)
                    .fillMaxWidth(), contentAlignment = Alignment.TopStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "background",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillWidth
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
                            mediaCheck.value = !mediaCheck.value
                            if(mediaCheck.value)
                            {
                                mediaPlayer.start()
                            }
                            else if(!mediaCheck.value)
                            {
                                mediaPlayer.pause()
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.play_pause),
                                modifier = Modifier.size(10.dp, 12.dp),
                                contentDescription = "favorite",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.padding(start = 175.dp))
                        Text(
                            text = "100 beğeni",
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .clickable {
                                    //beğenenlerin görüntüleneceği popup
                                }
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                    )
                    {
                        Text(
                            "#Hayat bizi resmen dört işlemle sınar; Gerçeklerle çarpar, ayrılıklarla böler, #insanlık tan çıkarır ve sonunda topla kendini der. ",
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Spacer(modifier = Modifier.padding(5.dp))
                            Text(text = "-muhtesemozgur9", color = Color.White)
                            Spacer(modifier = Modifier.padding(start = 100.dp))
                            Image(
                                painter = painterResource(id = R.drawable.voice_record),
                                contentDescription = "microphone button",
                                modifier = Modifier
                                    .size(21.dp, 21.dp)
                                    .clickable {

                                    }
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            Image(
                                painter = painterResource(id = R.drawable.share),
                                contentDescription = "share button",
                                modifier = Modifier
                                    .size(21.dp, 21.dp)
                                    .clickable {

                                    }
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            IconButton(
                                onClick = {

                                }, interactionSource = colorControl, modifier = Modifier
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
                        }
                    }
                }
            }
        }
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
}
