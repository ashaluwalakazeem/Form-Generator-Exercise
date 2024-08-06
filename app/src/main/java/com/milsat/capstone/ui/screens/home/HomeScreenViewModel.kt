package com.milsat.capstone.ui.screens.home

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.domain.usecase.CreateNewFormUseCase
import com.milsat.core.domain.usecase.GetAllFormUseCase
import com.milsat.core.utils.Logger
import com.milsat.core.utils.JsonFileSelector
import com.milsat.core.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeScreenViewModel(
    val jsonFileSelector: JsonFileSelector,
    private val createNewFormUseCase: CreateNewFormUseCase,
    getAllFormUseCase: GetAllFormUseCase,
) : ViewModel(), KoinComponent {
    private val context: Context by inject()

    val allFormsState = getAllFormUseCase()
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList())

    fun selectNewConfigurationFile(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>, onProceed: (formEntity: FormEntity) -> Unit) {
        jsonFileSelector.selectJsonFile(
            launcher = launcher,
            onJsonFileSelected = { jsonString: String ->
                viewModelScope.launch {
                    when(val formGenerationResult = createNewFormUseCase(jsonString = jsonString)){
                        is Result.Success -> {
                            onProceed(formGenerationResult.data)
                            Logger.debug(TAG, "Success: ${formGenerationResult.data}")
                        }
                        is Result.Error -> {
                            Toast.makeText(context, formGenerationResult.cause.message, Toast.LENGTH_LONG).show()
                            Logger.error(TAG, formGenerationResult.cause.message)
                        }
                    }
                }
            },
            onError = {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                Logger.debug(TAG, it)
            }
        )
    }

    companion object {
        private const val TAG = "HomeScreenViewModel"
    }
}