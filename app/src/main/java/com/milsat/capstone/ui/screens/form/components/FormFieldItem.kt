package com.milsat.capstone.ui.screens.form.components

import android.provider.Contacts.Intents.UI
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.milsat.capstone.ui.components.UIComponentTextField
import com.milsat.capstone.ui.components.UIComponentTextFieldWithDropDown
import com.milsat.capstone.utils.generateErrorMessage
import com.milsat.core.domain.model.ColumnType
import com.milsat.core.domain.model.FormFieldState
import com.milsat.core.domain.model.UIType
import com.milsat.core.domain.model.toKeyboardType

@Composable
fun FormFieldItem(
    modifier: Modifier = Modifier,
    formFieldState: FormFieldState
) {

    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(15.dp)
    ) {
        Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {

            when (formFieldState.fieldsEntity.uiType) {
                UIType.TEXT_FIELD -> {
                    val isError =
                        formFieldState.fieldsEntity.required && formFieldState.tempValue.value.isBlank() || (formFieldState.fieldsEntity.minLength != null && (formFieldState.fieldsEntity.minLength
                            ?: 0) > formFieldState.tempValue.value.length) || (formFieldState.fieldsEntity.maxLength != null && (formFieldState.tempValue.value.length > (formFieldState.fieldsEntity.maxLength
                            ?: 0)))

                    UIComponentTextField(
                        value = formFieldState.tempValue.value,
                        hint = formFieldState.fieldsEntity.columnName,
                        label = formFieldState.fieldsEntity.fieldTitle,
                        onValueChange = {
                            if (
                                formFieldState.fieldsEntity.maxLength == null || (it.length <= (formFieldState.fieldsEntity.maxLength
                                    ?: 0))
                            ) {
                                formFieldState.tempValue.value = it
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = formFieldState.fieldsEntity.columnType.toKeyboardType(),
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        isRequired = formFieldState.fieldsEntity.required,
                        isError = isError,
                        errorMessage = formFieldState.tempValue.value.generateErrorMessage(
                            fieldName = formFieldState.fieldsEntity.fieldTitle,
                            isRequired = formFieldState.fieldsEntity.required,
                            enableMaximum = formFieldState.fieldsEntity.maxLength != null,
                            maximumCount = formFieldState.fieldsEntity.maxLength ?: Int.MAX_VALUE,
                            enableMinimum = formFieldState.fieldsEntity.minLength != null,
                            minimumCount = formFieldState.fieldsEntity.minLength ?: 1,
                            isDigit = formFieldState.fieldsEntity.columnType == ColumnType.NUMBER,
                            isAnInputField = formFieldState.fieldsEntity.uiType == UIType.TEXT_FIELD
                        )
                    )
                }

                UIType.DROP_DOWN -> {
                    val options = formFieldState.fieldsEntity.values ?: emptyList()
                    val isError =
                        formFieldState.fieldsEntity.required && formFieldState.tempValue.value.isBlank()

                    UIComponentTextFieldWithDropDown(
                        value = formFieldState.tempValue.value,
                        hint = options.firstOrNull() ?: formFieldState.fieldsEntity.columnName,
                        label = formFieldState.fieldsEntity.fieldTitle,
                        onValueChange = {
                            if (
                                formFieldState.fieldsEntity.maxLength == null || (it.length < (formFieldState.fieldsEntity.maxLength
                                    ?: 0))
                            ) {
                                formFieldState.tempValue.value = it
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = formFieldState.fieldsEntity.columnType.toKeyboardType(),
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        isRequired = formFieldState.fieldsEntity.required,
                        isError = isError,
                        errorMessage = formFieldState.tempValue.value.generateErrorMessage(
                            fieldName = formFieldState.fieldsEntity.fieldTitle,
                            isRequired = formFieldState.fieldsEntity.required,
                            enableMaximum = formFieldState.fieldsEntity.maxLength != null,
                            maximumCount = formFieldState.fieldsEntity.maxLength ?: Int.MAX_VALUE,
                            enableMinimum = formFieldState.fieldsEntity.minLength != null,
                            minimumCount = formFieldState.fieldsEntity.minLength ?: 1,
                            isDigit = formFieldState.fieldsEntity.columnType == ColumnType.NUMBER,
                            isAnInputField = formFieldState.fieldsEntity.uiType == UIType.TEXT_FIELD
                        ),
                        options = options,
                        onOptionClicked = {
                            formFieldState.tempValue.value = it
                        },
                    )
                }

                UIType.UNKNOWN -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "This UIType is Unknown. Please update the app to the latest apk or customer support if this issue persist",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}