package com.marcelos.agendadecontatos.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.ui.navigation.Screen
import com.marcelos.agendadecontatos.presentation.ui.screens.SaveContact
import com.marcelos.agendadecontatos.presentation.ui.screens.ShowContacts
import com.marcelos.agendadecontatos.presentation.ui.screens.UpdateContact

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactsAgendaApp()
        }
    }
}

@Composable
fun ContactsAgendaApp() {
    ContactsAgendaTheme {
        SetupNavigation()
    }
}

@Composable
private fun SetupNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ShowContacts.route
    ) {
        composable(route = Screen.ShowContacts.route) {
            ShowContacts(navController)
        }
        composable(route = Screen.SaveContact.route) {
            SaveContact(navController)
        }
        composable(route = Screen.UpdateContact.route) {
            UpdateContact(navController)
        }
    }
}
