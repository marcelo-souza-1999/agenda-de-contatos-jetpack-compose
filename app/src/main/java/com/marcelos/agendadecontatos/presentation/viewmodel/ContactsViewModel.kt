package com.marcelos.agendadecontatos.presentation.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.domain.usecase.DeleteContactUseCase
import com.marcelos.agendadecontatos.domain.usecase.GetContactUseCase
import com.marcelos.agendadecontatos.domain.usecase.GetContactsUseCase
import com.marcelos.agendadecontatos.domain.usecase.SaveContactUseCase
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ContactsViewModel(
    private val saveContactUseCase: SaveContactUseCase,
    private val getContactsUseCase: GetContactsUseCase,
    private val getContactUseCase: GetContactUseCase,
    private val deleteContactUseCase: DeleteContactUseCase
) : ViewModel() {

    private val _contactId = MutableStateFlow<Int?>(null)
    val contactId = _contactId.asStateFlow()

    private val _image = MutableStateFlow<ImageBitmap?>(null)
    val image = _image.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _surname = MutableStateFlow("")
    val surname = _surname.asStateFlow()

    private val _age = MutableStateFlow("")
    val age = _age.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone = _phone.asStateFlow()

    private val _nameError = MutableStateFlow(false)
    val nameError = _nameError.asStateFlow()

    private val _surnameError = MutableStateFlow(false)
    val surnameError = _surnameError.asStateFlow()

    private val _ageError = MutableStateFlow(false)
    val ageError = _ageError.asStateFlow()

    private val _phoneError = MutableStateFlow(false)
    val phoneError = _phoneError.asStateFlow()

    private val _viewStateGetContacts =
        MutableStateFlow<State<List<ContactsViewData>>>(State.Loading())
    var viewStateGetContacts = _viewStateGetContacts.asStateFlow()

    private val _viewStateSaveContact = MutableStateFlow<State<ContactsViewData>>(State.Loading())
    val viewStateSaveContact = _viewStateSaveContact.asStateFlow()

    private val _viewStateDeleteContact =
        MutableStateFlow<State<Unit>>(State.Loading())
    var viewStateDeleteContact = _viewStateDeleteContact.asStateFlow()

    fun getContacts() = viewModelScope.launch {
        getContactsUseCase().onStart {
            _viewStateGetContacts.value = State.Loading()
        }.catch { throwable ->
            _viewStateGetContacts.value = State.Error(throwable)
        }.collect { result ->
            _viewStateGetContacts.value = result
        }
    }

    fun getContact(contactId: Int) = viewModelScope.launch {
        getContactUseCase(contactId).onStart {
            _viewStateGetContacts.value = State.Loading()
        }.catch { throwable ->
            _viewStateGetContacts.value = State.Error(throwable)
        }.collect { result ->
            _viewStateGetContacts.value = result
        }
    }

    fun deleteContact(contactId: Int) = viewModelScope.launch {
        deleteContactUseCase(contactId).onStart {
            _viewStateDeleteContact.value = State.Loading()
        }.catch { throwable ->
            _viewStateDeleteContact.value = State.Error(throwable)
        }.collect { result ->
            _viewStateDeleteContact.value = result
        }
    }

    fun resetDeleteContactState() {
        _viewStateDeleteContact.value = State.Loading()
    }

    fun saveContact(
        contactId: Int? = null,
        imagePath: String?,
        name: String,
        surname: String,
        age: Int,
        phone: String
    ) = viewModelScope.launch {
        saveContactUseCase(
            id = contactId,
            imagePath = imagePath,
            name = name,
            surname = surname,
            age = age,
            phone = phone
        ).onStart {
            _viewStateSaveContact.value = State.Loading()
        }.catch { throwable ->
            _viewStateSaveContact.value = State.Error(throwable)
        }.collect { result ->
            _viewStateSaveContact.value = result
        }
    }

    fun updateContactId(id: Int?) = viewModelScope.launch {
        _contactId.value = id
    }

    fun updateImage(newImage: ImageBitmap?) = viewModelScope.launch {
        _image.value = newImage
    }

    fun updateName(newName: String) = viewModelScope.launch {
        _name.value = newName
        _nameError.value = false
    }

    fun updateSurname(newSurname: String) = viewModelScope.launch {
        _surname.value = newSurname
        _surnameError.value = false
    }

    fun updateAge(newAge: String) = viewModelScope.launch {
        _age.value = newAge
        _ageError.value = false
    }

    fun updatePhone(newPhone: String) = viewModelScope.launch {
        _phone.value = newPhone
        _phoneError.value = false
    }

    fun validateFields(): Boolean {
        val nameError = _name.value.isEmpty()
        val surnameError = _surname.value.isEmpty()
        val ageError = _age.value.isEmpty()
        val phoneError = _phone.value.isEmpty()

        _nameError.value = nameError
        _surnameError.value = surnameError
        _ageError.value = ageError
        _phoneError.value = phoneError

        return !nameError && !surnameError && !ageError && !phoneError
    }
}