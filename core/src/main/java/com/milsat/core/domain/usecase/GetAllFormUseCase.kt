package com.milsat.core.domain.usecase

import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.domain.repository.FormRepository
import kotlinx.coroutines.flow.Flow

class GetAllFormUseCase(
    private val formRepository: FormRepository
) {

    operator fun invoke(): Flow<List<FormEntity>> {
        return formRepository.getAllFormEntities()
    }
}