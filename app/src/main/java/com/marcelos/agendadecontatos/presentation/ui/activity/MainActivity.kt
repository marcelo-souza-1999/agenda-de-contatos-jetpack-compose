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
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.theme.Purple700
import com.marcelos.agendadecontatos.presentation.ui.navigation.Routes
import com.marcelos.agendadecontatos.presentation.ui.screens.SaveContactScreen
import com.marcelos.agendadecontatos.presentation.ui.screens.ShowContacts
import com.marcelos.agendadecontatos.presentation.ui.screens.UpdateContactScreen
import com.marcelos.agendadecontatos.utils.Constants.ID_PARAMETER_NAVIGATE

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = Purple700.toArgb()
            )
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
        startDestination = Routes.ShowContacts.route,
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
        composable(route = Routes.ShowContacts.route) {
            ShowContacts(navController)
        }
        composable(route = Routes.SaveContact.route) {
            SaveContactScreen(navController)
        }
        composable(
            route = "${Routes.UpdateContact.route}/{${ID_PARAMETER_NAVIGATE}}",
            arguments = listOf(
                navArgument(ID_PARAMETER_NAVIGATE) { type = NavType.IntType }
            )
        ) {
            UpdateContactScreen(navController)
        }
    }
}

