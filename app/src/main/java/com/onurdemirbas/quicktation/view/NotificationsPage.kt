package com.onurdemirbas.quicktation.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.model.HomeResponse
import com.onurdemirbas.quicktation.ui.theme.nunitoFontFamily
import com.onurdemirbas.quicktation.viewmodel.HomeViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotificationsPage(navController: NavController, myId: Int) {
    val interactionSource = MutableInteractionSource()
    val isPressed by interactionSource.collectIsPressedAsState()
    Scaffold(topBar = {}, content = {
        Surface(Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.mainbg), contentDescription = "background image", contentScale = ContentScale.FillHeight)
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
            Spacer(modifier = Modifier.padding(top = 25.dp))
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //Lazy column row
                NotificationRow()
                NotificationRow()
                NotificationRow()
                NotificationRow()
                NotificationRow()
                NotificationRow()

            }
        }
    }, bottomBar = {
        BottomNavigationForNotificationsPage(navController = navController, myId = myId)
    })
}

@Composable
fun NotificationList(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
   // val postList by remember { viewModel.mainList }
   // NotificationListView(posts = postList, navController = navController)
}

@Composable
fun NotificationListView(posts: List<HomeResponse>, navController: NavController) {
  //  LazyColumn(contentPadding = PaddingValues(5.dp)) {
     //   items(posts) { post ->
            // MainRow(post =  post)
       // }
   // }
}

@Composable
fun NotificationRow() {
    val colorControl = MutableInteractionSource()
    val isPressed by colorControl.collectIsPressedAsState()
    val color = if (isPressed) Color(0xFFC1C1C1) else Color(0xFF3BE360)
    val clickable = Modifier.clickable(interactionSource = colorControl, indication = null){}
    Box(modifier = Modifier
        .then(clickable)
        .wrapContentSize())
    {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 40.dp, end = 40.dp, top = 30.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            )
            {
                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxWidth())
                {
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            onClick = {

                            }, interactionSource = colorControl, modifier = Modifier
                                .size(20.dp, 20.dp)
                                .padding(top = 3.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.notification_green),
                                contentDescription = "like",
                                tint = color,
                                modifier = Modifier
                                    .size(20.dp, 20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.padding(start = 15.dp))
                        Text(
                            modifier = Modifier.weight(6f),
                            text = "Lorem ipsum dolor sit amet.",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontFamily = nunitoFontFamily,
                            fontWeight = FontWeight.W100
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .weight(1f),
                            text = "1 g önce",
                            color = Color(0xFF525759),
                            fontFamily = nunitoFontFamily,
                            fontSize = 10.sp, maxLines = 1
                        )
                    }
                    Divider(color = Color.Black, thickness = 2.dp)
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun BottomNavigationForNotificationsPage(navController: NavController, myId: Int) {
    val selectedIndex = remember { mutableStateOf(0) }
    val navigationItems = listOf(
        NavigationItem("Ana Sayfa", R.drawable.homeblack, "home_page"),
        NavigationItem("Bildirimler", R.drawable.notifications,"notifications_page/${myId}"),
        NavigationItem("Ekle", R.drawable.add_black,"create_quote_page/${myId}"),
        NavigationItem("Mesajlar", R.drawable.chat_black,"messages_page/${myId}"),
        NavigationItem("Profil", R.drawable.profile_black,"my_profile_page/${myId}"))
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