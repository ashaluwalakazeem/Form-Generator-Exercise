package com.milsat.core.domain.repository

import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.utils.Result
import kotlinx.coroutines.flow.Flow

interface FormRepository {

    suspend fun createNewForm(config: String): Result<FormEntity>

    fun getAllFormEntities(): Flow<List<FormEntity>>

    suspend fun fetchAllFormFields(formId: Int): List<FieldsEntity>
}