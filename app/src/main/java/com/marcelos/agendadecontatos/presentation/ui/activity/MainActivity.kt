package com.marcelos.agendadecontatos.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.integerResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.theme.Purple700
import com.marcelos.agendadecontatos.presentation.ui.navigation.Screen
import com.marcelos.agendadecontatos.presentation.ui.screens.SaveContactScreen
import com.marcelos.agendadecontatos.presentation.ui.screens.ShowContacts
import com.marcelos.agendadecontatos.presentation.ui.screens.UpdateContactScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = Purple700.toArgb()
            )
        )
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
    val duration = integerResource(id = R.integer.duration_animation)

    NavHost(
        navController = navController,
        startDestination = Screen.ShowContacts.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(duration)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(duration)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(duration)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(duration)
            )
        }
    ) {
        composable(route = Screen.ShowContacts.route) {
            ShowContacts(navController)
        }
        composable(route = Screen.SaveContact.route) {
            SaveContactScreen(navController)
        }
        composable(route = Screen.UpdateContact.route) {
            UpdateContactScreen(navController)
        }
    }
}

