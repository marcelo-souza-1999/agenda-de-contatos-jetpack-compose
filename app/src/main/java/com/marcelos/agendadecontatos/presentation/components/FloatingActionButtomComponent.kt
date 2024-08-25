package com.marcelos.agendadecontatos.presentation.components

import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.Purple500
import com.marcelos.agendadecontatos.presentation.theme.White

@Composable
fun ShowFloatingActionButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            onClick()
        },
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = dimensionResource(id = R.dimen.size_8)
        ),
        containerColor = Purple500,
        contentColor = White,
        shape = CircleShape,
        modifier = Modifier
            .testTag("floatingActionBtn")
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = White
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun PreviewFloatingActionButton() {
    ShowFloatingActionButton {
        Log.d("FloatingActionButton", "Click")
    }
}