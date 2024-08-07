package com.milsat.capstone.ui.screens.form

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milsat.capstone.ui.screens.form.components.FieldTitle
import com.milsat.capstone.ui.screens.form.components.Index
import com.milsat.capstone.utils.ArgumentBundleKeys
import com.milsat.core.domain.model.FormPage
import com.milsat.core.domain.usecase.FetchFormEntityUseCase
import com.milsat.core.domain.usecase.FetchFormFieldsUseCase
import com.milsat.core.domain.usecase.SubmitFormUseCase
import com.milsat.core.utils.CsvUtils
import com.milsat.core.utils.Logger
import com.milsat.core.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FormScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchFormFieldsUseCase: FetchFormFieldsUseCase,
    private val submitFormUseCase: SubmitFormUseCase,
    private val csvUtils: CsvUtils,
    private val fetchFormEntityUseCase: FetchFormEntityUseCase
) : ViewModel(), KoinComponent {
    private val context: Context by inject()

    private val formId = savedStateHandle.get<String>(ArgumentBundleKeys.Form.FORM_ID)
    private val _formPagesState = MutableStateFlow(FormScreenState())
    val formPagesState = _formPagesState.asStateFlow()

    init {
        initializeForm()
    }

    private fun initializeForm() {
        val formId = formId?.toIntOrNull() ?: return

        viewModelScope.launch {
            when (val result = fetchFormFieldsUseCase(formId)) {
                is Result.Error -> {
                    Logger.debug(TAG, "initializeForm error | ${result.cause.message}")
                    Toast.makeText(context, result.cause.message, Toast.LENGTH_LONG).show()
                }

                is Result.Success -> {
                    _formPagesState.update {
                        it.copy(formPages = result.data)
                    }
                    initializeSkipLogic()
                }
            }
            Logger.debug(TAG, "${_formPagesState.value}")
        }
    }

    private fun initializeSkipLogic() {
        _formPagesState.value.formPages.forEachIndexed { pageIndex, formPage ->
            formPage.formFields.forEachIndexed { fieldIndex, formFieldState ->
                if (formFieldState.fieldsEntity.skipTo != null) {
                    onSkipTo(
                        currentFieldIndex = fieldIndex,
                        skippingTo = formFieldState.fieldsEntity.skipTo ?: "",
                        selectedPageIndex = pageIndex,
                        enable = formFieldState.fieldsEntity.columnValue.lowercase() != "yes"
                    )
                }
            }
        }
    }

    fun submit() {
        viewModelScope.launch {
            when (val result = submitFormUseCase(formPages = formPagesState.value.formPages)) {
                is Result.Success -> {
                    Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show()
                }

                is Result.Error -> {
                    Toast.makeText(context, result.cause.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun exportForm() {
        try {
            val formId = formId?.toIntOrNull() ?: return
            viewModelScope.launch {
                val formEntity = fetchFormEntityUseCase(formId = formId)
                csvUtils.exportFieldEntitiesToCsv(formName = formEntity.name, formInt = formId)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Unexpected error occurred", Toast.LENGTH_LONG).show()
        }
    }

    fun onSkipTo(
        currentFieldIndex: Index,
        skippingTo: FieldTitle,
        selectedPageIndex: Index,
        enable: Boolean
    ) {
        val updatedFormPages = _formPagesState.value.formPages.toMutableList()

        // Disable all fields after the current field
        for (i in currentFieldIndex + 1 until updatedFormPages[selectedPageIndex].formFields.size) {
            // If we find the target field, stop disabling subsequent fields
            val formField = updatedFormPages[selectedPageIndex].formFields[i]
            if (formField.fieldsEntity.fieldTitle == skippingTo) {
                break
            }
            formField.setEnabled(enable)
        }

        _formPagesState.value = FormScreenState(updatedFormPages)
    }

    companion object {
        private const val TAG = "FormScreenViewModel"
    }
}

data class FormScreenState(
    val formPages: List<FormPage> = emptyList()
)