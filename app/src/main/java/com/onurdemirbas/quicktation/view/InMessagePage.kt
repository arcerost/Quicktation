package com.onurdemirbas.quicktation.view

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.ViewTreeObserver
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import com.onurdemirbas.quicktation.util.Constants.MEDIA_URL
import com.onurdemirbas.quicktation.websocket.AppState
import com.onurdemirbas.quicktation.websocket.WebSocketListener

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalCoilApi::class, ExperimentalComposeUiApi::class)
@Composable
fun InMessagePage(navController: NavController, myId: Int, toUserId: Int, userName: String, userNick: String, viewModel: InMessageViewModel = hiltViewModel()) {
    val appState = remember { AppState(myId) }
    val webSocket = appState.webSocket
    val listener = appState.listener
    val userInfo by viewModel.userInfo.collectAsState()
    val userPhoto = userInfo.photo ?: ""
    LaunchedEffect(key1 = myId){
        viewModel.loadUser(myId, toUserId)
    }
    val message = remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val userPhotoUrl = remember(userPhoto) { MEDIA_URL + userPhoto}
    var keyboardOpen by rememberSaveable { mutableStateOf(false) }
    val messages = listener.messages.collectAsState().value
    val bottomBarHeight = 58.dp
//    DisposableEffect(Unit) {
//        onDispose {
//            webSocket.close(WebSocketListener.NORMAL_CLOSE_STATUS, "InMessagePage closed")    // mesaj sayfasında websocket'i kapatmak istiyorsan burayı aç
//        }
//    }
    val view = LocalView.current
    DisposableEffect(view) {
        val listenerr = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keyboardHeight = screenHeight - rect.bottom
            val isKeyboardVisible = keyboardHeight > screenHeight / 5
            keyboardOpen = isKeyboardVisible
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listenerr)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listenerr)
        }
    }
    Scaffold(
        topBar = {},
        content = {
            Surface(Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.mainbg), contentDescription = "background image", contentScale = ContentScale.FillHeight)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 15.dp, 0.dp, if (keyboardOpen) 2.dp else bottomBarHeight)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight(0.92f)) {
                        Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth())
                        {
                            if( userPhoto == "" || userPhoto == "null" ) {
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
                                val painter = rememberImagePainter(data = userPhotoUrl, builder = {})
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
                                Text(text = userName, fontFamily = openSansBold, fontSize = 18.sp)
                                Text(text = userNick, fontFamily = openSansFontFamily, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.padding(0.dp))
                            Spacer(modifier = Modifier.padding(0.dp))
                            Spacer(modifier = Modifier.padding(0.dp))
                            Spacer(modifier = Modifier.padding(0.dp))
                            IconButton(onClick = {
                                //options
                            }) {
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
                        MessagesRow(messages)
                    }
                }
                //message send bar
                Box(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center)
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        Arrangement.SpaceAround,
                        Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.padding(0.dp))
                        OutlinedTextField(
                            value = message.value,
                            onValueChange = {
                                message.value = it
                            },
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .size(width = 290.dp, height = 50.dp)
                                .onFocusChanged { focusState ->
                                    keyboardOpen = focusState.isFocused
                                    Log.d("focusstate", "state = $keyboardOpen")
                                    if (focusState.isFocused) {
                                        keyboardController?.show()
                                        Log.d("focusstate", "show")
                                    } else {
                                        keyboardController?.hide()
                                        Log.d("focusstate", "hide")
                                    }
                                },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    val messageText = message.value.text
                                    if(messageText == "" || messageText.isEmpty())
                                    {
                                        //boş text
                                    }
                                    else
                                    {
                                        listener.sendMessage(webSocket, messageText, toUserId)
                                        message.value = TextFieldValue("")
                                    }
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
                        IconButton(onClick = {
                            val messageText = message.value.text
                            if(messageText == "" || messageText.isEmpty())
                            {
                                return@IconButton
                            }
                            else
                            {
                                Log.d("deneme1","to usr $toUserId")
                                listener.sendMessage(webSocket, messageText, toUserId)
                                message.value = TextFieldValue("")
                            }
                        }, modifier = Modifier.size(50.dp)) {
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
            if (!keyboardOpen) {
                BottomNavigationForInMessagePage(navController = navController, userId = myId)
            }
        }
    )
}

@Composable
fun MessagesRow(messages: List<WebSocketListener.Message>) {
    MessageList(messages = messages)
}

@Composable
fun MessageList(
    messages: List<WebSocketListener.Message>,
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

@Composable
fun Balloon(
    message: String,
    isFromMe: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(3.dp)
) {
    val backgroundColor = if (isFromMe) {
        Color(6, 90, 101, 255)
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

@SuppressLint("SuspiciousIndentation", "AutoboxingStateValueProperty")
@Composable
fun BottomNavigationForInMessagePage(navController: NavController, userId: Int) {
    val selectedIndex = remember { mutableStateOf(0) }
    val navigationItems = listOf(
        NavigationItem("Ana Sayfa", R.drawable.homeblack, "home_page"),
        NavigationItem(
            "Bildirimler",
            R.drawable.notifications_black,
            "notifications_page/${userId}"
        ),
        NavigationItem("Ekle", R.drawable.add_black, "create_quote_page/${userId}"),
        NavigationItem("Mesajlar", R.drawable.chat, "messages_page/${userId}"),
        NavigationItem("Profil", R.drawable.profile_black, "my_profile_page/${userId}")
    )
    BottomNavigation(backgroundColor = Color.DarkGray, contentColor = LocalContentColor.current) {
        navigationItems.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = { CustomIcon(item.iconResId, contentDescription = item.title) },
                selected = selectedIndex.value == index,
                onClick = {
                    selectedIndex.value = index
                    navController.navigate(item.route) {
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