package com.marcelos.agendadecontatos.presentation.components

import android.util.Log
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.theme.White

@Composable
fun ActionFloatingButton(
    onClick: () -> Unit,
    icon: ImageVector,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = White
) {
    FloatingActionButton(
        onClick = {
            onClick()
        },
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = dimensionResource(id = R.dimen.size_8)
        ),
        containerColor = backgroundColor,
        contentColor = contentColor,
        shape = CircleShape,
        modifier = Modifier
            .testTag("floatingActionBtn")
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = White
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
internal fun PreviewFloatingActionButton() {
    ContactsAgendaTheme {
        ActionFloatingButton(
            onClick = {
                Log.d("FloatingActionButton", "Click")
            },
            backgroundColor = MaterialTheme.colorScheme.primary,
            icon = Icons.Default.Add
        )
    }
}