package com.onurdemirbas.quicktaion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onurdemirbas.quicktaion.view.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home_page")
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
                    composable("home_page")
                    {
                        HomePage(navController = navController)
                    }
                }
        }
    }
}

