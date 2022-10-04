package com.onurdemirbas.quicktation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.nunitoFontFamily
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.viewmodel.ForgotPasswordViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

@Composable
fun ForgotPasswordPage(navController: NavController, viewModel: ForgotPasswordViewModel = hiltViewModel()) {
    val email = remember { mutableStateOf(TextFieldValue()) }
    val code = remember { mutableStateOf(TextFieldValue()) }
    val newPw = remember { mutableStateOf(TextFieldValue()) }
    val openDialog = remember { mutableStateOf(false) }
    val openDialog2 = remember { mutableStateOf(false) }
    val maxChar = 6
    val context = LocalContext.current
    var errorMessage: MutableState<String>
    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
    Surface {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Color Gradient Background",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.FillBounds
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
                    },
                )
                AlreadyHaveAnAccText(
                    fullText = "Giriş yapmayı dene!",
                    hyperLinks = listOf(""),
                    linkText = listOf("Giriş yapmayı dene!"),
                    navController = navController
                )
                Spacer(modifier = Modifier.size(75.dp))
                Button(
                    onClick = {
                        if (email.value.text != "") {
                            viewModel.forgotPassword(email = email.value.text)
                            viewModel.viewModelScope.launch {
                                delay(1000)
                                errorMessage = viewModel.errorMessage
                                if (errorMessage.value.isEmpty()) {
                                    println("Başarılı Yollama")
                                    openDialog.value = true
                                } else {
                                    Toast.makeText(
                                        context,
                                        errorMessage.value,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else
                            Toast.makeText(context, "Email Boş Olamaz", Toast.LENGTH_LONG).show()
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
                    Text(text = "Şifremi Unuttum", color = Color.Black, fontSize = 25.sp, fontFamily = nunitoFontFamily)
                }
            }
        }
    }
    if (openDialog.value) {
        Popup(
            alignment = Alignment.BottomCenter,
            onDismissRequest = { openDialog.value = !openDialog.value },
            properties = PopupProperties(
                focusable = true,
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
            )
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .size(750.dp, 333.dp).windowInsetsPadding(WindowInsets.ime)
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = code.value,
                        onValueChange = {
                            if (it.text.length <= maxChar)
                                code.value = it
                        }, modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Black
                        ),
                        placeholder = {
                            Text(
                                text = "E-mailinize gelen kodu giriniz",
                                color = Color.Black,
                                fontFamily = openSansFontFamily
                            )
                        })
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = {
                            viewModel.checkCode(email.value.text, code.value.text)
                            viewModel.viewModelScope.launch {
                                delay(1000)
                                errorMessage = viewModel.errorMessage
                                if (errorMessage.value.isEmpty()) {
                                    println("Başarılı Doğrulama")
                                    openDialog.value = !openDialog.value
                                    openDialog2.value = !openDialog2.value
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
                            backgroundColor = Color.Yellow,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 15.dp)
                    ) {
                        Text(
                            text = "Kodu Gönder",
                            fontSize = 25.sp,
                            fontFamily = nunitoFontFamily
                        )
                    }
                }
            }
        }
    }
    if (openDialog2.value) {
            Popup(
                alignment = Alignment.BottomCenter,
                onDismissRequest = { openDialog2.value = !openDialog2.value },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true,
                    clippingEnabled = false,
                    excludeFromSystemGesture = true
                )
            )
            {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .background(color = Color.White)
                        .size(750.dp, 333.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = newPw.value,
                            onValueChange = {
                                newPw.value = it
                            }, modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.Black,
                                backgroundColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Black
                            ),
                            placeholder = {
                                Text(
                                    text = "Yeni Şifrenizi giriniz",
                                    color = Color.Black,
                                    fontFamily = openSansFontFamily
                                )
                            })
                        Spacer(modifier = Modifier.size(20.dp))
                        Button(
                            onClick = {
                                viewModel.updatePassword(
                                    email.value.text,
                                    md5(newPw.value.text),
                                    navController
                                )
                                viewModel.viewModelScope.launch {
                                    delay(1000)
                                    errorMessage = viewModel.errorMessage
                                    if (errorMessage.value.isEmpty()) {
                                        openDialog2.value = !openDialog2.value
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
                                backgroundColor = Color.Yellow,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 15.dp)
                        ) {
                            Text(
                                text = "Şifreyi Onayla",
                                fontSize = 25.sp,
                                fontFamily = nunitoFontFamily
                            )
                        }
                    }
                }
            }
        }
    }