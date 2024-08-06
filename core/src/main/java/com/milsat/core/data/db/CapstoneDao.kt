package com.milsat.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.milsat.core.data.db.entities.FieldsEntity

@Dao
interface CapstoneDao {

    @Insert
    suspend fun insert(fieldsEntity: FieldsEntity)

    @Query("SELECT * FROM fields WHERE formId = :formId")
    suspend fun getFieldsByFormId(formId: Int): List<FieldsEntity>
}