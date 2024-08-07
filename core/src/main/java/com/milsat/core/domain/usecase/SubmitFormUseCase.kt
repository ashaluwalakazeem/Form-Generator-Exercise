package com.milsat.core.domain.usecase

import com.milsat.core.domain.model.FormPage
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.utils.ErrorResponse
import com.milsat.core.utils.FormPageGenerator
import com.milsat.core.utils.Result

class SubmitFormUseCase(
    private val formRepository: FormRepository,
    private val formPageGenerator: FormPageGenerator
) {

    private fun isAllFieldsReadyForSubmission(formPages: List<FormPage>): Boolean {
        val isNotReadyForSubmission = formPages.any { item ->
            item.formFields.any {
                it.isTempValueInValid.value
            }
        }
        return isNotReadyForSubmission.not()
    }

    suspend operator fun invoke(formPages: List<FormPage>): Result<Boolean> {
        return try {
            if(isAllFieldsReadyForSubmission(formPages).not()){
                return Result.Success(false, msg = "Form is invalid. Please do the needful")
            }
            val fieldEntities = formPageGenerator.transformFormPagesToFieldEntities(formPages)
            formRepository.submitForm(fieldsEntities = fieldEntities)
            Result.Success(true, "Form has been saved successfully.")
        } catch (e: Exception){
            Result.Error(ErrorResponse.FormFieldError("An unexpected error occurred while transforming formPages."))
        }
    }
}