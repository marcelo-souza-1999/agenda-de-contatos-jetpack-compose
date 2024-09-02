package com.marcelos.agendadecontatos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marcelos.agendadecontatos.data.entitys.ContactEntity
import com.marcelos.agendadecontatos.utils.Constants.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY name ASC")
    fun getContacts(): Flow<List<ContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    suspend fun deleteContact(id: Int)
}