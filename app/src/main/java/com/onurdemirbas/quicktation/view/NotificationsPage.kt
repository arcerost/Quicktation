package com.onurdemirbas.quicktation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun NotificationsPage(navController: NavController, myId: Int) {
    val interactionSource = MutableInteractionSource()
    val isPressed by interactionSource.collectIsPressedAsState()
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
                Image(painter = painterResource(id = R.drawable.notifications),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("notifications_page/$myId") }
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
                        ) { navController.navigate("messages_page/${myId}") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("my_profile_page/$myId") }
                        .size(28.dp, 31.dp))
            }
        }
    }
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
    Box(modifier = Modifier.then(clickable).wrapContentSize())
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