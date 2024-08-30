package com.marcelos.agendadecontatos.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.components.ActionFloatingButton
import com.marcelos.agendadecontatos.presentation.components.TopAppBar
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.ui.navigation.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowContacts(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = context.getString(R.string.title_show_contacts_top_app_bar)
            )
        },
        floatingActionButton = {
            ActionFloatingButton(
                icon = Icons.Default.Add,
                backgroundColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navController.navigate(
                        route = Routes.SaveContact.route
                    )
                }
            )
        }
    ) {

    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
internal fun PreviewShowContacts() {
    ContactsAgendaTheme {
        ShowContacts(navController = rememberNavController())
    }
}