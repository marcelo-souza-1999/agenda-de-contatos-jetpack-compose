package com.marcelos.agendadecontatos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marcelos.agendadecontatos.data.dao.ContactDao
import com.marcelos.agendadecontatos.data.entitys.ContactEntity

@Database(
    entities = [ContactEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun createContactDao(): ContactDao
}