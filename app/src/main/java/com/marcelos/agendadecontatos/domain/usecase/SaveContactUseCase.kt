package com.marcelos.agendadecontatos.domain.usecase

import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.domain.repository.ContactRepository
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class SaveContactUseCase(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(
        id: Int?,
        imagePath: String?,
        name: String,
        surname: String,
        age: Int,
        phone: String
    ): Flow<State<ContactsViewData>> = flow {
        try {
            val contact = ContactsViewData(
                id = id,
                imagePath = imagePath,
                name = name,
                surname = surname,
                age = age,
                phone = phone
            )
            repository.insertContact(
                contact
            )
            emit(State.Success(contact))
        } catch (e: Throwable) {
            emit(State.Error(e))
        }
    }
}