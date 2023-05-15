@file:OptIn(ExperimentalCoilApi::class, ExperimentalComposeUiApi::class)
@file:Suppress("DEPRECATION")

package com.onurdemirbas.quicktation.view

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.EditProfileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.ByteArrayOutputStream
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditProfilePage(navController: NavController, myId: Int, viewModel: EditProfileViewModel = hiltViewModel()) {
    val permissionState = rememberMultiplePermissionsState(permissions = listOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    var permCheck by remember { mutableStateOf(false) }
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { MutableStateFlow<Bitmap?>(null) }
    val bitmapState = bitmap.collectAsState()
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }
    var photoChanged by remember { mutableStateOf(false) }
    val byteArrayOutputStream = ByteArrayOutputStream()
    var byteArray by remember { mutableStateOf<ByteArray?>(null) }
    var encoded by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester()}
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = myId){
        viewModel.loadUser(myId)
    }
    LaunchedEffect(key1 = permissionState){
        if(permissionState.allPermissionsGranted) {
            permCheck = true
        } else {
            permissionState.launchMultiplePermissionRequest()
            if(permissionState.shouldShowRationale) {
                snackbarHostState.showSnackbar("Lütfen bu izinleri uygulama ayarlarından açın!")
            }
        }
    }
    LaunchedEffect(key1 = imageUri) {
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
                photoChanged = true
                bitmap.value!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                byteArray = byteArrayOutputStream.toByteArray()
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        }
    }
    Scaffold(topBar = {

    }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, content = {
        Surface(Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.mainbg), contentDescription = "background image", contentScale = ContentScale.FillHeight)
        }
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.padding(top = 25.dp))
            Text(text = "PROFİLİ DÜZENLE", fontSize = 18.sp, fontFamily = openSansBold)
            Spacer(modifier = Modifier.padding(top = 15.dp))
            Divider(
                thickness = 1.dp,
                color = Color.Black,
                modifier = Modifier.size(300.dp, 1.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize()) {
                ProfilePhotoBox(
                    photoChanged = photoChanged,
                    bitmapState = bitmapState.value,
                    userPhotoAlr = userPhotoAlr,
                    userPhotoForService = userPhotoForService,
                    launcher = launcher,
                    permCheck = permCheck,
                    permissionState = permissionState
                )
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }),
                    modifier = Modifier.focusRequester(focusRequester),
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
                OutlinedTextField(
                    value = nameSurname.value,
                    onValueChange = {
                        nameSurname.value = it
                    },
                    modifier = Modifier.focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }),
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
                Spacer(modifier = Modifier.padding(0.dp))
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.loadEdit(myId, nameSurname.value.text, encoded ?: "", userName.value.text)
                        Toast.makeText(context, "Değişiklikler Kaydedildi.", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
                    border = BorderStroke(1.dp, color = Color.Black),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.size(130.dp, 50.dp)
                ) {
                    Text(text = "KAYDET", color = Color.Black, fontFamily = openSansBold)
                }
                Spacer(modifier = Modifier.padding(0.dp))
            }
        }
    }, bottomBar = {
        BottomNavigationForEditProfilePage(navController = navController, userId = myId)
    })
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfilePhotoBox(
    photoChanged: Boolean,
    bitmapState: Bitmap?,
    userPhotoAlr: String?,
    userPhotoForService: Bitmap?,
    launcher: ActivityResultLauncher<String>,
    permCheck: Boolean,
    permissionState: MultiplePermissionsState
) {
    val scale by animateFloatAsState(if (photoChanged) 1.1f else 1f)
    Box(contentAlignment = Alignment.Center)
    {
        if (photoChanged) {
            bitmapState?.let { bitmap ->
                Image(bitmap = bitmap.asImageBitmap(),
                    contentDescription = "profile photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .scale(scale)
                )
            }
            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.size(150.dp))
            {
                IconButton(onClick = {
                    launcher.launch("image/*")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.addphoto),
                        contentDescription = "add photo",
                        Modifier.size(30.dp, 30.dp)
                    )
                }
            }
        } else if (userPhotoAlr == "" || userPhotoAlr == null || userPhotoAlr == "null") {
            if (userPhotoForService == null) {
                Image(
                    painter = painterResource(id = R.drawable.pp),
                    contentDescription = "profile photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    bitmap = userPhotoForService.asImageBitmap(),
                    contentDescription = "profile photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }
            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.size(150.dp))
            {
                IconButton(onClick = {
                    if(permCheck)
                    {
                        launcher.launch("image/*")
                    }
                    else
                    {
                        permissionState.launchMultiplePermissionRequest()
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.addphoto),
                        contentDescription = "add photo",
                        Modifier.size(30.dp, 30.dp)
                    )
                }
            }
        } else {
            val painter =
                rememberImagePainter(data = Constants.MEDIA_URL + userPhotoAlr, builder = {})
            Image(
                painter = painter,
                contentDescription = "profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.size(140.dp))
            {
                IconButton(onClick = {
                    launcher.launch("image/*")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.addphoto),
                        contentDescription = "add photo",
                        Modifier.size(30.dp, 30.dp)
                    )
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation", "AutoboxingStateValueProperty")
@Composable
fun BottomNavigationForEditProfilePage(navController: NavController, userId: Int) {
    val selectedIndex = remember { mutableStateOf(0) }
    val navigationItems = listOf(
        NavigationItem("Ana Sayfa", R.drawable.homeblack, "home_page"),
        NavigationItem("Bildirimler", R.drawable.notifications_black,"notifications_page/${userId}"),
        NavigationItem("Ekle", R.drawable.add_black,"create_quote_page/${userId}"),
        NavigationItem("Mesajlar", R.drawable.chat_black,"messages_page/${userId}"),
        NavigationItem("Profil", R.drawable.profile,"my_profile_page/${userId}"))
    BottomNavigation(backgroundColor = Color.DarkGray, contentColor = LocalContentColor.current) {
        navigationItems.forEachIndexed{ index, item ->
            BottomNavigationItem(
                icon = { CustomIcon(item.iconResId, contentDescription = item.title) },
                selected = selectedIndex.value == index,
                onClick = {
                    selectedIndex.value = index
                    navController.navigate(item.route){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Black
            )
        }
    }
}