package com.marcelos.agendadecontatos.di.modules

import android.content.Context
import androidx.room.Room
import com.marcelos.agendadecontatos.data.database.ContactDatabase
import com.marcelos.agendadecontatos.utils.Constants.DATABASE_NAME
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent

@Module
@Single
class DatabaseModule : KoinComponent {

    @Single
    fun providesDatabase(context: Context) = Room.databaseBuilder(
        context = context,
        klass = ContactDatabase::class.java,
        name = DATABASE_NAME
    ).build()

    @Single
    fun providesContactDao(database: ContactDatabase) = database.createContactDao()
}