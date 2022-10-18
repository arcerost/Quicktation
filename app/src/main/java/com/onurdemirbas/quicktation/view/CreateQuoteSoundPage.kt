@file:OptIn(ExperimentalCoilApi::class)

package com.onurdemirbas.quicktation.view

import android.os.CountDownTimer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import kotlin.math.roundToInt

var pageCheck = mutableStateOf(true)

@Composable
fun CreateQuoteSoundPage(navController: NavController, userId: Int, quoteText: String, userPhoto: String?, userName: String, quoteId: Int) {
    val interactionSource =  MutableInteractionSource()
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFDDDDDD)) {
    }
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        CreateMain(navController = navController, userId = userId, quoteText = quoteText, userPhoto = userPhoto, userName = userName, quoteId = quoteId)
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
                        ) { navController.navigate("notifications_page/${userId}") }
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
                        ) { navController.navigate("messages_page/${userId}") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("my_profile_page/${userId}") }
                        .size(28.dp, 31.dp))
            }
        }
    }
}

@Composable
fun CreateMain(navController: NavController, userId: Int, quoteText: String, userPhoto: String?, userName: String, quoteId: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(start = 80.dp))
                Text(text = "SES OLUŞTUR", color = Color.Black, fontSize = 16.sp, fontFamily = openSansFontFamily)
                Spacer(modifier = Modifier.padding(start = 30.dp))
                Icon(painter = painterResource(id = R.drawable.how), contentDescription = "how to record", Modifier.size(23.dp))
            }
            if(pageCheck.value)
            {
                Spacer(modifier = Modifier.padding(20.dp))
                RecordRow()
            }
            else
            {
                Spacer(modifier = Modifier.padding(top = 20.dp))
                Divider(Modifier.padding(start = 20.dp, end = 20.dp), thickness = 1.dp, color = Color.Black)
                Spacer(modifier = Modifier.padding(top = 10.dp))
                QuoteRow(quoteText = quoteText, userPhoto = userPhoto ?: "", userName = userName, quoteId = quoteId, myId = userId, navController = navController)
                SaveRecord()
            }
        }
    }
}

@Composable
fun QuoteRow(userPhoto: String?, quoteText: String, userName: String,quoteId: Int, myId: Int, navController: NavController) {
    Box(modifier = Modifier
        .fillMaxWidth(), contentAlignment = Alignment.TopStart) {
        Surface(shape = RoundedCornerShape(15.dp), modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)) {
            Box(
                modifier = Modifier
                    .defaultMinSize(343.dp, 80.dp)
                    .fillMaxWidth(), contentAlignment = Alignment.TopStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.backgroundrow),
                    contentDescription = "background",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top, modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
                    Spacer(modifier = Modifier.padding(15.dp))
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start) {
                        Spacer(modifier = Modifier.padding(start = 15.dp))
                        HashText(navController = navController, fullText = quoteText, quoteId = quoteId, userId = myId) {}
                    }
                    Spacer(modifier = Modifier.padding(15.dp))
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Start) {
                        Spacer(Modifier.padding(start = 2.dp))
                        Text(text = "-$userName", color = Color.White)
                    }
                    Spacer(modifier = Modifier.padding(bottom = 5.dp))
                }
            }
        }
        if(userPhoto == null || userPhoto == "" || userPhoto == "null") {
            Image(
                painter = painterResource(id = R.drawable.pp),
                contentDescription = "user photo",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(44.dp, 44.dp)
            )
        }
        else {
            val painter = rememberImagePainter(data = Constants.MEDIA_URL + userPhoto, builder = {})
            Image(
                painter = painter,
                contentDescription = "user photo",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(44.dp, 44.dp)
            )
        }
    }
}

private data class DottedShape(val step: Dp ) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density) = Outline.Generic(Path().apply {
        val stepPx = with(density) { step.toPx() }
        val stepsCount = (size.width / stepPx).roundToInt()
        val actualStep = size.width / stepsCount
        val dotSize = Size(width = actualStep / 2, height = size.height)
        for (i in 0 until stepsCount) {
            addRect(
                Rect(
                    offset = Offset(x = i * actualStep, y = 0f),
                    size = dotSize
                )
            )
        }
        close()
    })
}

