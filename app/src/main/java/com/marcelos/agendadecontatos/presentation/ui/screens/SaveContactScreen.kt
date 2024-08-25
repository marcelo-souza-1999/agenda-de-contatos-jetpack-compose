package com.marcelos.agendadecontatos.presentation.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SaveContactScreen(navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            ShowTopAppBar(
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

            ImagePicker()

            CustomOutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = stringResource(R.string.text_label_name),
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_50))
            )

            CustomOutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = stringResource(R.string.text_label_surname)
            )

            CustomOutlinedTextField(
                value = age,
                onValueChange = { age = ageMask(it) },
                label = stringResource(R.string.text_label_age),
                keyboardType = KeyboardType.Number
            )

            CustomOutlinedTextField(
                value = phone,
                onValueChange = { phone = phoneMask(it) },
                label = stringResource(R.string.text_label_phone),
                keyboardType = KeyboardType.Phone
            )

            ShowButton(
                onClickBtn = {
                    // Salvar Contato
                },
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
    SaveContactScreen(navController = rememberNavController())
}
