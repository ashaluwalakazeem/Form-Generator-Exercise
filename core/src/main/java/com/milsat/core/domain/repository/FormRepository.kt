package com.milsat.core.domain.repository

import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.utils.Result

interface FormRepository {

    suspend fun createNewForm(config: String): Result<FormEntity>
}