package com.marcelos.agendadecontatos.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
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
        onPermissionGranted: () -> Unit
    ) {
        when (option) {
            context.getString(R.string.option_camera) -> {
                handlePermission(
                    context = context,
                    permission = android.Manifest.permission.CAMERA,
                    permissionLauncher = cameraPermissionLauncher,
                    onPermissionGranted = onPermissionGranted
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
                    onPermissionGranted = onPermissionGranted
                )
            }
        }
    }

    private fun handlePermission(
        context: Context,
        permission: String,
        permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
        onPermissionGranted: () -> Unit
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
                onPermissionGranted()
                permissionLauncher.launch(permission)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                /**
                 * A permissão foi negada anteriormente. Mostra um diálogo explicando ao usuário
                 * por que a permissão é necessária antes de solicitar novamente.
                 */
                Log.d(
                    "PermissionHandler",
                    "Permission sendo solicitada sem ser a primeira vez: $permission"
                )
                permissionLauncher.launch(permission)
            }

            else -> {
                /**
                 * Solicita a permissão pela primeira vez
                 */
                Log.d("PermissionHandler", "Solicitando permissao: : $permission")
                permissionLauncher.launch(permission)
            }
        }
    }

    fun handlePermissionDenial(context: Context, permission: String) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
            /**
             * A permissão foi negada várias vezes ou o usuário selecionou "Não perguntar novamente".
             * Direciona o usuário para as configurações do app para conceder a permissão manualmente.
             */
            openAppSettings(context)
        } else {
            /**
             * A permissão foi negada uma vez, mas o usuário não selecionou "Não perguntar novamente".
             * Registra a negação da permissão e pode tentar solicitá-la novamente.
             */
            Log.d("PermissionHandler", "Permissao foi negada uma vez: $permission")
        }
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
        }
        ContextCompat.startActivity(context, intent, null)
    }
}