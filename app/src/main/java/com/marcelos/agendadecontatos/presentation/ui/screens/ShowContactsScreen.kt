package com.marcelos.agendadecontatos.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.components.ShowFloatingActionButton
import com.marcelos.agendadecontatos.presentation.components.ShowTopAppBar
import com.marcelos.agendadecontatos.presentation.ui.navigation.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowContacts(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            ShowTopAppBar(
                title = context.getString(R.string.title_show_contacts_top_app_bar)
            )
        },
        floatingActionButton = {
            ShowFloatingActionButton {
                navController.navigate(
                    route = Screen.SaveContact.route
                )
            }
        }
    ) {

    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun PreviewShowContacts() {
    ShowContacts(navController = rememberNavController())
}