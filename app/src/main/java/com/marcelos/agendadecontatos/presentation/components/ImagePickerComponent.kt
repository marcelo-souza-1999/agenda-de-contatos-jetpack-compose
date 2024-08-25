package com.marcelos.agendadecontatos.presentation.components

import android.os.Build
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
import androidx.lifecycle.lifecycleScope
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.utils.ImageProcessing
import com.marcelos.agendadecontatos.utils.PermissionsHandler.handlePermissionDenial
import com.marcelos.agendadecontatos.utils.PermissionsHandler.requestPermissionForOption
import com.patrik.fancycomposedialogs.dialogs.SuccessFancyDialog
import com.patrik.fancycomposedialogs.enums.DialogActionType
import com.patrik.fancycomposedialogs.enums.DialogStyle
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties
import kotlinx.coroutines.launch

@Composable
fun ImagePicker() {
    val context = LocalContext.current
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val activity = context as ComponentActivity

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            activity.lifecycleScope.launch {
                selectedImage = ImageProcessing.decodeImageFromUri(context, it)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            activity.lifecycleScope.launch {
                val rotatedBitmap = ImageProcessing.correctImageRotation(it)
                selectedImage = rotatedBitmap.asImageBitmap()
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        } else {
            handlePermissionDenial(context, android.Manifest.permission.CAMERA)
        }
    }

    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch(context.getString(R.string.filter_gallery))
        } else {
            handlePermissionDenial(
                context,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    android.Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                }
            )
        }
    }

    ImagePickerColumn(
        selectedImage = selectedImage,
        expanded = expanded,
        onImageClick = { expanded = true },
        onDismissMenu = { expanded = false },
        onCameraOptionClick = {
            requestPermissionForOption(
                context = context,
                option = context.getString(R.string.option_camera),
                cameraPermissionLauncher = cameraPermissionLauncher,
                galleryPermissionLauncher = galleryPermissionLauncher,
                onPermissionGranted = { cameraLauncher.launch() }
            )
            expanded = false
        },
        onGalleryOptionClick = {
            requestPermissionForOption(
                context = context,
                option = context.getString(R.string.option_gallery),
                cameraPermissionLauncher = cameraPermissionLauncher,
                galleryPermissionLauncher = galleryPermissionLauncher,
                onPermissionGranted = { galleryLauncher.launch(context.getString(R.string.filter_gallery)) }
            )
            expanded = false
        }
    )
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
        expanded = expanded,
        onDismissRequest = { onDismissMenu() },
        properties = PopupProperties()
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
private fun ShowSuccessDialog(
    onDismiss: () -> Unit,
    typePermission: String
) {
    SuccessFancyDialog(
        showTitle = true,
        title = "Permissão concedida",
        message = "Permissão para $typePermission concedida!",
        dialogStyle = DialogStyle.UPPER_CUTTING,
        dialogActionType = DialogActionType.INFORMATIVE,
        isCancelable = true,
        dialogProperties = DialogButtonProperties(
            neutralButtonText = R.string.postive_btn_text_success_dialog
        ),
        dismissTouchOutside = onDismiss,
        neutralButtonClick = onDismiss
    )
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun PreviewImagePicker() {
    ImagePicker()
}

