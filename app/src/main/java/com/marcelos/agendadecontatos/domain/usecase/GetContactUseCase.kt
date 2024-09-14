package com.marcelos.agendadecontatos.domain.usecase

import com.marcelos.agendadecontatos.domain.model.ContactsViewData
import com.marcelos.agendadecontatos.domain.repository.ContactRepository
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class GetContactUseCase(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(
        contactId: Int
    ): Flow<State<List<ContactsViewData>>> = flow {
        try {
            val contacts = repository.getContact(contactId).first()
            emit(State.Success(contacts))
        } catch (e: Throwable) {
            emit(State.Error(e))
        }
    }
}