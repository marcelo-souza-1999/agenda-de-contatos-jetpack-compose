package com.marcelos.agendadecontatos.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.components.CustomOutlinedTextField
import com.marcelos.agendadecontatos.presentation.components.ImagePicker
import com.marcelos.agendadecontatos.presentation.components.ShowButton
import com.marcelos.agendadecontatos.presentation.components.ShowTopAppBar
import com.marcelos.agendadecontatos.presentation.extensions.ageMask
import com.marcelos.agendadecontatos.presentation.extensions.phoneMask

@Composable
fun UpdateContactScreen(navController: NavController) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    var nameError by rememberSaveable { mutableStateOf(false) }
    var surnameError by rememberSaveable { mutableStateOf(false) }
    var ageError by rememberSaveable { mutableStateOf(false) }
    var phoneError by rememberSaveable { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            ShowTopAppBar(
                title = stringResource(R.string.title_update_contacts_top_app_bar)
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
                onImageSelected = { image = it }
            )

            CustomOutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                isError = nameError,
                errorMessage = stringResource(R.string.error_message_required_field),
                label = stringResource(R.string.text_label_name),
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_50))
            )
            CustomOutlinedTextField(
                value = surname,
                onValueChange = {
                    surname = it
                    surnameError = false
                },
                isError = surnameError,
                errorMessage = stringResource(R.string.error_message_required_field),
                label = stringResource(R.string.text_label_surname)
            )
            CustomOutlinedTextField(
                value = age,
                onValueChange = {
                    age = ageMask(it)
                    ageError = false
                },
                isError = ageError,
                errorMessage = stringResource(R.string.error_message_required_field),
                label = stringResource(R.string.text_label_age),
                keyboardType = KeyboardType.Number
            )
            CustomOutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = phoneMask(it)
                    phoneError = false
                },
                isError = phoneError,
                errorMessage = stringResource(R.string.error_message_required_field),
                label = stringResource(R.string.text_label_phone),
                keyboardType = KeyboardType.Phone
            )
            ShowButton(
                onClickBtn = {
                    if (validateFields(name, surname, age, phone,
                            onNameErrorChange = { nameError = it },
                            onSurnameErrorChange = { surnameError = it },
                            onAgeErrorChange = { ageError = it },
                            onPhoneErrorChange = { phoneError = it })
                    ) {

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.size_20)),
                text = stringResource(R.string.text_btn_update)
            )
        }
    }
}

private fun validateFields(
    name: String,
    surname: String,
    age: String,
    phone: String,
    onNameErrorChange: (Boolean) -> Unit,
    onSurnameErrorChange: (Boolean) -> Unit,
    onAgeErrorChange: (Boolean) -> Unit,
    onPhoneErrorChange: (Boolean) -> Unit
): Boolean {
    val nameError = name.isEmpty()
    val surnameError = surname.isEmpty()
    val ageError = age.isEmpty()
    val phoneError = phone.isEmpty()

    onNameErrorChange(nameError)
    onSurnameErrorChange(surnameError)
    onAgeErrorChange(ageError)
    onPhoneErrorChange(phoneError)

    return !nameError && !surnameError && !ageError && !phoneError
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun PreviewSaveContacts() {
    UpdateContactScreen(navController = rememberNavController())
}