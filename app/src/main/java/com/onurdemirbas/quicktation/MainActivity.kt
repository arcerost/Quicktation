package com.onurdemirbas.quicktation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.onurdemirbas.quicktation.view.*
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
                    composable("notifications_page")
                    {
                        NotificationsPage(navController = navController)
                    }
                    composable("quote_detail_page", arguments = listOf(navArgument("id"){
                            type = NavType.IntType
                        },
                        navArgument("userId"){
                            type = NavType.IntType
                        }
                    ))
                    {
                        val id = remember { it.arguments?.getInt("id")}
                        val userId = remember { it.arguments?.getInt("userId")}
                        QuoteDetailPage(id = id!!,userId = userId!!,navController = navController)
                    }
                    composable("my_profile_page")
                    {
                        MyProfilePage(navController = navController)
                    }
                    composable("other_profile_page")
                    {
                        OtherProfilePage(navController = navController)
                    }
                }
        }
    }
}

