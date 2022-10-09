@file:OptIn(ExperimentalCoilApi::class, ExperimentalPermissionsApi::class)
package com.onurdemirbas.quicktation.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.onurdemirbas.quicktation.R
import com.onurdemirbas.quicktation.ui.theme.openSansBold
import com.onurdemirbas.quicktation.ui.theme.openSansFontFamily
import com.onurdemirbas.quicktation.util.Constants
import com.onurdemirbas.quicktation.viewmodel.EditProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EditProfilePage(navController: NavController, myId: Int, viewModel: EditProfileViewModel = hiltViewModel()) {
    viewModel.viewModelScope.launch {
        viewModel.loadUser(myId)
    }
    val userInfo = viewModel.userInfo.collectAsState()
    val userNameAlr = userInfo.value.namesurname
    val userEmailAlr = userInfo.value.email
    val userPhotoAlr = userInfo.value.photo
    val username = remember { mutableStateOf(TextFieldValue()) }
    val email = remember { mutableStateOf(TextFieldValue()) }
    var userPhoto = remember { mutableStateOf("") }
    val photoFromVm = viewModel.userphoto.collectAsState()
    val interactionSource = MutableInteractionSource()
    val context = LocalContext.current
    val check = remember {mutableStateOf(false)}
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher2 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }
    if(check.value)
    {
        Popup(Alignment.Center, onDismissRequest = {check.value = !check.value}, properties = PopupProperties(focusable = true,dismissOnBackPress = true, dismissOnClickOutside = true)) {
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission Accepted: Do something
                    Toast.makeText(context,"a",Toast.LENGTH_LONG).show()

                } else {
                    // Permission Denied: Do something
                    Toast.makeText(context,"b",Toast.LENGTH_LONG).show()
                }
            }
            Button(
                onClick = {
                    // Check permission
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) -> {
                            // Some works that require permission
                            Toast.makeText(context,"c",Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            // Asking for permission
                            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                }
            ) {
                Text(text = "Check and Request Permission")
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
                    if (userPhotoAlr == "" || userPhotoAlr == null || userPhotoAlr == "null") {
                        Image(
                            painter = painterResource(id = R.drawable.pp),
                            contentDescription = "profile photo",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(100.dp)
                        )
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier.size(120.dp)
                        ) {
                            Image(painter = painterResource(id = R.drawable.addphoto),
                                contentDescription = "add photo",
                                Modifier
                                    .size(30.dp, 30.dp)
                                    .clickable {
                                        check.value = !check.value
                                    })
                        }
                    } else {
                        val painter = rememberImagePainter(
                            data = Constants.BASE_URL + userPhotoAlr,
                            builder = {})
                        Image(
                            painter = painter,
                            contentDescription = "profile photo",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(100.dp)
                        )
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier.size(120.dp)
                        ) {
                            Image(painter = painterResource(id = R.drawable.addphoto),
                                contentDescription = "add photo",
                                Modifier
                                    .size(30.dp, 30.dp)
                                    .clickable { userPhoto = userPhoto })
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
                    value = username.value,
                    onValueChange = {
                        username.value = it
                    },
                    textStyle = TextStyle(fontFamily = openSansFontFamily),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color(217, 217, 217, 255)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = {
                        Text(
                            text = userNameAlr
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
                Spacer(modifier = Modifier.padding(30.dp))
                Button(
                    onClick = {
                        viewModel.loadEdit(myId, username.value.text, userPhoto.value)
                        userPhoto.value = photoFromVm.value
                        viewModel.viewModelScope.launch {
                            delay(300)
                            Toast.makeText(context, "Değişiklikler Kaydedildi.", Toast.LENGTH_LONG)
                                .show()
                        }
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
                Image(painter = painterResource(id = R.drawable.home),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.notifications_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("notifications_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.add_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("home_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.chat_black),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource,
                            indication = null
                        ) { navController.navigate("messages_page") }
                        .size(28.dp, 31.dp))
                Image(painter = painterResource(id = R.drawable.profile_black),
                    contentDescription = null,
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


@Composable
fun Change() {
//    fun Context.getActivity(): AppCompatActivity? {
//        var currentContext = this
//        while (currentContext is ContextWrapper) {
//            if (currentContext is AppCompatActivity) {
//                return currentContext
//            }
//            currentContext = currentContext.baseContext
//        }
//        return null
//    }
//    val activity = LocalContext.current as Activity //activity


//    if(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
//    {
//        if(ActivityCompat.shouldShowRequestPermissionRationale(context.getActivity()!!.parent,Manifest.permission.READ_EXTERNAL_STORAGE))
//        {
//            val snackBarResult = rememberScaffoldState()
//            val x = rememberCoroutineScope()
//            x.launch {
//                when (snackBarResult.snackbarHostState.showSnackbar("message","do",SnackbarDuration.Indefinite))
//                {
//                    SnackbarResult.Dismissed -> ""
//                    SnackbarResult.ActionPerformed -> launcher.launch("image/*")
//                }
//            }
//
//        }
//        else
//        {
//            imageUri?.let {
//                if (Build.VERSION.SDK_INT < 28) {
//                    bitmap.value = MediaStore.Images
//                        .Media.getBitmap(context.contentResolver, it)
//                } else {
//                    val source = ImageDecoder.createSource(context.contentResolver, it)
//                    bitmap.value = ImageDecoder.decodeBitmap(source)
//                }
//                bitmap.value?.let { btm ->
//                    Image(
//                        bitmap = btm.asImageBitmap(),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(400.dp)
//                            .padding(20.dp)
//                    )
//                }
//            }
//        }
//    }
}