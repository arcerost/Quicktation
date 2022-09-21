package com.onurdemirbas.quicktation.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.onurdemirbas.quicktation.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun PolicyPage(navController: NavController) {
    Surface {
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Box()
            {
                TopAppBar(modifier = Modifier.fillMaxWidth(), backgroundColor = Color.LightGray) {

                }
                Button(onClick = { navController.navigate("register_page") }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)) {
                    Image(painter = painterResource(id = R.drawable.back_icon), contentDescription = "back button", modifier = Modifier.size(35.dp))
                }
            }
            WebPageScreen(urlToRender = "https://www.google.com/")
        }
    }
}
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPageScreen(urlToRender: String) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            webViewClient = WebViewClient()
            loadUrl(urlToRender)
        }
    }, update = {
        it.loadUrl(urlToRender)
    })
}