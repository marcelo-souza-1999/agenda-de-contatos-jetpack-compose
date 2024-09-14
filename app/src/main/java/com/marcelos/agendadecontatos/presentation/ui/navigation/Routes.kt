package com.marcelos.agendadecontatos.presentation.ui.navigation

sealed class Routes(val route: String) {
    data object ShowContacts : Routes("showContacts")
    data object SaveContact : Routes("saveContact")
    data object UpdateContact : Routes("updateContact")
}