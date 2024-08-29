package com.marcelos.agendadecontatos.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.components.FormOutlinedTextField
import com.marcelos.agendadecontatos.presentation.components.ImagePicker
import com.marcelos.agendadecontatos.presentation.components.PrimaryButton
import com.marcelos.agendadecontatos.presentation.components.TopAppBar
import com.marcelos.agendadecontatos.presentation.extensions.ageMask
import com.marcelos.agendadecontatos.presentation.extensions.phoneMask
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.ui.navigation.Screen
import com.marcelos.agendadecontatos.presentation.viewmodel.ContactViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule

@Composable
fun SaveContactScreen(
    navController: NavController,
    viewModel: ContactViewModel = koinViewModel()
) {
    val image by viewModel.image.collectAsState()
    val name by viewModel.name.collectAsState()
    val surname by viewModel.surname.collectAsState()
    val age by viewModel.age.collectAsState()
    val phone by viewModel.phone.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val surnameError by viewModel.surnameError.collectAsState()
    val ageError by viewModel.ageError.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.title_save_contacts_top_app_bar)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20)))

            ImagePicker(
                selectedImage = image,
                onImageSelected = { viewModel.updateImage(it) }
            )

            FormOutlinedTextField(
                value = name,
                onValueChange = { viewModel.updateName(it) },
                isError = nameError,
                errorMessage = stringResource(R.string.error_message_required_field),
                label = stringResource(R.string.text_label_name),
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_50))
            )
            FormOutlinedTextField(
                value = surname,
                onValueChange = { viewModel.updateSurname(it) },
                isError = surnameError,
                errorMessage = stringResource(R.string.error_message_required_field),
                label = stringResource(R.string.text_label_surname)
            )
            FormOutlinedTextField(
                value = age,
                onValueChange = { viewModel.updateAge(it.ageMask()) },
                isError = ageError,
                errorMessage = stringResource(R.string.error_message_required_field),
                label = stringResource(R.string.text_label_age),
                keyboardType = KeyboardType.Number
            )
            FormOutlinedTextField(
                value = phone,
                onValueChange = { viewModel.updatePhone(it.phoneMask()) },
                isError = phoneError,
                errorMessage = stringResource(R.string.error_message_required_field),
                label = stringResource(R.string.text_label_phone),
                keyboardType = KeyboardType.Phone
            )

            PrimaryButton(
                onClickBtn = {
                    if (viewModel.validateFields()) {
                        navController.navigate(Screen.UpdateContact.route)
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.size_20)),
                text = stringResource(R.string.text_btn_save)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun PreviewSaveContacts() {
    ContactsAgendaTheme {
        val context = LocalContext.current
        startKoin {
            androidContext(context)
            modules(defaultModule)
        }
        SaveContactScreen(navController = rememberNavController())
    }
}
