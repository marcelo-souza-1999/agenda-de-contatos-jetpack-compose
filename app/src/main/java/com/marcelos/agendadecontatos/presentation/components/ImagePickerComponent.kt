package com.marcelos.agendadecontatos.presentation.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.White
import com.marcelos.agendadecontatos.utils.ImageProcessing
import com.marcelos.agendadecontatos.utils.PermissionsHandler.requestPermissionForOption
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties
import kotlinx.coroutines.launch

@Suppress("LongMethod")
@Composable
fun ImagePicker(
    selectedImage: ImageBitmap? = null, onImageSelected: (ImageBitmap?) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var rationaleMessage by remember { mutableStateOf("") }
    var showPermissionWarningDialog by remember { mutableStateOf(false) }
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }
    var hasPermissionBeenDeniedBefore by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val activity = context as ComponentActivity
            activity.lifecycleScope.launch {
                onImageSelected(ImageProcessing.decodeImageFromUri(activity, it))
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val activity = context as ComponentActivity
            activity.lifecycleScope.launch {
                onImageSelected(ImageProcessing.correctImageRotation(it).asImageBitmap())
            }
        }
    }

    val cameraPermissionLauncher = createPermissionLauncher(launcher = {
        hasPermissionBeenDeniedBefore = false
        cameraLauncher.launch()
    }, onDenied = {
        hasPermissionBeenDeniedBefore = true
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity, android.Manifest.permission.CAMERA
            )
        ) {
            showPermissionDeniedDialog = true
        }
    })

    val galleryPermissionLauncher = createPermissionLauncher(launcher = {
        hasPermissionBeenDeniedBefore = false
        galleryLauncher.launch(context.getString(R.string.filter_gallery))
    }, onDenied = {
        hasPermissionBeenDeniedBefore = true
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    android.Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                }
            )
        ) showPermissionDeniedDialog = true
    })

    ImagePickerColumn(selectedImage = selectedImage,
        expanded = expanded,
        onImageClick = { expanded = true },
        onDismissMenu = { expanded = false },
        onCameraOptionClick = {
            requestPermissionForOption(context = context,
                option = context.getString(R.string.option_camera),
                cameraPermissionLauncher = cameraPermissionLauncher,
                galleryPermissionLauncher = galleryPermissionLauncher,
                onPermissionRationaleNeeded = {
                    rationaleMessage = context.getString(R.string.message_warning_permission_camera)
                    showPermissionWarningDialog = true
                })
            expanded = false
        },
        onGalleryOptionClick = {
            requestPermissionForOption(context = context,
                option = context.getString(R.string.option_gallery),
                cameraPermissionLauncher = cameraPermissionLauncher,
                galleryPermissionLauncher = galleryPermissionLauncher,
                onPermissionRationaleNeeded = {
                    rationaleMessage =
                        context.getString(R.string.message_warning_permission_gallery)
                    showPermissionWarningDialog = true
                })
            expanded = false
        })

    if (showPermissionWarningDialog) {
        WarningDialog(title = stringResource(R.string.title_permission_warning_dialog),
            message = rationaleMessage,
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                positiveButtonText = R.string.txt_btn_positive_permission_dialog,
                negativeButtonText = R.string.txt_btn_negative_permission_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = {
                showPermissionWarningDialog = false
                if (rationaleMessage.contains(
                        other = context.getString(R.string.filter_contains_camera),
                        ignoreCase = true
                    )
                ) {
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                } else {
                    galleryPermissionLauncher.launch(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                    )
                }
            },
            onDismissClick = { showPermissionWarningDialog = false })
    }

    if (showPermissionDeniedDialog && hasPermissionBeenDeniedBefore) {
        WarningDialog(title = stringResource(id = R.string.title_permission_denied_dialog),
            message = if (rationaleMessage.contains(
                    other = context.getString(R.string.filter_contains_camera), ignoreCase = true
                )
            ) {
                stringResource(id = R.string.message_denied_permission_camera)
            } else {
                stringResource(id = R.string.message_denied_permission_gallery)
            },
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                positiveButtonText = R.string.txt_btn_positive_permission_denied_dialog,
                negativeButtonText = R.string.txt_btn_negative_permission_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = {
                showPermissionDeniedDialog = false
                context.openScreenAppSettings()
            },
            onDismissClick = { showPermissionDeniedDialog = false })
    }
}

@Composable
private fun ImagePickerColumn(
    selectedImage: ImageBitmap?,
    expanded: Boolean,
    onImageClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onCameraOptionClick: () -> Unit,
    onGalleryOptionClick: () -> Unit
) {
    Column {
        Image(
            painter = selectedImage?.let { BitmapPainter(it) }
                ?: painterResource(R.drawable.ic_add_photo),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_100))
                .clip(RectangleShape)
                .clickable { onImageClick() },
            contentScale = ContentScale.Crop
        )

        ImagePickerDropdownMenu(
            expanded = expanded,
            onDismissMenu = onDismissMenu,
            onCameraOptionClick = onCameraOptionClick,
            onGalleryOptionClick = onGalleryOptionClick
        )
    }
}

@Composable
private fun ImagePickerDropdownMenu(
    expanded: Boolean,
    onDismissMenu: () -> Unit,
    onCameraOptionClick: () -> Unit,
    onGalleryOptionClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded, onDismissRequest = { onDismissMenu() }, properties = PopupProperties()
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.option_camera_menu)) },
            onClick = onCameraOptionClick
        )

        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.option_gallery_menu)) },
            onClick = onGalleryOptionClick
        )
    }
}

@Composable
private fun createPermissionLauncher(
    launcher: () -> Unit, onDenied: () -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) launcher() else onDenied()
}

private fun Context.openScreenAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:$packageName")
    }
    ContextCompat.startActivity(this, intent, null)
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
internal fun PreviewImagePicker() {
    Image(
        painter = painterResource(R.drawable.ic_add_photo),
        contentDescription = null,
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.size_100))
            .clip(RectangleShape),
        contentScale = ContentScale.Crop
    )
}
