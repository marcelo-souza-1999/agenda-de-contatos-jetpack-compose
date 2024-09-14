package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.theme.White
import com.patrik.fancycomposedialogs.dialogs.ErrorFancyDialog
import com.patrik.fancycomposedialogs.enums.DialogStyle
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    isCancelable: Boolean,
    dialogButtonProperties: DialogButtonProperties,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    ErrorFancyDialog(
        title = title,
        showTitle = true,
        showMessage = true,
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
internal fun PreviewErrorDialog() {
    ContactsAgendaTheme {
        ErrorDialog(
            title = stringResource(R.string.title_save_contact_error_dialog),
            message = stringResource(id = R.string.message_save_contact_error_dialog),
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                positiveButtonText = R.string.txt_btn_positive_error_dialog,
                negativeButtonText = R.string.txt_btn_negative_error_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = {},
            onDismissClick = {}
        )
    }
}