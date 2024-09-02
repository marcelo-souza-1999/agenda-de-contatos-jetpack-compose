package com.marcelos.agendadecontatos.domain.usecase

import com.marcelos.agendadecontatos.domain.model.ContactViewData
import com.marcelos.agendadecontatos.domain.repository.ContactRepository
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class SaveContactUseCase(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(
        image: ByteArray?,
        name: String,
        surname: String,
        age: Int,
        phone: String
    ): Flow<State<ContactViewData>> = flow {
        try {
            val contact = ContactViewData(
                id = UUID.randomUUID(),
                image = image,
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