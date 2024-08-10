package com.marcelos.agendadecontatos.presentation.ui.navigation

sealed class Screen(val route: String) {
    data object ShowContacts : Screen("showContacts")
    data object SaveContact : Screen("saveContact")
    data object UpdateContact : Screen("updateContact")
}