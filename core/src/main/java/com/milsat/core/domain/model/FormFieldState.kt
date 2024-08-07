package com.milsat.core.domain.model

import androidx.compose.runtime.Stable
import com.milsat.core.data.db.entities.FieldsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
data class FormFieldState(
    val fieldsEntity: FieldsEntity,
) {
    private val _tempValue = MutableStateFlow(fieldsEntity.columnValue)
    val tempValue = _tempValue.asStateFlow()
    private val _isTempValueInValid = MutableStateFlow(true)
    val isTempValueInValid = _isTempValueInValid.asStateFlow()
    private val _isFormEnabled = MutableStateFlow(true)
    val isFormEnabled = _isFormEnabled.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            isFormEnabled.collect {
                _isTempValueInValid.update { isNotValid() }
            }
        }
    }

    fun setValue(value: String) {
        if (fieldsEntity.maxLength == null || value.length <= fieldsEntity.maxLength) {
            _tempValue.value = value
        }
        _isTempValueInValid.update { isNotValid() }
    }

    private fun isNotValid(): Boolean {
        return ((fieldsEntity.required && _tempValue.value.isBlank()) || (fieldsEntity.maxLength != null && tempValue.value.length > fieldsEntity.maxLength) || (fieldsEntity.minLength != null && tempValue.value.length < fieldsEntity.minLength)) && _isFormEnabled.value
    }

    fun setEnabled(value: Boolean){
        _isFormEnabled.update { value }
    }
}