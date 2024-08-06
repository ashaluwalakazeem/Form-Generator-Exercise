package com.milsat.core.utils

import androidx.compose.runtime.mutableStateOf
import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.domain.model.FormFieldState
import com.milsat.core.domain.model.FormPage

class FormPageGenerator() {

    fun generateFormPages(fieldsEntities: List<FieldsEntity>): Result<List<FormPage>> {
        return try {
            Result.Success(
                fieldsEntities.filter { it.showOnList == null || it.showOnList }.map {
                    FormFieldState(
                        fieldsEntity = it,
                        tempValue = mutableStateOf(it.columnValue)
                    )
                }.groupBy { it.fieldsEntity.pageName }.map {
                    FormPage(name = it.key, formFields = it.value)
                }
            )
        } catch (e: Exception) {
            Result.Error(ErrorResponse.FormFieldError())
        }
    }

    companion object {
        private const val TAG = "FormPageGenerator"
    }
}