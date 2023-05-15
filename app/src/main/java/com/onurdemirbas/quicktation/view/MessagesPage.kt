@file:OptIn(ExperimentalCoilApi::class)

package com.onurdemirbas.quicktation.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.model.Message
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.MessagesViewModel
import okhttp3.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MessagesPage(navController: NavController, myId: Int, viewModel: MessagesViewModel = hiltViewModel()) {
    LaunchedEffect(myId) {
        viewModel.loadMessageList(myId)
    }
    Scaffold(topBar = {}, content = {
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
                Spacer(modifier = Modifier.padding(top = 20.dp))
                MessageList(myId = myId, viewModel = viewModel, navController = navController)
            }
        }
    }, bottomBar = {
        BottomNavigationMessagesPage(navController = navController, userId = myId)
    })
}

@Composable
fun MessageList(myId: Int, viewModel: MessagesViewModel, navController: NavController) {
    val messageList = viewModel.messageList.collectAsState()
    val errorMessage = viewModel.errorMessage
    val context = LocalContext.current
    if (errorMessage.value.isNotEmpty()) {
        Toast.makeText(context, errorMessage.value, Toast.LENGTH_LONG).show()
    } else {
        MessageListView(messages = messageList.value, navController = navController, myId = myId)
    }
}
@Composable
fun MessageListView(messages: List<Message>, navController: NavController, myId: Int) {
    val state = rememberLazyListState()
    LazyColumn(contentPadding = PaddingValues(top = 5.dp, bottom = 50.dp), verticalArrangement = Arrangement.SpaceEvenly, state = state) {
        items(messages, key = { it.id }) { message ->
            MessageRow(myId = myId, message = message, navController = navController)
        }
    }
}


@Composable
fun MessageRow(myId: Int, message: Message, navController: NavController) {
    val toUserId = if(message.receiver == myId) message.sender else message.receiver
    val lastMessage = message.lastMessageValue
    val lastMessageDate = message.lastMessageDate
    val senderName = message.senderName
    val senderPhoto = message.senderPhoto
    val senderNick = message.senderNick
    Box(
        modifier = Modifier
            .defaultMinSize(315.dp, 50.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("in_message_page/$myId/$toUserId/$senderName/$senderNick")
            }, contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)) {
                if(senderPhoto == null || senderPhoto == "" || senderPhoto == "null") {
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
                    val painter = rememberImagePainter(data = Constants.MEDIA_URL + senderPhoto, builder = {})
                    Image(
                        painter = painter,
                        contentDescription = "user photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(44.dp, 44.dp)
                            .clip(CircleShape),
                        alignment = Alignment.CenterStart
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = if (senderName == "") { "" } else "$senderName", fontFamily = openSansBold, fontSize = 13.sp)
                    Text(
                        text = lastMessage,
                        fontFamily = openSansFontFamily, fontSize = 13.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = calculateTimePassed(lastMessageDate), fontSize = 13.sp, color = Color(82, 87, 89, 255)
                    )
                }
                Spacer(modifier = Modifier.padding(end = 5.dp))
            }
        }
    }
}

fun calculateTimePassed(lastMessageDate: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val past = sdf.parse(lastMessageDate)
    val now = Date()

    val milliseconds = now.time - past!!.time
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "$days g önce"
        hours > 0 -> "$hours s önce"
        minutes > 0 -> "$minutes dk önce"
        else -> "az önce"
    }
}





@SuppressLint("SuspiciousIndentation", "AutoboxingStateValueProperty")
@Composable
fun BottomNavigationMessagesPage(navController: NavController, userId: Int) {
    val selectedIndex = remember { mutableStateOf(0) }
    val navigationItems = listOf(
        NavigationItem("Ana Sayfa", R.drawable.homeblack, "home_page"),
        NavigationItem("Bildirimler", R.drawable.notifications_black,"notifications_page/${userId}"),
        NavigationItem("Ekle", R.drawable.add_black,"create_quote_page/${userId}"),
        NavigationItem("Mesajlar", R.drawable.chat,"messages_page/${userId}"),
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