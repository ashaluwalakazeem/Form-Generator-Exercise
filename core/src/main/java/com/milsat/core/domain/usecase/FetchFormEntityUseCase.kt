package com.milsat.core.domain.usecase

import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchFormEntityUseCase(
    private val formRepository: FormRepository
) {

    suspend operator fun invoke(formId: Int): FormEntity {
        return withContext(Dispatchers.IO){
            formRepository.fetchFormEntity(formId)
        }
    }
}