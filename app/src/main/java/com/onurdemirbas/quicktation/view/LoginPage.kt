@file:OptIn(ExperimentalComposeUiApi::class)

package com.onurdemirbas.quicktation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.Room
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.database.UserDatabase
import com.onurdemirbas.quicktation.database.UserInfo
import com.onurdemirbas.quicktation.ui.theme.nunitoFontFamily
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest


fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}


@Composable
fun LoginPage(navController: NavController,viewModel: LoginViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val email = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val db: UserDatabase = Room.databaseBuilder(context,UserDatabase::class.java,"UserInfo").build()
    val userDao = db.UserDao()

    Surface {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
        }
    }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.quicktationlogo),
            contentDescription = "quicktationlogo",
            Modifier
                .size(250.dp)
                .aspectRatio(1.0f)
        )
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
                modifier = Modifier.width((0.8f * LocalConfiguration.current.screenWidthDp).dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()
                        focusManager.clearFocus()}),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.White
                ),
                placeholder = {
                    Text(text = "Email", color = Color.White, fontFamily = openSansFontFamily)
                })
            TextField(
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    password.value = it
                },
                modifier = Modifier.width((0.8f * LocalConfiguration.current.screenWidthDp).dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()
                        focusManager.clearFocus()}),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.White
                ),
                placeholder = {
                    Text(text = "Şifre", color = Color.White, fontFamily = openSansFontFamily)
                })
            Spacer(modifier = Modifier.size(50.dp))
            Row(
                modifier = Modifier.width((0.8f * LocalConfiguration.current.screenWidthDp).dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ForgotPasswordText(
                    fullText = "Şifreni mi Unuttun?",
                    linkText = listOf("Şifreni mi Unuttun?"),
                    hyperLinks = listOf(""),
                    navController = navController
                )
            }
            Row(
                modifier = Modifier.width((0.8f * LocalConfiguration.current.screenWidthDp).dp),
                horizontalArrangement = Arrangement.Center
            ) {
                RegisterText(
                    fullText = "Hesabın mı yok? Kayıt olmak için tıkla!",
                    linkText = listOf("tıkla!"),
                    hyperLinks = listOf(""),
                    navController = navController
                )
            }
            Spacer(modifier = Modifier.size(75.dp))
            Button(
                onClick = {
                    viewModel.beLogin(email = email.value.text, password = md5(password.value.text), navController = navController)
                    viewModel.viewModelScope.launch {
                        delay(600)
                        val errorMessage = viewModel.errorMessage
                        val id = viewModel.id.value
                        if (errorMessage.value.isEmpty()) {
                            val user = UserInfo(id,email.value.text,password.value.text)
                            userDao.insert(user)
                            navController.navigate("home_page")
                        } else {
                            Toast.makeText(
                                context,
                                errorMessage.value,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0FB050),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 15.dp)
            ) {
                Text(text = "Giriş yap", color = Color.Black, fontSize = 25.sp, fontFamily = nunitoFontFamily)
            }
        }
    }
}


@Composable
fun RegisterText(
    modifier: Modifier = Modifier,
    navController: NavController,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color  = Color.Yellow,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    hyperLinks: List<String>,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        addStyle(style = SpanStyle(fontSize = fontSize, color = Color.White, fontFamily = openSansFontFamily), start = 0, end = fullText.length)
        linkText.forEachIndexed { index, s ->
            val  startIndex  = fullText.indexOf(s)
            val endIndex = startIndex + s.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize  =  fontSize,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = "URL",
                annotation = hyperLinks[index],
                start = startIndex,
                end = endIndex
            )
            addStyle(
                style = SpanStyle(
                    color = Color.White),
                start = endIndex,
                end = fullText.length
            )
        }
    }
    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick ={
            navController.navigate("register_page")
        } )
}


@Composable
fun ForgotPasswordText(
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color  = Color.Yellow,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
    hyperLinks: List<String>,
    navController: NavController,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        addStyle(style = SpanStyle(fontSize = fontSize, color = Color.White, fontFamily = openSansFontFamily), start = 0, end = fullText.length)
        linkText.forEachIndexed { index, s ->
            val  startIndex  = fullText.indexOf(s)
            val endIndex = startIndex + s.length
            addStyle(
                style = SpanStyle(
                    color = linkTextColor,
                    fontSize  =  fontSize,
                    fontWeight = linkTextFontWeight,
                    textDecoration = linkTextDecoration
                ),
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(
                tag = "URL",
                annotation = hyperLinks[index],
                start = startIndex,
                end = endIndex
            )
            addStyle(
                style = SpanStyle(
                    color = Color.White),
                start = endIndex,
                end = fullText.length
            )
        }
    }
    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick ={
            navController.navigate("forgot_password_page")
        } )
}