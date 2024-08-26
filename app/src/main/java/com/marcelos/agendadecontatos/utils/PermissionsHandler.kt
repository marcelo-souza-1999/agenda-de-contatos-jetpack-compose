package com.marcelos.agendadecontatos.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.marcelos.agendadecontatos.R

object PermissionsHandler {

    fun requestPermissionForOption(
        context: Context,
        option: String,
        cameraPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
        galleryPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
        onPermissionRationaleNeeded: () -> Unit
    ) {
        when (option) {
            context.getString(R.string.option_camera) -> {
                handlePermission(
                    context = context,
                    permission = android.Manifest.permission.CAMERA,
                    permissionLauncher = cameraPermissionLauncher,
                    onPermissionRationaleNeeded = onPermissionRationaleNeeded
                )
            }

            context.getString(R.string.option_gallery) -> {
                val permission =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    } else {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    }
                handlePermission(
                    context = context,
                    permission = permission,
                    permissionLauncher = galleryPermissionLauncher,
                    onPermissionRationaleNeeded = onPermissionRationaleNeeded
                )
            }
        }
    }

    private fun handlePermission(
        context: Context,
        permission: String,
        permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
        onPermissionRationaleNeeded: () -> Unit
    ) {
        val activity = context as Activity

        when {
            ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                /**
                 * A permissão já foi concedida anteriormente. Continua com a ação relacionada.
                 */
                permissionLauncher.launch(permission)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                /**
                 * A permissão foi negada anteriormente. Mostra um diálogo explicando ao usuário
                 * por que a permissão é necessária antes de solicitar novamente.
                 */
                onPermissionRationaleNeeded()
            }

            else -> {
                /**
                 * Solicita a permissão pela primeira vez
                 */
                permissionLauncher.launch(permission)
            }
        }
    }
}