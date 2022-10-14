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
import androidx.room.Room
import com.onurdemirbas.quicktation.database.UserDatabase
import com.onurdemirbas.quicktation.view.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db: UserDatabase = Room.databaseBuilder(this, UserDatabase::class.java,"UserInfo")
                .fallbackToDestructiveMigration()
                .build()
            val userDao = db.UserDao()
            var userId: Int?
            var userEmail: String?
            var userPw: String?
            runBlocking {
                if(userDao.anyData() ==0)
                {
                    userEmail = ""
                    userId = null
                    userPw = ""
                }
                else
                {
                    userId = userDao.getUser().userId
                    userEmail = userDao.getUser().userPassword
                    userPw = userDao.getUser().userPassword
                }
            }
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination =
            if(userEmail != "" && userPw !="" && userId != null){
                "home_page"
            }
                else
            {
                    "open_page"
            })
            {
                composable("open_page")
                {
                    OpenPage(navController)
                }
                composable("messages_page/{myId}", arguments = listOf(
                    navArgument("myId"){
                        type = NavType.IntType
                    }
                ))
                {
                    val myId = remember { it.arguments?.getInt("myId")}
                    MessagesPage(navController = navController, myId!!)
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
                composable("notifications_page/{myId}", arguments = listOf(
                    navArgument("myId"){
                        type = NavType.IntType
                    }
                ))
                {
                    val myId = remember { it.arguments?.getInt("myId")}
                    NotificationsPage(navController = navController, myId!!)
                }
                composable("quote_detail_page/{id}/{userId}", arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                    },
                    navArgument("userId") {
                        type = NavType.IntType
                    }
                ))
                {
                    val id = remember { it.arguments?.getInt("id") }
                    val userId1 = remember { it.arguments?.getInt("userId") }
                    QuoteDetailPage(id = id!!, userId = userId1!!, navController = navController)
                }
                composable("my_profile_page/{myId}", arguments = listOf(
                    navArgument("myId"){
                        type = NavType.IntType
                    }
                ))
                {
                    val myId = remember { it.arguments?.getInt("myId")}
                    MyProfilePage(navController = navController, myId!!)
                }
                composable("follower_page/{userId}/{toUserId}/{action}/{namesurname}/{likeCount}/{followCount}/{followerCount}/{amIFollow}", arguments = listOf(
                    navArgument("userId") {
                        type = NavType.IntType
                    },
                    navArgument("toUserId") {
                        type = NavType.IntType
                    },
                    navArgument("action") {
                        type = NavType.StringType
                    },
                    navArgument("namesurname"){
                        type = NavType.StringType
                    },
                    navArgument("likeCount") {
                        type = NavType.IntType
                    },
                    navArgument("followCount"){
                        type = NavType.IntType
                    },
                    navArgument("followerCount"){
                        type = NavType.IntType
                    },
                    navArgument("amIFollow"){
                        type = NavType.IntType
                    }
                ))
                {
                    val userId2 = remember { it.arguments?.getInt("userId") }
                    val toUserId = remember { it.arguments?.getInt("toUserId") }
                    val action = remember { it.arguments?.getString("action") }
                    val namesurname = remember { it.arguments?.getString("namesurname") }
                    val likeCount = remember{ it.arguments?.getInt("likeCount") }
                    val followCount = remember { it.arguments?.getInt("followCount") }
                    val followerCount = remember { it.arguments?.getInt("followerCount") }
                    val amIFollow = remember { it.arguments?.getInt("amIFollow")}
                    FollowerPage(navController = navController, userId = userId2!!, toUserId =  toUserId!!, action =  action!!,namesurname!!,likeCount!!,followCount!!,followerCount!!, amIFollow!!)
                }
                composable("other_profile_page/{userId}/{myId}", arguments = listOf(
                    navArgument("userId") {
                        type = NavType.IntType
                    },
                    navArgument("myId"){
                        type = NavType.IntType
                    }
                ))
                {
                    val userId3 = remember { it.arguments?.getInt("userId") }
                    val myId = remember { it.arguments?.getInt("myId")}
                    OtherProfilePage(navController = navController, userId = userId3!!, myId = myId!!)
                }
                composable("edit_profile_page/{userId}", arguments = listOf(
                    navArgument("userId"){
                        type = NavType.IntType
                    }
                ))
                {
                    val userId4 = remember { it.arguments?.getInt("userId")}
                    EditProfilePage(navController = navController,userId4!!)
                }
            }
        }
    }
}

