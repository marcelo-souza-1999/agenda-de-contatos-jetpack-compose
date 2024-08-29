package com.marcelos.agendadecontatos.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme

@Composable
fun FormOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        maxLines = 1,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = cursorColor,
            focusedBorderColor = focusedBorderColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.size_20),
                end = dimensionResource(id = R.dimen.size_20),
                bottom = dimensionResource(id = R.dimen.size_12)
            )
    )
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun PreviewCustomOutlinedTextField() {
    ContactsAgendaTheme {
        FormOutlinedTextField(
            value = "John Doe",
            onValueChange = {},
            label = "Name",
            keyboardType = KeyboardType.Text
        )
    }
}