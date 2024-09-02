package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.theme.White
import com.patrik.fancycomposedialogs.dialogs.SuccessFancyDialog
import com.patrik.fancycomposedialogs.enums.DialogActionType
import com.patrik.fancycomposedialogs.enums.DialogStyle
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties

@Composable
fun SuccessDialog(
    title: String,
    message: String,
    isCancelable: Boolean,
    dialogButtonProperties: DialogButtonProperties,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    SuccessFancyDialog(
        title = title,
        showTitle = true,
        showMessage = true,
        message = message,
        isCancelable = isCancelable,
        dialogActionType = DialogActionType.INFORMATIVE,
        dialogProperties = dialogButtonProperties,
        dialogStyle = DialogStyle.UPPER_CUTTING,
        neutralButtonClick = {
            onConfirmClick.invoke()
            onDismissClick.invoke()
        },
        dismissTouchOutside = onDismissClick
    )
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
internal fun PreviewSuccessDialog() {
    ContactsAgendaTheme {
        SuccessDialog(
            title = stringResource(R.string.title_save_contact_success_dialog),
            message = stringResource(id = R.string.message_save_contact_success_dialog),
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                neutralButtonText = R.string.txt_btn_positive_save_contact_success_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = {},
            onDismissClick = {}
        )
    }
}