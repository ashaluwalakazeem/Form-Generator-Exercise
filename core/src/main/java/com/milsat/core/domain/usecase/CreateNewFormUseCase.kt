package com.milsat.core.domain.usecase

import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.utils.Result

class CreateNewFormUseCase(
    private val formRepository: FormRepository
) {

    suspend operator fun invoke(jsonString: String): Result<FormEntity> {
        return formRepository.createNewForm(jsonString)
    }
}