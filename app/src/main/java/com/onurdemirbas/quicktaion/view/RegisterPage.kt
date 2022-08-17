package com.onurdemirbas.quicktaion.view

import android.content.Context
import android.text.style.LineHeightSpan
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.onurdemirbas.quicktaion.R
import com.onurdemirbas.quicktaion.ui.theme.nunitoFontFamily
import com.onurdemirbas.quicktaion.ui.theme.openSansFontFamily

@Composable
fun RegisterPage(navController: NavController) {
    var name = remember { mutableStateOf(TextFieldValue())}
    var email = remember { mutableStateOf(TextFieldValue())}
    var password = remember { mutableStateOf(TextFieldValue())}
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
            Modifier.size(250.dp)
        )
        Column(verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            TextField(value = name.value, onValueChange ={
                name.value = it
            }, colors = TextFieldDefaults.textFieldColors(textColor = Color.White,backgroundColor = Color.Transparent, unfocusedIndicatorColor = Color.White), placeholder = {
                Text(text = "İsim Soyisim", color= Color.White, fontFamily = openSansFontFamily)
            })
            TextField(value = email.value, onValueChange ={
                email.value = it
            }, colors = TextFieldDefaults.textFieldColors(textColor = Color.White,backgroundColor = Color.Transparent, unfocusedIndicatorColor = Color.White), placeholder = {
                Text(text = "Email", color= Color.White, fontFamily = openSansFontFamily)
            })
            TextField(value = password.value, onValueChange ={
                password.value = it
            }, colors = TextFieldDefaults.textFieldColors(textColor = Color.White,backgroundColor = Color.Transparent, unfocusedIndicatorColor = Color.White), placeholder = {
                Text(text = "Şifre", color= Color.White, fontFamily = openSansFontFamily)
            })
            Spacer(modifier = Modifier.size(25.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                var s1check = remember { mutableStateOf(false)}
                Checkbox(checked = s1check.value , onCheckedChange = {s1check.value =it}, colors = CheckboxDefaults.colors(checkedColor = Color.White, uncheckedColor = Color.White, checkmarkColor = Color.Black))
                HyperLinkText(
                    fullText = "Sözleşmeyi okudum, anladım, kabul ediyorum",
                    linkText = listOf("Sözleşmeyi"),
                    hyperLinks = listOf("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"))

            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                var s2check = remember { mutableStateOf(false)}
                Checkbox(checked = s2check.value, onCheckedChange = {s2check.value = it}, colors = CheckboxDefaults.colors(checkedColor = Color.White, uncheckedColor = Color.White, checkmarkColor = Color.Black))
                HyperLinkText(
                    fullText = "Sözleşmeyi okudum, anladım, kabul ediyorum",
                    linkText = listOf("Sözleşmeyi"),
                    hyperLinks = listOf("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"))
            }
            Button(onClick = { },colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black), shape = RoundedCornerShape(15.dp), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp)) {
                Text(text = "Kayıt Ol", fontSize = 25.sp, fontFamily = nunitoFontFamily)
            }

        }
    }
}

@Composable
fun HyperLinkText(
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
    val uriHandler = LocalUriHandler.current
    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick ={
        annotatedString
            .getStringAnnotations("URL",it,it)
            .firstOrNull()?.let { stringAnnotation ->
            uriHandler.openUri(stringAnnotation.item)
        }
    } )

}