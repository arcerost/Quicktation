package com.onurdemirbas.quicktation.view

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.CountDownTimer
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FileUtils
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.QuicktationTheme
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.viewmodel.CreateQuoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

private var player = MediaPlayer()
private var pageCheck by mutableStateOf(false)
private var seconds by mutableStateOf(0)
private var minutes by mutableStateOf(0)
private val outputFile = Environment.getExternalStorageDirectory() .absolutePath + "/Kayıt.3gpp"
private lateinit var recorder: MediaRecorder
@Composable
fun CreateQuotePage(navController: NavController, userId: Int, viewModel: CreateQuoteViewModel = hiltViewModel()) {
    QuicktationTheme {
        val interactionSource =  MutableInteractionSource()
        Scaffold(Modifier.fillMaxSize()) {
            Surface(Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.mainbg), contentDescription = "background image", contentScale = ContentScale.FillHeight)
                Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.padding(0.dp))
                        Spacer(modifier = Modifier.padding(0.dp))
                        Text(text = "GÖNDERİ OLUŞTUR", color = Color.Black, fontSize = 16.sp, fontFamily = openSansBold)
                        Spacer(modifier = Modifier.padding(0.dp))
                        Spacer(modifier = Modifier.padding(0.dp))
                        Icon(painter = painterResource(id = R.drawable.how), contentDescription = "how to record",
                            Modifier
                                .size(23.dp)
                                .clickable {
                                    //popup
                                })
                    }
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Divider(Modifier.padding(start = 20.dp, end = 20.dp), thickness = 2.dp, color = Color.Black)
                    CreateRow(userId,navController,viewModel)
                }
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
                    Image(painter = painterResource(id = R.drawable.homeblack),
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
                    Image(painter = painterResource(id = R.drawable.add),
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
                            ) { navController.navigate("my_profile_page/${userId}") }
                            .size(28.dp, 31.dp))
                }
            }
        }
    }
}



