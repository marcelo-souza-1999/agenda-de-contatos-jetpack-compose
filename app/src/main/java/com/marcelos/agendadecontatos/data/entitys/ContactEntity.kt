package com.marcelos.agendadecontatos.data.entitys

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marcelos.agendadecontatos.utils.Constants.COLUMN_INFO_AGE
import com.marcelos.agendadecontatos.utils.Constants.COLUMN_INFO_ID
import com.marcelos.agendadecontatos.utils.Constants.COLUMN_INFO_IMAGE
import com.marcelos.agendadecontatos.utils.Constants.COLUMN_INFO_NAME
import com.marcelos.agendadecontatos.utils.Constants.COLUMN_INFO_PHONE
import com.marcelos.agendadecontatos.utils.Constants.COLUMN_INFO_SURNAME
import com.marcelos.agendadecontatos.utils.Constants.TABLE_NAME
import java.util.UUID

@Entity(tableName = TABLE_NAME)
data class ContactEntity(
    @ColumnInfo(name = COLUMN_INFO_ID)
    @PrimaryKey
    val id: UUID,
    @ColumnInfo(name = COLUMN_INFO_IMAGE)
    val image: ByteArray?,
    @ColumnInfo(name = COLUMN_INFO_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_INFO_SURNAME)
    val surname: String,
    @ColumnInfo(name = COLUMN_INFO_AGE)
    val age: Int,
    @ColumnInfo(name = COLUMN_INFO_PHONE)
    val phone: String,
)
