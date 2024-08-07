package com.milsat.core.utils

import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.domain.model.FormFieldState
import com.milsat.core.domain.model.FormPage

class FormPageGenerator() {

    fun generateFormPages(fieldsEntities: List<FieldsEntity>): Result<List<FormPage>> {
        return try {
            val formPages = fieldsEntities
                .asSequence()
                .filter { it.showOnList == null || it.showOnList }
                .map {
                    FormFieldState(
                        fieldsEntity = it.copy(values = it.values?.filterNot { value -> value == "SELECT OPTION" })
                    )
                }
                .groupBy { it.fieldsEntity.pageName }
                .map { (pageName, formFields) ->
                    FormPage(name = pageName, formFields = formFields)
                }
                .toList()

            Result.Success(formPages)
        } catch (e: Exception) {
            Result.Error(ErrorResponse.FormFieldError())
        }
    }

    fun transformFormPagesToFieldEntities(formPages: List<FormPage>) =
        formPages.flatMap { formPage -> formPage.formFields.map { it.fieldsEntity.copy(columnValue = it.tempValue.value) } }

    companion object {
        private const val TAG = "FormPageGenerator"
    }
}