@Composable
fun SaveRecord() {
    Box(modifier = Modifier
        .fillMaxWidth(), contentAlignment = Alignment.TopStart) {
        Surface(
            shape = RoundedCornerShape(15.dp), modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(343.dp, 100.dp)
                    .fillMaxWidth(), contentAlignment = Alignment.TopStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.backgroundrow),
                    contentDescription = "background",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
                    Spacer(modifier = Modifier.padding(15.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.padding(15.dp))
                        Icon(painter = painterResource(
                            id = R.drawable.start
                        ), tint = Color.White,
                            contentDescription = "start record",
                            modifier = Modifier
                                .size(47.dp, 40.dp)
                                .clickable {
                                    //start the record
                                })
                        Spacer(modifier = Modifier.padding(start = 25.dp))
                        Box(
                            Modifier
                                .height(1.dp)
                                .size(150.dp)
                                .background(Color.White, shape = DottedShape(step = 10.dp))
                        )
//                        Divider(
//                            thickness = 2.dp,
//                            modifier = Modifier.size(150.dp, 2.dp),
//                            color = Color.White
//                        )
                        Spacer(modifier = Modifier.padding(start = 25.dp))
                        Icon(painter = painterResource(
                            id = R.drawable.save
                        ), tint = Color.White,
                            contentDescription = "start record",
                            modifier = Modifier
                                .size(47.dp, 40.dp)
                                .clickable {
                                    //stop the record and get recorded sound
                                })
                    }
                }

            }
        }
    }
}

private fun getVideoDurationSeconds(player: ExoPlayer): Int {
    val timeMs = player.duration.toInt()
    return timeMs / 1000
}

@Composable
fun RecordRow() {
    val context = LocalContext.current
    val saveCheck = remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(0F) }
    val playing = remember { mutableStateOf(false) }
    //MEDIAPLAYERSTARTED
    var duration: Int
    var durationForSlider by remember { mutableStateOf(0L) }
    val player = ExoPlayer.Builder(context).build()
    var minute by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }
    player.setMediaItem(MediaItem.fromUri(""))
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
        .fillMaxSize(), contentAlignment = Alignment.TopStart) {
        Surface(
            shape = RoundedCornerShape(15.dp), modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(343.dp, 100.dp)
                    .fillMaxWidth(), contentAlignment = Alignment.TopStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.backgroundrow),
                    contentDescription = "background",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.padding(15.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.padding(15.dp))
                        if (saveCheck.value) {
                            Icon(painter = painterResource(
                                id = R.drawable.start
                            ),
                                contentDescription = "start record",
                                modifier = Modifier
                                    .size(47.dp, 40.dp)
                                    .clickable {
                                        //start the record
                                    })
                            Divider(thickness = 1.dp, modifier = Modifier.size(150.dp, 1.dp))
                            Icon(painter = painterResource(
                                id = R.drawable.save
                            ),
                                contentDescription = "save record",
                                modifier = Modifier
                                    .size(47.dp, 40.dp)
                                    .clickable {
                                        //stop the record and get recorded sound
                                    })
                        } else {
                            Icon(
                                painter = painterResource(
                                    id =
                                    if (!playing.value || player.currentPosition == durationForSlider)
                                        R.drawable.play
                                    else
                                        R.drawable.play_pause
                                ),
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
                            Spacer(Modifier.padding(start = 25.dp))
                            Slider(
                                value = position,
                                valueRange = 0F..durationForSlider.toFloat(),
                                onValueChange = {
                                    position = it
                                    player.seekTo(it.toLong())
                                },
                                modifier = Modifier.size(130.dp, 18.dp),
                                enabled = true,
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
                            Spacer(Modifier.padding(start = 0.dp))
                            Text(
                                text = "3:21",
//                        if(seconds > 10) {
//                            "$minute:$seconds"
//                        }
//                        else
//                        {
//                            "$minute:0$seconds"
//                        },
                                color = Color.White,
                                modifier = Modifier.padding(start = 15.dp)
                            )
                            Spacer(modifier = Modifier.padding(start = 10.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.deletesave),
                                tint = Color.White,
                                modifier = Modifier.size(17.dp),
                                contentDescription = "delete save record"
                            )
                            Spacer(modifier = Modifier.padding(end = 20.dp))
                        }
                    }
                }
            }
        }
    }
}