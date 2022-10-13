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
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.nunitoFontFamily
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest


@Composable
fun RegisterPage(navController: NavController,viewModel: RegisterViewModel = hiltViewModel()) {
    val name = remember { mutableStateOf(TextFieldValue())}
    val email = remember { mutableStateOf(TextFieldValue())}
    val password = remember { mutableStateOf(TextFieldValue())}
    val password2 = remember { mutableStateOf(TextFieldValue())}
    val s1check = remember { mutableStateOf(false)}
    val s2check = remember { mutableStateOf(false)}
    val context = LocalContext.current
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    fun isEmailValid(email: String): Boolean {
            return emailRegex.toRegex().matches(email);
    }
    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

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
    Column(verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.quicktationlogo),
            contentDescription = "quicktationlogo",
            Modifier.size(200.dp)
        )
        Column(verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            TextField(value = name.value, onValueChange ={
                name.value = it
            },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()
                        focusManager.clearFocus()}), colors = TextFieldDefaults.textFieldColors(textColor = Color.White,backgroundColor = Color.Transparent,  unfocusedIndicatorColor = Color.White), placeholder = {
                Text(text = "İsim Soyisim", color= Color.White, fontFamily = openSansFontFamily)
            })
            TextField(value = email.value, onValueChange ={
                email.value = it
            },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()
                        focusManager.clearFocus()}), colors = TextFieldDefaults.textFieldColors(textColor = Color.White,backgroundColor = Color.Transparent, unfocusedIndicatorColor = Color.White), placeholder = {
                Text(text = "Email", color= Color.White, fontFamily = openSansFontFamily)
            })
            TextField(value = password.value, visualTransformation = PasswordVisualTransformation(), onValueChange ={
                password.value = it
            },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()
                        focusManager.clearFocus()}), colors = TextFieldDefaults.textFieldColors(textColor = Color.White,backgroundColor = Color.Transparent, unfocusedIndicatorColor = Color.White), placeholder = {
                Text(text = "Şifre", color= Color.White, fontFamily = openSansFontFamily)
            })
            TextField(value = password2.value, visualTransformation = PasswordVisualTransformation(), onValueChange ={
                password2.value = it
            },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()
                        focusManager.clearFocus()}), colors = TextFieldDefaults.textFieldColors(textColor = Color.White,backgroundColor = Color.Transparent, unfocusedIndicatorColor = Color.White), placeholder = {
                Text(text = "Şifre (Tekrar)", color= Color.White, fontFamily = openSansFontFamily)
            })
            Spacer(modifier = Modifier.size(25.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Checkbox(checked = s1check.value , onCheckedChange = {s1check.value =it}, colors = CheckboxDefaults.colors(checkedColor = Color.White, uncheckedColor = Color.White, checkmarkColor = Color.Black))
                HyperLinkText(
                    navController= navController,
                    fullText = "Sözleşmeyi okudum, anladım, kabul ediyorum",
                    linkText = listOf("Sözleşmeyi"), onClick = {
                        navController.navigate("policy_page")
                    })
                 //   hyperLinks = listOf(PolicyPage(navController = navController)))

            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

                Checkbox(checked = s2check.value, onCheckedChange = {s2check.value = it}, colors = CheckboxDefaults.colors(checkedColor = Color.White, uncheckedColor = Color.White, checkmarkColor = Color.Black))
                HyperLinkText(
                    navController= navController,
                    fullText = "Sözleşmeyi okudum, anladım, kabul ediyorum",
                    linkText = listOf("Sözleşmeyi"), onClick = {
                        navController.navigate("policy_page")
                    })
            }
            AlreadyHaveAnAccText(fullText = "Zaten hesabın var mı? Giriş yap.", hyperLinks = listOf(""), linkText = listOf("Zaten hesabın var mı? Giriş yap."), navController = navController)
            Spacer(modifier = Modifier.padding(15.dp))
            Button(onClick = {
                if (name.value.text != "") {
                    if (email.value.text != "") {
                        if (isEmailValid(email.value.text)) {
                            if (password.value.text != "") {
                                if (password2.value.text != "") {
                                    if (s1check.value && s2check.value) {
                                        if (password.value.text == password2.value.text) {
                                            viewModel.beRegister(
                                                email = email.value.text,
                                                password = md5(password.value.text),
                                                namesurname = name.value.text,
                                                navController = navController
                                            )
                                            viewModel.viewModelScope.launch {
                                                delay(600)
                                                val errorMessage = viewModel.errorMessage
                                                if (errorMessage.value.isEmpty()) {
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        errorMessage.value,
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Parolalar aynı olmalıdır!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Sözleşmeyi kabul etmeden devam edemezsiniz!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Parolayı Tekrar Giriniz!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(context, "Parolayı giriniz!", Toast.LENGTH_LONG)
                                    .show()
                            }
                        } else {
                            Toast.makeText(context, "Geçerli bir email giriniz!", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        Toast.makeText(context, "Email giriniz!", Toast.LENGTH_LONG).show()
                    }
                }
                 else {
                    Toast.makeText(context, "İsim giriniz!", Toast.LENGTH_LONG).show()
                }

            },colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black), shape = RoundedCornerShape(15.dp), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp)) {
                Text(text = "Kayıt Ol", color = Color.Black, fontSize = 25.sp, fontFamily = nunitoFontFamily)
            }
        }
    }
}
@Composable
fun HyperLinkText(
    navController: NavController,
    modifier: Modifier = Modifier,
    fullText: String,
    linkText: List<String>,
    linkTextColor: Color  = Color.Yellow,
    linkTextFontWeight: FontWeight = FontWeight.Medium,
    linkTextDecoration: TextDecoration = TextDecoration.Underline,
//    hyperLinks: List<String>,
    fontSize: TextUnit = TextUnit.Unspecified,
    onClick: () -> Unit,
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        addStyle(style = SpanStyle(fontSize = fontSize, color = Color.White, fontFamily = openSansFontFamily), start = 0, end = fullText.length)
        linkText.forEachIndexed { a, s ->
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
            /*
            addStringAnnotation(
                tag = "URL",
                annotation = hyperLinks[index],
                start = startIndex,
                end = endIndex
            )
             */
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
            navController.navigate("policy_page")
        annotatedString
            .getStringAnnotations("URL",it,it)
            .firstOrNull()?.let { a -> }
    } )

}

@Composable
fun AlreadyHaveAnAccText(
    navController: NavController,
    modifier: Modifier = Modifier,
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
            navController.navigate("login_page")
        } )
}