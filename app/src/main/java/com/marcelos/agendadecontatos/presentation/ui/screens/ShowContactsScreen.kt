package com.marcelos.agendadecontatos.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.presentation.components.ActionFloatingButton
import com.marcelos.agendadecontatos.presentation.components.ListContactsItem
import com.marcelos.agendadecontatos.presentation.components.TopAppBar
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.ui.navigation.Routes
import com.marcelos.agendadecontatos.presentation.viewmodel.ContactsViewModel
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShowContacts(
    navController: NavController,
    viewModel: ContactsViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val viewStateGetContacts by viewModel.viewStateGetContacts.collectAsState()

    fun fetchGetContacts() = viewModel.getContacts()

    LaunchedEffect(Unit) {
        fetchGetContacts()
    }

    Scaffold(topBar = {
        TopAppBar(
            title = context.getString(R.string.title_show_contacts_top_app_bar)
        )
    }, floatingActionButton = {
        ActionFloatingButton(icon = Icons.Default.Add,
            backgroundColor = MaterialTheme.colorScheme.primary,
            onClick = {
                navController.navigate(
                    route = Routes.SaveContact.route
                )
            })
    }) { innerPadding ->
        when (val state = viewStateGetContacts) {
            is State.Loading -> LoadingView(modifier = Modifier.padding(innerPadding))
            is State.Success -> ContactsList(
                contacts = state.data,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )

            is State.Error -> {

            }
        }
    }
}

@Composable
private fun ContactsList(
    contacts: List<ContactsViewData>,
    modifier: Modifier = Modifier
) {
    if (contacts.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_contacts_found),
                style = MaterialTheme.typography.titleLarge
            )
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(contacts) { contactData ->
                ListContactsItem(
                    contactsData = contactData
                )
            }
        }
    }
}


@Composable
private fun LoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(
    showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun PreviewShowContacts() {
    ContactsAgendaTheme {
        ShowContactsPreview(
            contacts = listOf(
                ContactsViewData(
                    name = "John",
                    surname = "Doe",
                    age = 30,
                    phone = "12123456789",
                    imagePath = null
                ),
                ContactsViewData(
                    name = "Jane",
                    surname = "Smith",
                    age = 28,
                    phone = "12996259393",
                    imagePath = null
                )
            )
        )
    }
}

@Composable
private fun ShowContactsPreview(
    contacts: List<ContactsViewData>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.title_show_contacts_top_app_bar)
            )
        },
        floatingActionButton = {
            ActionFloatingButton(
                icon = Icons.Default.Add,
                backgroundColor = MaterialTheme.colorScheme.primary,
                onClick = { /* No action for preview */ }
            )
        }
    ) { innerPadding ->
        ContactsList(
            contacts = contacts,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
    }
}
