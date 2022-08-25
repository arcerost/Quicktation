package com.onurdemirbas.quicktaion

import android.os.Bundle
import android.view.textclassifier.TextLinks
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onurdemirbas.quicktaion.ui.theme.QuicktaionTheme
import com.onurdemirbas.quicktaion.view.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "open_page")
                {
                    composable("open_page")
                    {
                        OpenPage(navController)
                    }
                    composable("register_page")
                    {
                        //register_page codes
                        RegisterPage(navController = navController)
                    }
                    composable("login_page")
                    {
                        //login_page codes
                        LoginPage(navController = navController)
                    }
                    composable("forgot_password_page")
                    {
                        //forgot_password_page codes
                        ForgotPasswordPage(navController)
                    }
                    composable("policy_page")
                    {
                        PolicyPage(navController = navController)
                    }
                }
        }
    }
}

