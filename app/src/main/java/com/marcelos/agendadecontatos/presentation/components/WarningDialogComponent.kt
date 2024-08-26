package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.Purple500
import com.marcelos.agendadecontatos.presentation.theme.White
import com.patrik.fancycomposedialogs.dialogs.WarningFancyDialog
import com.patrik.fancycomposedialogs.enums.DialogStyle
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties

@Composable
fun WarningDialog(
    title: String,
    message: String,
    isCancelable: Boolean,
    dialogButtonProperties: DialogButtonProperties,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    WarningFancyDialog(
        title = title,
        message = message,
        isCancelable = isCancelable,
        dialogProperties = dialogButtonProperties,
        dialogStyle = DialogStyle.UPPER_CUTTING,
        positiveButtonClick = {
            onConfirmClick.invoke()
            onDismissClick.invoke()
        },
        negativeButtonClick = onDismissClick,
        dismissTouchOutside = onDismissClick
    )
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun PreviewWarningDialog() {
    WarningDialog(
        title = stringResource(R.string.title_permission_warning_dialog),
        message = stringResource(id = R.string.message_warning_permission_camera),
        isCancelable = true,
        dialogButtonProperties = DialogButtonProperties(
            positiveButtonText = R.string.txt_btn_positive_permission_dialog,
            negativeButtonText = R.string.txt_btn_negative_permission_dialog,
            buttonColor = Purple500,
            buttonTextColor = White
        ),
        onConfirmClick = {},
        onDismissClick = {}
    )
}