package com.marcelos.agendadecontatos.presentation.ui.screens

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.presentation.components.ErrorDialog
import com.marcelos.agendadecontatos.presentation.components.FormOutlinedTextField
import com.marcelos.agendadecontatos.presentation.components.ImagePicker
import com.marcelos.agendadecontatos.presentation.components.PrimaryButton
import com.marcelos.agendadecontatos.presentation.components.SuccessDialog
import com.marcelos.agendadecontatos.presentation.components.TopAppBar
import com.marcelos.agendadecontatos.presentation.extensions.ageMask
import com.marcelos.agendadecontatos.presentation.extensions.phoneMask
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.theme.White
import com.marcelos.agendadecontatos.presentation.ui.navigation.Routes
import com.marcelos.agendadecontatos.presentation.viewmodel.ContactsViewModel
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import com.marcelos.agendadecontatos.utils.Constants.EXTENSION_IMAGE
import com.marcelos.agendadecontatos.utils.Constants.FILE_PATH_IMAGE_NAME
import com.marcelos.agendadecontatos.utils.Constants.QUALITY_IMAGE
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun SaveContactScreen(
    navController: NavController, viewModel: ContactsViewModel = koinViewModel()
) {
    val context: Context = LocalContext.current
    val image by viewModel.image.collectAsState()
    val name by viewModel.name.collectAsState()
    val surname by viewModel.surname.collectAsState()
    val age by viewModel.age.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val viewStateSaveContact by viewModel.viewStateSaveContact.collectAsState()
    var showSaveContactSuccessDialog by remember { mutableStateOf(false) }
    var showSaveContactErrorDialog by remember { mutableStateOf(false) }

    val nameError by viewModel.nameError.collectAsState()
    val surnameError by viewModel.surnameError.collectAsState()
    val ageError by viewModel.ageError.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()

    val scrollState = rememberScrollState()

    Scaffold(topBar = {
        TopAppBar(
            title = stringResource(R.string.title_save_contacts_top_app_bar)
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .imePadding(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20)))

            ContactImageSection(image = image, onImageSelected = viewModel::updateImage)

            ContactFormFields(name = name,
                surname = surname,
                age = age,
                phone = phone,
                nameError = nameError,
                surnameError = surnameError,
                ageError = ageError,
                phoneError = phoneError,
                onNameChange = viewModel::updateName,
                onSurnameChange = viewModel::updateSurname,
                onAgeChange = { viewModel.updateAge(it.ageMask()) },
                onPhoneChange = { viewModel.updatePhone(it.phoneMask()) })

            fun fetchSaveContact() = viewModel.saveContact(
                imagePath = getPathImage(
                    context = context,
                    imageBitmap = image
                ),
                name = name,
                surname = surname,
                age = age.toInt(),
                phone = phone
            )

            PrimaryButton(
                onClickBtn = {
                    if (viewModel.validateFields()) {
                        fetchSaveContact()
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.size_20)),
                text = stringResource(R.string.text_btn_save)
            )

            LaunchedEffect(viewStateSaveContact) {
                if (viewStateSaveContact is State.Success) showSaveContactSuccessDialog = true
                else if (viewStateSaveContact is State.Error) showSaveContactErrorDialog = true
            }

            HandleSaveContactDialogs(showSaveContactSuccessDialog = showSaveContactSuccessDialog,
                showSaveContactErrorDialog = showSaveContactErrorDialog,
                onSuccess = {
                    showSaveContactSuccessDialog = false
                    navController.navigate(Routes.ShowContacts.route) {
                        popUpTo(Routes.SaveContact.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onError = {
                    showSaveContactErrorDialog = false
                },
                onRetry = {
                    showSaveContactErrorDialog = false
                    fetchSaveContact()
                })
        }
    }
}

private fun getPathImage(
    context: Context, imageBitmap: ImageBitmap?
): String? {
    val bitmap = imageBitmap?.asAndroidBitmap()

    val fileName = "${FILE_PATH_IMAGE_NAME}${System.currentTimeMillis()}$EXTENSION_IMAGE"
    val file = File(context.filesDir, fileName)

    file.outputStream().use { outputStream ->
        bitmap?.compress(
            Bitmap.CompressFormat.PNG, QUALITY_IMAGE, outputStream
        )
    }

    return file.absolutePath
}

@Composable
private fun HandleSaveContactDialogs(
    showSaveContactSuccessDialog: Boolean,
    showSaveContactErrorDialog: Boolean,
    onSuccess: () -> Unit,
    onError: () -> Unit,
    onRetry: () -> Unit
) {
    if (showSaveContactSuccessDialog) {
        SuccessDialog(
            title = stringResource(R.string.title_save_contact_success_dialog),
            message = stringResource(id = R.string.message_save_contact_success_dialog),
            isCancelable = false,
            dialogButtonProperties = DialogButtonProperties(
                neutralButtonText = R.string.txt_btn_positive_save_contact_success_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = onSuccess,
            onDismissClick = onSuccess
        )
    }

    if (showSaveContactErrorDialog) {
        ErrorDialog(
            title = stringResource(R.string.title_save_contact_error_dialog),
            message = stringResource(id = R.string.message_save_contact_error_dialog),
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                positiveButtonText = R.string.txt_btn_positive_error_dialog,
                negativeButtonText = R.string.txt_btn_negative_error_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = onRetry,
            onDismissClick = onError
        )
    }
}

@Composable
private fun ContactImageSection(
    image: ImageBitmap?, onImageSelected: (ImageBitmap?) -> Unit
) {
    ImagePicker(
        selectedImage = image, onImageSelected = onImageSelected
    )
}

@Composable
private fun ContactFormFields(
    name: String,
    surname: String,
    age: String,
    phone: String,
    nameError: Boolean,
    surnameError: Boolean,
    ageError: Boolean,
    phoneError: Boolean,
    onNameChange: (String) -> Unit,
    onSurnameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {
    FormOutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        isError = nameError,
        errorMessage = stringResource(R.string.error_message_required_field),
        label = stringResource(R.string.text_label_name),
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_50))
    )
    FormOutlinedTextField(
        value = surname,
        onValueChange = onSurnameChange,
        isError = surnameError,
        errorMessage = stringResource(R.string.error_message_required_field),
        label = stringResource(R.string.text_label_surname)
    )
    FormOutlinedTextField(
        value = age,
        onValueChange = onAgeChange,
        isError = ageError,
        errorMessage = stringResource(R.string.error_message_required_field),
        label = stringResource(R.string.text_label_age),
        keyboardType = KeyboardType.Number
    )
    FormOutlinedTextField(
        value = phone,
        onValueChange = onPhoneChange,
        isError = phoneError,
        errorMessage = stringResource(R.string.error_message_required_field),
        label = stringResource(R.string.text_label_phone),
        keyboardType = KeyboardType.Phone
    )
}


@Preview(
    showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun PreviewSaveContacts() {
    ContactsAgendaTheme {
        val fakeImage: ImageBitmap? = null
        val fakeName = "John"
        val fakeSurname = "Doe"
        val fakeAge = "30"
        val fakePhone = "1234567890"
        val fakeNameError = false
        val fakeSurnameError = false
        val fakeAgeError = false
        val fakePhoneError = false

        Scaffold(topBar = {
            TopAppBar(
                title = stringResource(R.string.title_save_contacts_top_app_bar)
            )
        }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20)))

                ContactImageSection(image = fakeImage, onImageSelected = {})

                ContactFormFields(name = fakeName,
                    surname = fakeSurname,
                    age = fakeAge,
                    phone = fakePhone,
                    nameError = fakeNameError,
                    surnameError = fakeSurnameError,
                    ageError = fakeAgeError,
                    phoneError = fakePhoneError,
                    onNameChange = {},
                    onSurnameChange = {},
                    onAgeChange = {},
                    onPhoneChange = {})

                PrimaryButton(
                    onClickBtn = {},
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.size_20)),
                    text = stringResource(R.string.text_btn_save)
                )
            }
        }
    }
}

