@file:OptIn(ExperimentalCoilApi::class, ExperimentalComposeUiApi::class)
@file:Suppress("DEPRECATION")

package com.onurdemirbas.quicktation.view

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.EditProfileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
@Composable
fun EditProfilePage(navController: NavController, myId: Int, viewModel: EditProfileViewModel = hiltViewModel()) {
    viewModel.viewModelScope.launch {
        viewModel.loadUser(myId)
    }
    Surface(Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.mainbg), contentDescription = "background image", contentScale = ContentScale.FillHeight)
    }
    val context = LocalContext.current
    val userInfo = viewModel.userInfo.collectAsState()
    val nameSurnameAlr = userInfo.value.namesurname
    val userEmailAlr = userInfo.value.email
    val userPhotoAlr = userInfo.value.photo
    val usernameAlr = userInfo.value.username
    val userName = remember { mutableStateOf(TextFieldValue()) }
    val nameSurname = remember { mutableStateOf(TextFieldValue()) }
    val email = remember { mutableStateOf(TextFieldValue()) }
    var userPhotoForService by remember { mutableStateOf<Bitmap?>(null) }
    val interactionSource = MutableInteractionSource()
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { MutableStateFlow<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }
    var photoChanged by remember { mutableStateOf(false) }
    var uriChangeCheck by remember { mutableStateOf(false) }
    val byteArrayOutputStream = ByteArrayOutputStream()
    var byteArray by remember { mutableStateOf<ByteArray?>(null) }
    var encoded by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    if(uriChangeCheck)
    {
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            }
            else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
            bitmap.value?.let { btm ->
                userPhotoForService = btm
                uriChangeCheck = !uriChangeCheck
                photoChanged = !photoChanged
                bitmap.value!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                byteArray = byteArrayOutputStream.toByteArray()
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        )
        {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            )
            {
                Spacer(modifier = Modifier.padding(top = 25.dp))
                Text(text = "PROFİLİ DÜZENLE", fontSize = 18.sp, fontFamily = openSansBold)
                Spacer(modifier = Modifier.padding(top = 15.dp))
                Divider(
                    thickness = 1.dp,
                    color = Color.Black,
                    modifier = Modifier.size(300.dp, 1.dp)
                )
                Spacer(modifier = Modifier.padding(top = 30.dp))
                Box(contentAlignment = Alignment.Center)
                {
                    if(photoChanged)
                    {
                        Image(bitmap = bitmap.value!!.asImageBitmap(),"profile photo",contentScale = ContentScale.Crop, modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape))
                        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.size(150.dp))
                        {
                            IconButton(onClick = {
                                launcher.launch("image/*")
                            }) {
                                Icon(painter = painterResource(id = R.drawable.addphoto), contentDescription = "add photo", Modifier.size(30.dp, 30.dp))
                            }
                        }
                    }
                    else if (userPhotoAlr == "" || userPhotoAlr == null || userPhotoAlr == "null") {
                        if(userPhotoForService == null)
                        {
                            Image(painter = painterResource(id = R.drawable.pp), contentDescription = "profile photo", contentScale = ContentScale.Crop, modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape))
                        }
                        else
                        {
                            Image(bitmap = userPhotoForService!!.asImageBitmap(), contentDescription = "profile photo", contentScale = ContentScale.Crop, modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape))
                        }
                        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.size(150.dp))
                        {
                            IconButton(onClick = {
                                launcher.launch("image/*")
                            }) {
                                Icon(painter = painterResource(id = R.drawable.addphoto), contentDescription = "add photo", Modifier.size(30.dp, 30.dp))
                            }
                        }
                    }
                    else {
                        val painter = rememberImagePainter(data = Constants.MEDIA_URL + userPhotoAlr, builder = {})
                        Image(painter = painter, contentDescription = "profile photo", contentScale = ContentScale.Crop, modifier = Modifier.size(100.dp).clip(CircleShape))
                        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.size(140.dp))
                        {
                            IconButton(onClick = {
                                launcher.launch("image/*")
                                uriChangeCheck = !uriChangeCheck
                            }) {
                                Icon(painter = painterResource(id = R.drawable.addphoto), contentDescription = "add photo", Modifier.size(30.dp, 30.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(top = 50.dp))
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    enabled = false,
                    textStyle = TextStyle(fontFamily = openSansFontFamily),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color(217, 217, 217, 255)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = {
                        Text(
                            text = userEmailAlr
                        )
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = "email icon",
                            Modifier.size(25.dp)
                        )
                    },
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(20.dp))
                OutlinedTextField(
                    value = nameSurname.value,
                    onValueChange = {
                        nameSurname.value = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {keyboardController?.hide()
                            focusManager.clearFocus()}),
                    textStyle = TextStyle(fontFamily = openSansFontFamily),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color(217, 217, 217, 255)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = {
                        Text(
                            text = nameSurnameAlr
                        )
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.useredit),
                            contentDescription = "username icon",
                            Modifier.size(25.dp)
                        )
                    },
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(top = 30.dp))
                OutlinedTextField(
                    value = userName.value,
                    onValueChange = {
                        userName.value = it
                    },
                    enabled = true,
                    textStyle = TextStyle(fontFamily = openSansFontFamily),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color(217, 217, 217, 255)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = {
                        Text(
                            text = usernameAlr
                        )
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "username icon",
                            Modifier.size(25.dp)
                        )
                    },
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(30.dp))
                Button(
                    onClick = {
                        viewModel.loadEdit(myId, nameSurname.value.text, encoded?:"",userName.value.text)
                        Toast.makeText(context, "Değişiklikler Kaydedildi.", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
                    border = BorderStroke(1.dp, color = Color.Black),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.size(130.dp, 50.dp)
                ) {
                    Text(text = "KAYDET", color = Color.Black, fontFamily = openSansBold)
                }
            }
        }
    }
    //BottomBar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(), contentAlignment = Alignment.BottomStart
    )
    {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .size(height = 50.dp, width = 500.dp), color = Color(
                0xFFC1C1C1
            )
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.backgroundbottombar),
                    contentDescription = "background",
                    contentScale = ContentScale.FillWidth
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.homeblack),
                    contentDescription = "home",
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.notifications_black),
                    contentDescription = "notifications",
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("notifications_page/$myId") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.add_black),
                    contentDescription = "new post",
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("create_quote_page/$myId") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.chat_black),
                    contentDescription = "messages",
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("messages_page/${myId}") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile),
                    contentDescription = "my profile",
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("my_profile_page/$myId") }
                        .size(28.dp, 31.dp))
            }
        }
    }
}