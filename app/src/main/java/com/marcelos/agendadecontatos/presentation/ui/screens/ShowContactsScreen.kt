package com.marcelos.agendadecontatos.presentation.ui.screens

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.presentation.components.ActionFloatingButton
import com.marcelos.agendadecontatos.presentation.components.ErrorDialog
import com.marcelos.agendadecontatos.presentation.components.ListContactsItem
import com.marcelos.agendadecontatos.presentation.components.SuccessDialog
import com.marcelos.agendadecontatos.presentation.components.TopAppBar
import com.marcelos.agendadecontatos.presentation.components.WarningDialog
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.theme.White
import com.marcelos.agendadecontatos.presentation.ui.navigation.Routes
import com.marcelos.agendadecontatos.presentation.viewmodel.ContactsViewModel
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShowContactsScreen(
    navController: NavController,
    viewModel: ContactsViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val viewStateGetContacts by viewModel.viewStateGetContacts.collectAsState()
    val viewStateDeleteContact by viewModel.viewStateDeleteContact.collectAsState()

    var showContactsErrorDialog by remember { mutableStateOf(false) }
    var showDeleteContactWarningDialog by remember { mutableStateOf(false) }
    var showDeleteContactSuccessDialog by remember { mutableStateOf(false) }
    var showDeleteContactErrorDialog by remember { mutableStateOf(false) }

    var contactIdForRetry by rememberSaveable { mutableStateOf<Int?>(null) }

    fun fetchGetContacts() = viewModel.getContacts()
    fun fetchDeleteContact(contactId: Int) {
        contactIdForRetry = contactId
        viewModel.deleteContact(contactId)
    }

    LaunchedEffect(Unit) {
        fetchGetContacts()
        showDeleteContactSuccessDialog = false
        showDeleteContactErrorDialog = false
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
                navController = navController,
                contacts = state.data,
                onDeleteClick = { contactId ->
                    fetchDeleteContact(contactId)
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )

            is State.Error -> showContactsErrorDialog = true
        }

        LaunchedEffect(viewStateDeleteContact) {
            when (viewStateDeleteContact) {
                is State.Success -> {
                    showDeleteContactWarningDialog = true
                }

                is State.Error -> showDeleteContactErrorDialog = true
                else -> Unit
            }
        }
        HandleDeleteContactDialogs(
            showWarningDialog = showDeleteContactWarningDialog,
            showSuccessDialog = showDeleteContactSuccessDialog,
            showErrorDialog = showDeleteContactErrorDialog,
            onDeleteConfirmed = {
                fetchGetContacts()
                viewModel.resetDeleteContactState()
                showDeleteContactSuccessDialog = true
            },
            onCloseSuccessDialog = {
                showDeleteContactSuccessDialog = false
            },
            onCloseWarningDialog = {
                showDeleteContactWarningDialog = false
            },
            onCloseErrorDialog = {
                showDeleteContactErrorDialog = false
            },
            onRetryDelete = {
                showDeleteContactSuccessDialog = false
                showDeleteContactErrorDialog = false
                contactIdForRetry?.let { fetchDeleteContact(it) }
            }
        )
    }

    HandleContactsErrorDialog(
        showErrorDialog = showContactsErrorDialog,
        onDismissErrorDialog = {
            showContactsErrorDialog = false
            fetchGetContacts()
        }
    )
}

@Composable
private fun HandleDeleteContactDialogs(
    showWarningDialog: Boolean,
    showSuccessDialog: Boolean,
    showErrorDialog: Boolean,
    onDeleteConfirmed: () -> Unit,
    onCloseWarningDialog: () -> Unit,
    onCloseErrorDialog: () -> Unit,
    onCloseSuccessDialog: () -> Unit,
    onRetryDelete: () -> Unit
) {
    if (showWarningDialog) {
        WarningDialog(
            title = stringResource(R.string.title_delete_contact_warning_dialog),
            message = stringResource(R.string.message_delete_contact_warning_dialog),
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                positiveButtonText = R.string.txt_btn_positive_warning_contact_dialog,
                negativeButtonText = R.string.txt_btn_negative_warning_contact_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = onDeleteConfirmed,
            onDismissClick = onCloseWarningDialog
        )
    }

    if (showSuccessDialog) {
        SuccessDialog(
            title = stringResource(R.string.title_delete_contact_success_dialog),
            message = stringResource(R.string.message_delete_contact_success_dialog),
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                neutralButtonText = R.string.txt_btn_neutral_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = onCloseSuccessDialog,
            onDismissClick = onCloseSuccessDialog
        )
    }

    if (showErrorDialog) {
        ErrorDialog(
            title = stringResource(R.string.title_delete_contact_error_dialog),
            message = stringResource(R.string.message_delete_contact_error_dialog),
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                positiveButtonText = R.string.txt_btn_positive_error_dialog,
                negativeButtonText = R.string.txt_btn_negative_error_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = onRetryDelete,
            onDismissClick = onCloseErrorDialog
        )
    }
}

@Composable
private fun HandleContactsErrorDialog(
    showErrorDialog: Boolean,
    onDismissErrorDialog: () -> Unit
) {
    if (showErrorDialog) {
        ErrorDialogView(onDismiss = onDismissErrorDialog)
    }
}

@Composable
private fun ErrorDialogView(
    onDismiss: () -> Unit
) {
    ErrorDialog(
        title = stringResource(R.string.title_show_contacts_error_dialog),
        message = stringResource(R.string.message_show_contacts_error_dialog),
        isCancelable = true,
        dialogButtonProperties = DialogButtonProperties(
            positiveButtonText = R.string.txt_btn_positive_error_dialog,
            negativeButtonText = R.string.txt_btn_negative_error_dialog,
            buttonColor = MaterialTheme.colorScheme.primary,
            buttonTextColor = White
        ),
        onConfirmClick = onDismiss,
        onDismissClick = onDismiss
    )
}

@Composable
private fun ContactsList(
    navController: NavController,
    contacts: List<ContactsViewData>,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (contacts.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
                    navController = navController,
                    contactsData = contactData,
                    onDelete = onDeleteClick
                )
            }
        }
    }
}

@Composable
private fun LoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ShowContactsPreview(
    contacts: List<ContactsViewData>
) {
    Scaffold(topBar = {
        TopAppBar(
            title = stringResource(R.string.title_show_contacts_top_app_bar)
        )
    }, floatingActionButton = {
        ActionFloatingButton(icon = Icons.Default.Add,
            backgroundColor = MaterialTheme.colorScheme.primary,
            onClick = { /* No action for preview */ })
    }) { innerPadding ->
        ContactsList(
            navController = rememberNavController(),
            contacts = contacts,
            onDeleteClick = {
                Log.d("DeleteContact", "Ação de deletar contato")
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
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
                ), ContactsViewData(
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
