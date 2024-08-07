package com.milsat.core.domain.usecase

import com.milsat.core.domain.model.FormPage
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.utils.ErrorResponse
import com.milsat.core.utils.FormPageGenerator
import com.milsat.core.utils.Logger
import com.milsat.core.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchFormFieldsUseCase(
    private val formRepository: FormRepository,
    private val formPageGenerator: FormPageGenerator
) {

    suspend operator fun invoke(formId: Int): Result<List<FormPage>> {
        return try {
            withContext(Dispatchers.IO) {
                formPageGenerator.generateFormPages(formRepository.fetchAllFormFields(formId))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.error(TAG, "${e.message}")
            Result.Error(ErrorResponse.FormFieldError())
        }
    }

    companion object {
        private const val TAG = "FetchFormFieldsUseCase"
    }
}