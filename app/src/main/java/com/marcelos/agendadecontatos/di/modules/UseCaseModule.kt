package com.marcelos.agendadecontatos.di.modules

import com.marcelos.agendadecontatos.domain.repository.ContactRepository
import com.marcelos.agendadecontatos.domain.usecase.DeleteContactUseCase
import com.marcelos.agendadecontatos.domain.usecase.GetContactUseCase
import com.marcelos.agendadecontatos.domain.usecase.GetContactsUseCase
import com.marcelos.agendadecontatos.domain.usecase.SaveContactUseCase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Single
class UseCaseModule {

    @Single
    fun providesGetContactsUseCase(
        repository: ContactRepository
    ) = GetContactsUseCase(repository)

    @Single
    fun providesGetContactUseCase(
        repository: ContactRepository
    ) = GetContactUseCase(repository)

    @Single
    fun providesSaveContactUseCase(
        repository: ContactRepository
    ) = SaveContactUseCase(repository)

    @Single
    fun providesDeleteContactUseCase(
        repository: ContactRepository
    ) = DeleteContactUseCase(repository)
}