package com.onurdemirbas.quicktation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.MyProfileViewModel

@Composable
fun FollowerPage(navController: NavController) {
    val interactionSource =  MutableInteractionSource()
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFDDDDDD)) {
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    )
    {
        // ProfileRow(navController = navController, myId = 1)
    }

    //BottomBar
    Box(modifier = Modifier
        .fillMaxSize()
        .fillMaxWidth(), contentAlignment = Alignment.BottomStart) {
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
                Image(painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("my_profile_page") }
                        .size(28.dp, 31.dp))
            }
        }
    }
}



@OptIn(ExperimentalCoilApi::class)
@Composable
fun FollowerProfileRow(navController: NavController, viewModel: MyProfileViewModel = hiltViewModel(), myId: Int) {
    val user = viewModel.userInfo.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.padding(top = 25.dp))
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(start = 25.dp))
                Image(painter = painterResource(id = R.drawable.options),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable() {
                            //options
                        }
                        .size(52.dp, 12.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(start = 90.dp))
                Text(
                    text = user.value.namesurname,
                    modifier = Modifier.defaultMinSize(165.dp, 30.dp),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.padding(start = 35.dp))
                if (user.value.photo == null || user.value.photo == "") {
                    Image(
                        painter = painterResource(id = R.drawable.pp),
                        contentDescription = null,
                        modifier = Modifier
                            .size(75.dp, 75.dp)
                    )
                } else {
                    val painter = rememberImagePainter(
                        data = Constants.BASE_URL + user.value.photo,
                        builder = {})
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(75.dp, 75.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(end = 15.dp))
            }
            Spacer(Modifier.padding(top = 15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Takipçiler", fontSize = 16.sp)
                Text("Takip Edilen", fontSize = 16.sp)
                Text(text = "Beğeniler", fontSize = 16.sp)
            }
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "${user.value.followerCount}", fontSize = 16.sp)
                Text(text = "${user.value.followCount}", fontSize = 16.sp)
                Text(text = "${user.value.likeCount}", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.padding(top = 25.dp))
            ProfilePostList(navController = navController)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.padding(top = 25.dp))
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(start = 25.dp))
                Image(painter = painterResource(id = R.drawable.options),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable() {
                            //options
                        }
                        .size(52.dp, 12.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(start = 90.dp))
                Text(
                    text = user.value.namesurname,
                    modifier = Modifier.defaultMinSize(165.dp, 30.dp),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.padding(start = 35.dp))
                if (user.value.photo == null || user.value.photo == "") {
                    Image(
                        painter = painterResource(id = R.drawable.pp),
                        contentDescription = null,
                        modifier = Modifier
                            .size(75.dp, 75.dp)
                    )
                } else {
                    val painter = rememberImagePainter(
                        data = Constants.BASE_URL + user.value.photo,
                        builder = {})
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(75.dp, 75.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(end = 15.dp))
            }
            Spacer(Modifier.padding(top = 15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Takipçiler", fontSize = 16.sp)
                Text("Takip Edilen", fontSize = 16.sp)
                Text(text = "Beğeniler", fontSize = 16.sp)
            }
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "${user.value.followerCount}", fontSize = 16.sp)
                Text(text = "${user.value.followCount}", fontSize = 16.sp)
                Text(text = "${user.value.likeCount}", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.padding(top = 25.dp))
            FollowerList(navController = navController)
        }
    }
}

@Composable
fun FollowerList(navController: NavController) {

}