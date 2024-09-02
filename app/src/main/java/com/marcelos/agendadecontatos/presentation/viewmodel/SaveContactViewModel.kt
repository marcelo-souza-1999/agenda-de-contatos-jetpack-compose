package com.marcelos.agendadecontatos.presentation.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelos.agendadecontatos.domain.model.ContactViewData
import com.marcelos.agendadecontatos.domain.usecase.SaveContactUseCase
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import com.marcelos.agendadecontatos.utils.toByteArray
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SaveContactViewModel(
    private val saveContactUseCase: SaveContactUseCase
) : ViewModel() {

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

    private val _viewStateSaveContact =
        MutableStateFlow<State<ContactViewData>>(State.Loading())
    val viewStateSaveContact = _viewStateSaveContact.asStateFlow()

    fun saveContact(
        image: ImageBitmap?,
        name: String,
        surname: String,
        age: Int,
        phone: String
    ) = viewModelScope.launch {
        saveContactUseCase(
            image = image.toByteArray(),
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

    fun updateImage(newImage: ImageBitmap?) = viewModelScope.launch {
        _image.value = newImage
    }

    fun updateName(newName: String) =
        viewModelScope.launch {
            _name.value = newName
            _nameError.value = false
        }

    fun updateSurname(newSurname: String) =
        viewModelScope.launch {
            _surname.value = newSurname
            _surnameError.value = false
        }

    fun updateAge(newAge: String) =
        viewModelScope.launch {
            _age.value = newAge
            _ageError.value = false
        }

    fun updatePhone(newPhone: String) =
        viewModelScope.launch {
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