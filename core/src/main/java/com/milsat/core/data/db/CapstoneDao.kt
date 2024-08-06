package com.milsat.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.data.db.entities.FormEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CapstoneDao {

    @Insert
    suspend fun insertFormEntity(formEntity: FormEntity)

    @Query("SELECT * FROM forms")
    fun getAllFormEntities(): Flow<List<FormEntity>>


    @Insert
    suspend fun insertFieldsEntity(fieldsEntity: FieldsEntity)

    @Update
    suspend fun updateFieldEntity(fieldsEntity: FieldsEntity): Int

    @Query("SELECT * FROM fields WHERE formId = :formId")
    suspend fun getFieldsByFormId(formId: Int): List<FieldsEntity>

}