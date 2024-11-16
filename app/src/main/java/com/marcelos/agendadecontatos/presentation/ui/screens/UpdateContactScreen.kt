package com.marcelos.agendadecontatos.presentation.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.marcelos.agendadecontatos.R
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.presentation.components.ErrorDialog
import com.marcelos.agendadecontatos.presentation.components.FormOutlinedTextField
import com.marcelos.agendadecontatos.presentation.components.ImagePicker
import com.marcelos.agendadecontatos.presentation.components.PrimaryButton
import com.marcelos.agendadecontatos.presentation.components.SuccessDialog
import com.marcelos.agendadecontatos.presentation.components.TopAppBar
import com.marcelos.agendadecontatos.presentation.theme.ContactsAgendaTheme
import com.marcelos.agendadecontatos.presentation.theme.White
import com.marcelos.agendadecontatos.presentation.ui.navigation.Routes
import com.marcelos.agendadecontatos.presentation.viewmodel.ContactsViewModel
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import com.marcelos.agendadecontatos.utils.Constants.ID_PARAMETER_NAVIGATE
import com.marcelos.agendadecontatos.utils.ageMask
import com.marcelos.agendadecontatos.utils.phoneMask
import com.marcelos.agendadecontatos.utils.saveToPathImage
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties
import org.koin.androidx.compose.koinViewModel

@Composable
fun UpdateContactScreen(
    navController: NavController, viewModel: ContactsViewModel = koinViewModel()
) {
    val context: Context = LocalContext.current
    val viewStateGetContact by viewModel.viewStateGetContacts.collectAsState()
    val viewStateSaveContact by viewModel.viewStateSaveContact.collectAsState()

    val id by viewModel.contactId.collectAsState()
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
    var showContactErrorDialog by remember { mutableStateOf(false) }
    var showUpdateContactErrorDialog by remember { mutableStateOf(false) }
    var showUpdateContactSuccessDialog by remember { mutableStateOf(false) }

    val contactId = navController.currentBackStackEntry?.arguments?.getInt(ID_PARAMETER_NAVIGATE)
    contactId?.let { contactIdParam ->
        viewModel.updateContactId(contactIdParam)
        viewModel.getContact(contactIdParam)
    }

    fun fetchSaveContact() = viewModel.saveContact(
        contactId = id,
        imagePath = saveToPathImage(context = context, imageBitmap = image),
        name = name,
        surname = surname,
        age = age.toInt(),
        phone = phone
    )

    Scaffold(topBar = {
        TopAppBar(
            title = stringResource(R.string.title_update_contacts_top_app_bar)
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HandleGetContact(
                state = viewStateGetContact,
                onError = { showContactErrorDialog = true },
                viewModel = viewModel
            )

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
                text = stringResource(R.string.text_btn_update)
            )

            LaunchedEffect(viewStateSaveContact) {
                if (viewStateSaveContact is State.Success) showUpdateContactSuccessDialog = true
                else if (viewStateSaveContact is State.Error) showUpdateContactErrorDialog = true
            }

            if (showContactErrorDialog) {
                ErrorDialogView(onDismiss = {
                    showContactErrorDialog = false
                    id?.let { viewModel.getContact(it) }
                })
            }

            HandleUpdateContactDialogs(
                showUpdateContactSuccessDialog = showUpdateContactSuccessDialog,
                showUpdateContactErrorDialog = showUpdateContactErrorDialog,
                onSuccess = {
                    showUpdateContactSuccessDialog = false
                    navController.navigate(Routes.ShowContacts.route) {
                        popUpTo(Routes.ShowContacts.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onError = {
                    showUpdateContactErrorDialog = false
                },
                onRetry = {
                    showUpdateContactSuccessDialog = false
                    showUpdateContactErrorDialog = false
                    fetchSaveContact()
                })
        }
    }
}

@Composable
private fun HandleGetContact(
    state: State<List<ContactsViewData>>,
    onError: () -> Unit,
    viewModel: ContactsViewModel
) = when (state) {
    is State.Loading -> LoadingView()
    is State.Success -> handleSuccessState(state.data, viewModel)
    is State.Error -> onError()
}

private fun handleSuccessState(
    dataContact: List<ContactsViewData>, viewModel: ContactsViewModel
) {
    val contactData = dataContact.firstOrNull() ?: return
    viewModel.updateName(contactData.name)
    viewModel.updateSurname(contactData.surname)
    viewModel.updateAge(contactData.age.toString())
    viewModel.updatePhone(contactData.phone)
    viewModel.updateImage(contactData.imagePath?.let { imageBitmapFromPath(it) })
}

@Composable
private fun HandleUpdateContactDialogs(
    showUpdateContactSuccessDialog: Boolean,
    showUpdateContactErrorDialog: Boolean,
    onSuccess: () -> Unit,
    onError: () -> Unit,
    onRetry: () -> Unit
) {
    if (showUpdateContactSuccessDialog) {
        SuccessDialog(
            title = stringResource(R.string.title_update_contact_success_dialog),
            message = stringResource(id = R.string.message_update_contact_success_dialog),
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                neutralButtonText = R.string.txt_btn_neutral_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = onSuccess,
            onDismissClick = onSuccess
        )
    }

    if (showUpdateContactErrorDialog) {
        ErrorDialog(
            title = stringResource(R.string.title_update_contact_error_dialog),
            message = stringResource(id = R.string.message_update_contact_error_dialog),
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
) = ImagePicker(
    selectedImage = image, onImageSelected = onImageSelected
)

private fun imageBitmapFromPath(imagePath: String?) = imagePath?.let {
    BitmapFactory.decodeFile(it)?.asImageBitmap()
}

@Composable
private fun ErrorDialogView(
    onDismiss: () -> Unit
) = ErrorDialog(
    title = stringResource(R.string.title_show_contact_error_dialog),
    message = stringResource(R.string.message_show_contact_error_dialog),
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
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.size_50))
            .testTag("inputName")
    )
    FormOutlinedTextField(
        value = surname,
        onValueChange = onSurnameChange,
        isError = surnameError,
        errorMessage = stringResource(R.string.error_message_required_field),
        label = stringResource(R.string.text_label_surname),
        modifier = Modifier.testTag("inputSurname")
    )
    FormOutlinedTextField(
        value = age,
        onValueChange = onAgeChange,
        isError = ageError,
        errorMessage = stringResource(R.string.error_message_required_field),
        label = stringResource(R.string.text_label_age),
        keyboardType = KeyboardType.Number,
        modifier = Modifier.testTag("inputAge")
    )
    FormOutlinedTextField(
        value = phone,
        onValueChange = onPhoneChange,
        isError = phoneError,
        errorMessage = stringResource(R.string.error_message_required_field),
        label = stringResource(R.string.text_label_phone),
        keyboardType = KeyboardType.Phone,
        modifier = Modifier.testTag("inputPhone")
    )
}

@Composable
private fun LoadingView(modifier: Modifier = Modifier) = Box(
    modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
) {
    CircularProgressIndicator()
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
internal fun PreviewUpdateContacts() {
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
                title = stringResource(R.string.title_update_contacts_top_app_bar)
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
                    text = stringResource(R.string.text_btn_update)
                )
            }
        }
    }
}
