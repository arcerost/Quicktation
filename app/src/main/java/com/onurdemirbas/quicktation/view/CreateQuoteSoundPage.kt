@file:OptIn(ExperimentalCoilApi::class)
@file:Suppress("DEPRECATION")

package com.onurdemirbas.quicktation.view

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.CountDownTimer
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FileUtils
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.CreateQuoteSoundViewModel
import kotlinx.coroutines.delay
import java.io.File
import java.util.*


private var player = MediaPlayer()
private var pageCheck by mutableStateOf(false)
private var recCheck by mutableStateOf(0)
private var seconds by mutableStateOf(0)
private var minutes by mutableStateOf(0)
private val outputFile = Environment.getExternalStorageDirectory() .absolutePath + "/recording.3gpp"
private lateinit var recorder: MediaRecorder
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateQuoteSoundPage(navController: NavController, userId: Int, postUserId: Int, quoteText: String, userName: String, quoteId: Int, viewModel: CreateQuoteSoundViewModel = hiltViewModel()) {
    viewModel.getPhoto(postUserId, userId)
    val userPhoto = viewModel.userInfo.collectAsState().value.photo
    Scaffold(topBar = {},
        content = {
            Surface(Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.mainbg),
                    contentDescription = "background image",
                    contentScale = ContentScale.FillHeight
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                CreateMain(
                    navController = navController,
                    userId = userId,
                    quoteText = quoteText,
                    userPhoto = userPhoto,
                    userName = userName,
                    quoteId = quoteId
                )
            }
        },
        bottomBar = {
            BottomNavigationForCreateQuoteSoundPage(navController = navController, userId = userId)
        }
    )
}

