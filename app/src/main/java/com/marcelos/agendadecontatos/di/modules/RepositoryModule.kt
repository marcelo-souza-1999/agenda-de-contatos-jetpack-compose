package com.marcelos.agendadecontatos.di.modules

import com.marcelos.agendadecontatos.data.repository.ContactRepositoryImpl
import com.marcelos.agendadecontatos.domain.datasource.ContactDataSource
import com.marcelos.agendadecontatos.domain.repository.ContactRepository
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Single
class RepositoryModule {

    @Single
    fun providesContactRepository(
        contactDataSource: ContactDataSource
    ): ContactRepository = ContactRepositoryImpl(contactDataSource)
}