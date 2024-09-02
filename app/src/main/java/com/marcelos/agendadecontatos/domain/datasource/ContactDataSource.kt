package com.marcelos.agendadecontatos.domain.datasource

import com.marcelos.agendadecontatos.domain.model.ContactViewData
import kotlinx.coroutines.flow.Flow

interface ContactDataSource {

    suspend fun getContacts(): Flow<List<ContactViewData>>

    suspend fun insertContacts(contacts: ContactViewData)

    suspend fun deleteContact(idContact: Int)
}