package com.milsat.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.data.db.entities.FormEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CapstoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormEntity(formEntity: FormEntity): Long

    @Query("SELECT * FROM forms")
    fun getAllFormEntities(): Flow<List<FormEntity>>

    @Query("SELECT * FROM forms WHERE id = :formId LIMIT 1")
    fun getFormEntity(formId: Int): FormEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFieldsEntity(fieldsEntity: FieldsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFieldsEntities(fieldsEntities: List<FieldsEntity>)



    @Update
    suspend fun updateFieldEntity(fieldsEntity: FieldsEntity): Int

    @Update
    suspend fun updateFieldEntities(fieldsEntities: List<FieldsEntity>)

    @Query("SELECT * FROM fields WHERE formId = :formId")
    suspend fun getFieldsByFormId(formId: Int): List<FieldsEntity>

}