private fun startRecord() {
    recorder= MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        setOutputFile(outputFile)
        setMaxDuration(180000)
        //permission control?
        try {
            prepare()
            start()
        }
        catch (e: Exception)
        {
            Log.d("exam","exception from start ${e.message}")
        }
    }
}
private fun stopRecord() {
    recorder.stop()
    recorder.reset()
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
            pageCheck = !pageCheck
            recCheck = 0
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
            Log.d("exception","$e")
        }
    }
}
private fun stopPlayer(mediaPlayer: MediaPlayer) {
    mediaPlayer.stop()
    mediaPlayer.reset()
}
private fun getVideoDurationSeconds(player: MediaPlayer): Int {
    val timeMs = player.duration
    return timeMs / 1000
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreateMain(navController: NavController, userId: Int, quoteText: String, userPhoto: String?, userName: String, quoteId: Int,  viewModel: CreateQuoteSoundViewModel = hiltViewModel()) {
    var position by remember { mutableStateOf(0F) }
    var durationForSlider by remember { mutableStateOf(0L) }
    var audioPermCheck by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
                    recCheck=2
                }
            }
        }
    }
    //timer  end

    //permission control start
    val permissionState = rememberMultiplePermissionsState(permissions = listOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
    if(permissionState.allPermissionsGranted)
    {
        audioPermCheck = true
    }
    else
    {
        LaunchedEffect(key1 = Unit)
        {
            permissionState.launchMultiplePermissionRequest()
        }
    }
    //permission control end
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(),
        contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
            ) {
                Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .padding(end = 0.dp)
                    .fillMaxWidth()) {
                    Spacer(modifier = Modifier.padding(0.dp))
                    Spacer(modifier = Modifier.padding(0.dp))
                    Text(text = "SES OLUŞTUR", color = Color.Black, fontSize = 16.sp, fontFamily = openSansBold)
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
                QuoteRow(navController = navController, userId = userId, quoteText = quoteText, userPhoto = userPhoto, userName = userName, quoteId = quoteId)
            }
        }
        Spacer(modifier = Modifier.padding(top = 10.dp))
        if(pageCheck)
        {
            val x = getVideoDurationSeconds(player)
            minutes = x/60
            seconds = x%60
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 75.dp)
            ) {
                Text(text =
                    if(seconds > 10) {
                        "$minutes:$seconds"
                    }
                    else
                    {
                        "$minutes:0$seconds"
                    }, modifier = Modifier.size(80.dp,54.dp), fontFamily = openSansFontFamily, fontSize = 40.sp)
                Spacer(modifier = Modifier.padding(top = 5.dp))
                Slider(
                    value = position,
                    valueRange = 0F..durationForSlider.toFloat(),
                    onValueChange = {
                        position = it
                        player.seekTo(it.toInt())
                    },
                    modifier = Modifier.size(300.dp,1.dp),
                    enabled = true,
                    colors = SliderDefaults.colors(thumbColor = Color.Black, disabledThumbColor = Color.Black, activeTickColor = Color.Black, inactiveTickColor = Color.Black, activeTrackColor = Color.Black, inactiveTrackColor = Color.Black, disabledActiveTickColor = Color.Black, disabledActiveTrackColor = Color.Black, disabledInactiveTickColor = Color.Black, disabledInactiveTrackColor = Color.Black))
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Text(text =
                when (recCheck) {
                    0 -> {
                        "Kaydetmeye Başla"
                    }
                    1 -> {
                        "Kayıt Ediliyor.."
                    }
                    2 -> {
                        "Kayıt Tamamlandı."
                    }
                    3 -> {
                        ""
                    }
                    else -> {
                        ""
                    }
                }, fontFamily = openSansFontFamily, fontSize = 20.sp)
                Spacer(modifier = Modifier.padding(top = 50.dp))
                IconButton(onClick = {
                    when (recCheck) {
                        0 -> {
                            if(audioPermCheck)
                            {
                                try {
                                    startRecord()
                                    recCheck = 1
                                    timerCheck = true
                                }
                                catch (e: Exception)
                                {
                                    Log.d("exam","exception from start onclick: $e")
                                }

                            }
                        }
                        1 -> {
                            if(audioPermCheck)
                            {
                                try {
                                    stopRecord()
                                    recCheck = 2
                                    timerCheck = false
                                }
                                catch (e: Exception)
                                {
                                    Log.d("exam","exception from stop onclick: $e")
                                }
                            }
                        }
                        2 -> {
                            pageCheck = !pageCheck
                            startPlayer(mediaPlayer = player)
                            recCheck = 3
                        }
                        3  -> {
                            stopPlayer(player)
                            pageCheck = !pageCheck
                            recCheck = 0
                            seconds = 0
                            minutes = 0
                        }
                    }
                }) {
                    when (recCheck) {
                        0 -> {
                            Icon(painter = painterResource(id = R.drawable.record), contentDescription = "start record", Modifier.size(50.dp), tint = Color.Yellow)
                        }
                        1 -> {
                            Icon(painter = painterResource(id = R.drawable.stop2), contentDescription = "stop record", Modifier.size(50.dp), tint = Color.Yellow)
                        }
                        2 -> {
                            Icon(painter = painterResource(id = R.drawable.play), contentDescription = "play record", Modifier.size(50.dp), tint = Color.Yellow)
                        }
                        3 -> {
                            Icon(painter = painterResource(id = R.drawable.stop2), contentDescription = "stop record", Modifier.size(50.dp), tint = Color.Yellow)
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(top = 50.dp))
                Button(onClick = {
                    val file = File(outputFile)
                    val bytes: ByteArray = FileUtils.readFileToByteArray(file)
                    val encoded = Base64.encodeToString(bytes, Base64.DEFAULT)
                    viewModel.sendQuoteSound(userId,encoded,quoteId)
                    Toast.makeText(context,"Ses başarıyla gönderildi",Toast.LENGTH_SHORT).show()
                    navController.navigate("quote_detail_page/$quoteId/$userId")
                }, modifier = Modifier.size(120.dp,50.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow) ,shape = RoundedCornerShape(20.dp)) {
                    Text(text = "Gönder", fontFamily = openSansFontFamily, color = Color.Black, fontSize = 20.sp)
                }
            }
        }
        //standart page
        else
        {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 75.dp)
            ) {
                Text(text =
                if(seconds in 0..9)
                {
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
                }, modifier = Modifier.size(80.dp,54.dp), fontFamily = openSansFontFamily, fontSize = 40.sp)
                Spacer(modifier = Modifier.padding(top = 5.dp))
                Divider(color = Color.Black, thickness = 2.dp, modifier = Modifier.size(300.dp,2.dp))
                Text(text =
                when (recCheck) {
                    0 -> {
                        "Kaydetmeye Başla"
                    }
                    1 -> {
                        "Kayıt Ediliyor.."
                    }
                    2 -> {
                        "Kayıt Tamamlandı."
                    }
                    3 -> {
                        ""
                    }
                    else -> {
                        ""
                    }
                }, fontFamily = openSansFontFamily, fontSize = 20.sp)
                Spacer(modifier = Modifier.padding(top = 50.dp))
                IconButton(onClick = {
                    when (recCheck) {
                        0 -> {
                            if(audioPermCheck)
                            {
                                if(player.duration != 0)
                                {
                                    stopPlayer(player)
                                }
                                try {
                                    startRecord()
                                    recCheck = 1
                                    timerCheck = true
                                }
                                catch (e: Exception)
                                {
                                    Log.d("exam","exception from standart start onclick: $e")
                                }
                            }
                        }
                        1 -> {
                            if(audioPermCheck)
                            {
                                try {
                                    stopRecord()
                                    recCheck = 2
                                    timerCheck = false
                                }
                                catch (e: Exception)
                                {
                                    Log.d("exam","exception from standart stop onclick: $e")
                                }
                            }
                        }
                        2 -> {
                            pageCheck = !pageCheck
                            seconds = 0
                            minutes = 0
                            startPlayer(mediaPlayer = player)
                            durationForSlider = player.duration.toLong()
                            object :
                                CountDownTimer(durationForSlider, 100) {
                                override fun onTick(millisUntilFinished: Long) {
                                    position =
                                        player.currentPosition.toFloat()
                                    if (player.currentPosition == durationForSlider.toInt()) {
                                        pageCheck = !pageCheck
                                        stopPlayer(player)
                                    }
                                }
                                override fun onFinish() {
                                }
                            }.start()
                            recCheck = 3
                        }
                        3  -> {
                            stopPlayer(player)
                            pageCheck = !pageCheck
                            recCheck = 0
                            seconds = 0
                            minutes = 0
                        }
                    }
                }) {
                    when (recCheck) {
                        0 -> {
                            Icon(painter = painterResource(id = R.drawable.record), contentDescription = "start record", Modifier.size(50.dp), tint = Color.Yellow)
                        }
                        1 -> {
                            Icon(painter = painterResource(id = R.drawable.stop2), contentDescription = "stop record", Modifier.size(50.dp), tint = Color.Yellow)
                        }
                        2 -> {
                            Icon(painter = painterResource(id = R.drawable.play), contentDescription = "play record", Modifier.size(50.dp), tint = Color.Yellow)
                        }
                        3 -> {
                            Icon(painter = painterResource(id = R.drawable.stop2), contentDescription = "stop record", Modifier.size(50.dp), tint = Color.Yellow)
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(top = 50.dp))
                Button(onClick = {
                    val file = File(outputFile)
                    val bytes: ByteArray = FileUtils.readFileToByteArray(file)
                    val encoded = Base64.encodeToString(bytes, Base64.DEFAULT)
                    viewModel.sendQuoteSound(userId,encoded,quoteId)
                    Toast.makeText(context,"Ses başarıyla gönderildi",Toast.LENGTH_SHORT).show()
                    navController.navigate("quote_detail_page/$quoteId/$userId")
                }, modifier = Modifier.size(120.dp,50.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow) ,shape = RoundedCornerShape(20.dp)) {
                    Text(text = "Gönder", fontFamily = openSansFontFamily, color = Color.Black, fontSize = 20.sp)
                }
            }
        }
    }
}
@Composable
fun QuoteRow(navController: NavController, userId: Int, quoteText: String, userPhoto: String?, userName: String, quoteId: Int) {
    Spacer(modifier = Modifier.padding(top = 10.dp))
    Box(modifier = Modifier
        .fillMaxWidth(), contentAlignment = Alignment.TopStart) {
        Surface(shape = RoundedCornerShape(15.dp), modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)) {
            Box(
                modifier = Modifier
                    .defaultMinSize(343.dp, 150.dp)
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
                        HashText(navController = navController, fullText = quoteText, quoteId = quoteId, userId = userId)
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
                contentScale = ContentScale.Crop,
                contentDescription = "user photo",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clip(CircleShape)
                    .size(44.dp, 44.dp)
            )
        }
    }
}



@SuppressLint("SuspiciousIndentation", "AutoboxingStateValueProperty")
@Composable
fun BottomNavigationForCreateQuoteSoundPage(navController: NavController, userId: Int) {
    val selectedIndex = remember { mutableStateOf(0) }
    val navigationItems = listOf(
        NavigationItem("Ana Sayfa", R.drawable.homeblack, "home_page"),
        NavigationItem("Bildirimler", R.drawable.notifications_black,"notifications_page/${userId}"),
        NavigationItem("Ekle", R.drawable.add,"create_quote_page/${userId}"),
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