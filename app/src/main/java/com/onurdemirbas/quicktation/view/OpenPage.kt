package com.onurdemirbas.quicktation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.nunitoFontFamily

@Composable
fun OpenPage(navController: NavController) {
    Surface {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
        }
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.quicktationlogo),
                contentDescription = "quicktationlogo",
                Modifier.size(250.dp)
            )
            Column {
                Button(
                    onClick = {
                        navController.navigate("login_page")
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xCC0FB050),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    Text("Giriş Yap", color = Color.Black, fontSize = 25.sp, fontFamily = nunitoFontFamily)
                }
                Button(
                    onClick = {
                        navController.navigate("register_page")
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 15.dp)
                ) {
                    Text("Kayıt Ol", color = Color.Black, fontSize = 25.sp, fontFamily = nunitoFontFamily)
                }
            }
        }
    }
}