private fun startRecord() {
    recorder= MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        setOutputFile(outputFile)
        setMaxDuration(180000)
        try {
            prepare()
            start()
        }
        catch (e: Exception)
        {
            Log.d("exception","exception from start record ${e.message}")
        }
    }
}
private fun stopRecord() {
    try {
        recorder.stop()
        recorder.reset()
    }
    catch(e: Exception) {
        Log.d("exception","exception from stop record ${e.message}")
    }
}
private fun startPlayer(mediaPlayer: MediaPlayer) {
    mediaPlayer.apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        setOnCompletionListener {
            stopPlayer(player)
            pageCheck = false
            seconds = 0
            minutes = 0
        }
        try {
            mediaPlayer.setDataSource(outputFile)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
        catch (e: Exception)
        {
            Log.d("exception","${e.message}")
        }
    }
}
private fun stopPlayer(mediaPlayer: MediaPlayer) {
    try {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }
    catch (e: Exception)
    {
        Log.d("exception","${e.message}")
    }
}
@Composable
fun CreateRow(myId: Int, navController: NavController, viewModel: CreateQuoteViewModel) {
    val edit = remember { mutableStateOf(TextFieldValue()) }
    var position by remember { mutableStateOf(0F) }
    var durationForSlider by remember { mutableStateOf(0L) }
    var audioPermCheck by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var pageCheck by remember { mutableStateOf(false) }
    //timer started
    var timerCheck by remember { mutableStateOf(false) }
    if(timerCheck)
    {
        LaunchedEffect(Unit) {
            while(seconds<180) {
                delay(1000)
                seconds++
                minutes = seconds / 60
                if(seconds>=180)
                {
                    timerCheck= false
                    seconds=0
                }
            }
        }
    }
    //timer  end

    //permission control start
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        audioPermCheck = isGranted
    }
    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) -> {
            audioPermCheck = true
        }
        else -> {
            launcher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
    //permission control end
    Spacer(modifier = Modifier.padding(top = 20.dp))
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(), contentAlignment = Alignment.Center) {
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            TextField(
                value = edit.value,
                onValueChange =
                {
                    edit.value = it
                },
                modifier = Modifier
                    .defaultMinSize(350.dp, 230.dp),
                placeholder = { Text(text = "Bir şeyler yaz..") },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color(1, 6, 2, 255),
                    placeholderColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    cursorColor = Color.White,
                    unfocusedLabelColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.padding(top = 50.dp))
            if(pageCheck)   //player
            {
                Column(Modifier.fillMaxWidth()) {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = {
                            startPlayer(player)
                            durationForSlider = player.duration.toLong()
                            object :
                                CountDownTimer(durationForSlider, 100) {
                                override fun onTick(millisUntilFinished: Long) {
                                    position = player.currentPosition.toFloat()
                                    if (player.currentPosition == durationForSlider.toInt()) {
                                        stopPlayer(player)
                                    }
                                }
                                override fun onFinish() {
                                    stopPlayer(player)
                                }
                            }.start()
                        },Modifier.size(40.dp)) {
                            Icon(painter = painterResource(id = R.drawable.play2), contentDescription = "play record", Modifier.size(40.dp), tint = Color.Black)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.wrapContentSize()) {
                            Text(text =
                            if(seconds in 0..9) {
                                "0:0$seconds"
                            }
                            else if (seconds in 10..59)
                            {
                                "0:$seconds"
                            }
                            else
                            {
                                if(seconds==60 || seconds ==  120 || seconds == 180)
                                {
                                    "$minutes:00"
                                }
                                else if(seconds in 61..69  || seconds in 121..129)
                                {
                                    if(seconds in 61..69)
                                    {
                                        "$minutes:0${seconds-60}"
                                    }
                                    else
                                        "$minutes:0${seconds-120}"
                                }
                                else
                                {
                                    if(seconds in 70..119)
                                    {
                                        "$minutes:${seconds-60}"

                                    }
                                    else
                                        "$minutes:${seconds-120}"
                                }
                            }, fontSize = 20.sp)
                            Spacer(modifier = Modifier.padding(top=10.dp))
                            Slider(value = position, valueRange = 0F..durationForSlider.toFloat(), onValueChange = {
                                position = it
                                player.seekTo(it.toInt())
                            },
                                modifier = Modifier.size(150.dp,1.dp),
                                enabled = true,
                                colors = SliderDefaults.colors(thumbColor = Color.Black, disabledThumbColor = Color.Black, activeTickColor = Color.Black, inactiveTickColor = Color.Black, activeTrackColor = Color.Black, inactiveTrackColor = Color.Black, disabledActiveTickColor = Color.Black, disabledActiveTrackColor = Color.Black, disabledInactiveTickColor = Color.Black, disabledInactiveTrackColor = Color.Black))
                            Spacer(modifier = Modifier.padding(top=10.dp))
                        }
                        IconButton(onClick = {
                            stopPlayer(player)
                            pageCheck = !pageCheck
                            timerCheck = false
                            seconds = 0
                            minutes = 0
                        },Modifier.size(40.dp)) {
                            Icon(painter = painterResource(id = R.drawable.stop2), contentDescription = "stop player", Modifier.size(40.dp), tint = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.padding(top =5.dp))
                }
            }
            else    //recorder
            {
                Column(Modifier.fillMaxWidth()) {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = {
                            if(audioPermCheck)
                            {
                                startRecord()
                                timerCheck = true
                            }
                        },Modifier.size(40.dp)) {
                            Icon(painter = painterResource(id = R.drawable.record2), contentDescription = "start record", Modifier.size(40.dp), tint = Color.Black)
                        }
                        Text(text =
                        if(seconds in 0..9) {
                            "0:0$seconds"
                        }
                        else if (seconds in 10..59) {
                            "0:$seconds"
                        }
                        else {
                            if(seconds==60 || seconds ==  120 || seconds == 180)
                            {
                                "$minutes:00"
                            }
                            else if(seconds in 61..69  || seconds in 121..129)
                            {
                                if(seconds in 61..69)
                                {
                                    "$minutes:0${seconds-60}"
                                }
                                else
                                    "$minutes:0${seconds-120}"
                            }
                            else
                            {
                                if(seconds in 70..119)
                                {
                                    "$minutes:${seconds-60}"

                                }
                                else
                                    "$minutes:${seconds-120}"
                            }
                        }, fontSize = 20.sp)
                        IconButton(onClick = {
                            if(audioPermCheck)
                            {
                                if(seconds > 5)
                                {
                                    stopRecord()
                                    timerCheck = false
                                    pageCheck = !pageCheck
                                }
                                else
                                {
                                    timerCheck = false
                                    seconds = 0
                                    minutes = 0
                                    Toast.makeText(context, "5 saniyeden daha uzun bir ses kaydetmelisiniz..", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },Modifier.size(40.dp)) {
                            Icon(painter = painterResource(id = R.drawable.stop2), contentDescription = "stop record", Modifier.size(40.dp), tint = Color.Black)
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally, modifier =Modifier.fillMaxSize()) {
                Button(onClick = {
                    val file = File(outputFile)
                    val bytes: ByteArray = FileUtils.readFileToByteArray(file)
                    val encoded = Base64.encodeToString(bytes, Base64.DEFAULT)
                    viewModel.viewModelScope.launch {
                        viewModel.sendQuote(myId,encoded,edit.value.text)
                        delay(500)
                        val quoteId = viewModel.quoteId
                        Toast.makeText(context,"Ses başarıyla gönderildi", Toast.LENGTH_SHORT).show()
                        if(quoteId.value!=0)
                        {
                            navController.navigate("quote_detail_page/${quoteId.value}/$myId")
                        }
                    }
                }, modifier = Modifier
                    .size(120.dp, 50.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow) ,shape = RoundedCornerShape(20.dp)) {
                    Text(text = "Gönder", fontFamily = openSansFontFamily, color = Color.Black, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    }
}