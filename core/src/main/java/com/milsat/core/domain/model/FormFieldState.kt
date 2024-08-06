package com.milsat.core.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import com.milsat.core.data.db.entities.FieldsEntity

@Stable
data class FormFieldState(
    val fieldsEntity: FieldsEntity,
    val tempValue: MutableState<String>
)