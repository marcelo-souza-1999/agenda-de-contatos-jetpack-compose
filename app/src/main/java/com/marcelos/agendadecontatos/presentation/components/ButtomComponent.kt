package com.marcelos.agendadecontatos.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.Purple500
import com.marcelos.agendadecontatos.presentation.theme.TypographyTitle

@Composable
fun ShowButton(
    onClickBtn: () -> Unit,
    modifier: Modifier = Modifier,
    text: String
) {
    Button(
        onClick = {
            onClickBtn.invoke()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple500
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = TypographyTitle.titleLarge
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewShowButton() {
    val context = LocalContext.current

    ShowButton(
        onClickBtn = {
            Log.d("BtnClick", "Botão clicado")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.size_20)),
        text = context.getString(R.string.text_btn_save)
    )
}