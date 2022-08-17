package com.onurdemirbas.quicktaion.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.onurdemirbas.quicktaion.R
import com.onurdemirbas.quicktaion.ui.theme.nunitoFontFamily
import com.onurdemirbas.quicktaion.ui.theme.openSansFontFamily

@Composable
fun ForgotPasswordPage(navController: NavController) {
    var email = remember { mutableStateOf(TextFieldValue()) }
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
                Modifier.size(250.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                TextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        backgroundColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.White
                    ),
                    placeholder = {
                        Text(text = "Email", color = Color.White, fontFamily = openSansFontFamily)
                    })
                Spacer(modifier = Modifier.size(75.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 15.dp)
                ) {
                    Text(text = "Şifremi Unuttum", fontSize = 25.sp, fontFamily = nunitoFontFamily)
                }
            }
        }
    }
}