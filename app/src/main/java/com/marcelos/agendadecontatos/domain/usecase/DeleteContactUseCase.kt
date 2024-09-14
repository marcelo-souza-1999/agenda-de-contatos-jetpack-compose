package com.marcelos.agendadecontatos.domain.usecase

import com.marcelos.agendadecontatos.domain.repository.ContactRepository
import com.marcelos.agendadecontatos.presentation.viewmodel.viewstate.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class DeleteContactUseCase(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(
        contactId: Int
    ): Flow<State<Unit>> = flow {
        try {
            emit(State.Success(repository.deleteContact(contactId)))
        } catch (e: Throwable) {
            emit(State.Error(e))
        }
    }
}