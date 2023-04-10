package com.onurdemirbas.quicktation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.viewmodel.InMessageViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import com.onurdemirbas.quicktation.util.Constants.MEDIA_URL


data class Message(val text: String, val isSent: Boolean)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalCoilApi::class, ExperimentalComposeUiApi::class)
@Composable
fun InMessagePage(navController: NavController, myId: Int, viewModel: InMessageViewModel = hiltViewModel()) {
    val message = remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val userInfo = viewModel.userInfo.collectAsState()
    val userName = userInfo.value.namesurname
    val userPhoto = viewModel.userInfo.collectAsState().value.photo
    val userPhotoUrl = remember(userPhoto) { MEDIA_URL + userPhoto}
    Scaffold(
        topBar = {

        },
        content = {
            Surface(Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.mainbg), contentDescription = "background image", contentScale = ContentScale.FillHeight)
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 15.dp, 0.dp, 50.dp)
            ) {
                Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight(0.92f)) {
                    Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth())
                    {
                        if( userPhotoUrl == "" || userPhotoUrl == "null" || userPhotoUrl == MEDIA_URL) {
                            Image(
                                painter = painterResource(id = R.drawable.pp),
                                contentDescription = "user photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .size(44.dp, 44.dp)
                                    .clip(CircleShape)
                            )
                        }
                        else {
                            val painter = rememberImagePainter(data = MEDIA_URL + userPhotoUrl, builder = {})
                            Image(
                                painter = painter,
                                contentDescription = "user photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .size(44.dp, 44.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Column(Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                            Text(text = "Faruk Arkadan Mutlu $userName", fontFamily = openSansBold, fontSize = 18.sp)
                            Text(text = "musmutluyumuşyumak", fontFamily = openSansFontFamily, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.padding(0.dp))
                        Spacer(modifier = Modifier.padding(0.dp))
                        Spacer(modifier = Modifier.padding(0.dp))
                        Spacer(modifier = Modifier.padding(0.dp))
                        IconButton(onClick = {  }) {
                            Icon(
                                painter = painterResource(id = R.drawable.options),
                                contentDescription = "options",
                                modifier = Modifier
                                    .size(33.dp, 9.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Divider(Modifier.padding(start = 20.dp, end = 20.dp), color = Color.Black, thickness = 1.dp)
                    MessagesRow()
                }
                //message bottom bar
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(bottom = 6.dp),
                    contentAlignment = Alignment.Center)
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        Arrangement.SpaceAround,
                        Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.padding(0.dp))
                        OutlinedTextField(
                            value = message.value,
                            onValueChange = {
                                message.value = it
                            },
                            modifier = Modifier.size(width = 290.dp, height = 50.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }),
                            textStyle = TextStyle(
                                fontFamily = openSansFontFamily,
                                fontSize = 12.sp,
                                color = Color.White
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = Color(125, 120, 120),
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                disabledBorderColor = Color.Black
                            ),
                            shape = RoundedCornerShape(15.dp),
                            placeholder = {
                                Text(
                                    text = "mesaj...",
                                    fontSize = 11.sp,
                                    fontFamily = openSansBold,
                                    color = Color.White
                                )
                            })
                        IconButton(onClick = {  }, modifier = Modifier.size(50.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.send),
                                contentDescription = "options",
                                modifier = Modifier
                                    .size(30.dp, 30.dp),
                                tint = Color.Yellow
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Box{
                Image(
                    painter = painterResource(id = R.drawable.backgroundbottombar),
                    contentDescription = "BottomAppBar Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .zIndex(0f)
                )
            }
            BottomAppBar(
                Modifier.fillMaxWidth(),
                backgroundColor = Color.Transparent,
                contentColor = Color.White,
                elevation = 50.dp,
                cutoutShape = CircleShape
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { navController.navigate("home_page") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.homeblack),
                                contentDescription = "Home",
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        IconButton(
                            onClick = { navController.navigate("notifications_page/${myId}") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.notifications_black),
                                contentDescription = "Notifications",
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        IconButton(
                            onClick = { navController.navigate("create_quote_page/$myId") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_black),
                                contentDescription = "Create Quote",
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        IconButton(
                            onClick = { navController.navigate("messages_page/${myId}") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chat),
                                contentDescription = "Messages",
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        IconButton(
                            onClick = { navController.navigate("my_profile_page/${myId}") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.profile_black),
                                contentDescription = "Profile",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }


            }
        }
    )
}

@Composable
fun MessagesRow() {
    val messages = listOf(
        Message(text = "Merhaba, nasılsın?", isSent = true),
        Message(text = "İyiyim, teşekkürler. Sen nasılsın?", isSent = false),
        Message(text = "Ben de iyiyim, teşekkürler.", isSent = true),
        Message(text = "Ne yapıyorsun bu aralar?", isSent = false),
        Message(text = "Çalışıyorum, sen?", isSent = true),
        Message(text = "Ben hayatımda hiç..", isSent = true),
        Message(text = "Yemedin değil mi..", isSent = false),
        Message(text = "Ben de yemedim!", isSent = false),
        Message(text = "Merhaba, nasılsın?", isSent = true),
        Message(text = "İyiyim, teşekkürler. Sen nasılsın?", isSent = false),
        Message(text = "Ben de iyiyim, teşekkürler.", isSent = true),
        Message(text = "Ne yapıyorsun bu aralar?", isSent = false),
        Message(text = "Çalışıyorum, sen?", isSent = true),
        Message(text = "Ben hayatımda hiç..", isSent = true),
        Message(text = "Yemedin değil mi..", isSent = false),
        Message(text = "Ben de yemedim!", isSent = false)

    )
    MessageList(messages = messages)
}

@Composable
fun Balloon(
    message: String,
    isFromMe: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(3.dp)
) {
    val backgroundColor = if (isFromMe) {
        Color(189, 143, 163, 255)
    } else {
        Color.White
    }
    Box(
        modifier = modifier
            .padding(contentPadding)
            .defaultMinSize(238.dp, 43.dp)
            .background(
                backgroundColor,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = message,
            textAlign = if (isFromMe) TextAlign.End else TextAlign.Start,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun MessageList(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        reverseLayout = false,
    ) {
        items(messages.size) { index ->
            val message = messages[index]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .align(if (message.isSent) Alignment.CenterVertically else Alignment.CenterVertically)
                        .let { if (message.isSent) it.align(Alignment.Top) else it.align(Alignment.Bottom) }
                ) {
                    Balloon(message.text, message.isSent)
                }
            }

        }
    }
}