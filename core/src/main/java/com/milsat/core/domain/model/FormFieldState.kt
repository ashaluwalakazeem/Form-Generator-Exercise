package com.milsat.core.domain.model

import androidx.compose.runtime.Stable
import com.milsat.core.data.db.entities.FieldsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Stable
data class FormFieldState(
    val fieldsEntity: FieldsEntity,
) {
    private val _tempValue = MutableStateFlow(fieldsEntity.columnValue)
    val tempValue = _tempValue.asStateFlow()
    private val _isTempValueInValid = MutableStateFlow(true)
    val isTempValueInValid = _isTempValueInValid.asStateFlow()

    init {
        _isTempValueInValid.update { isNotValid() }
    }

    fun setValue(value: String) {
        if (fieldsEntity.maxLength == null || value.length <= fieldsEntity.maxLength) {
            _tempValue.value = value
        }
        _isTempValueInValid.update { isNotValid() }
    }

    private fun isNotValid(): Boolean {
        return (fieldsEntity.required && _tempValue.value.isBlank()) || (fieldsEntity.maxLength != null && tempValue.value.length > fieldsEntity.maxLength) || (fieldsEntity.minLength != null && tempValue.value.length < fieldsEntity.minLength)
    }
}